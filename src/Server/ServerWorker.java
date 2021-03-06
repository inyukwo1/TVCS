package Server;

        import Client.CommunicationType;
        import TVCS.Toon.Episode;
        import TVCS.Toon.ToonInfo;

        import java.io.*;
        import java.math.BigInteger;
        import java.net.Socket;

/**
 * Created by ina on 2017-05-31.
 */
public class ServerWorker implements Runnable {
    ///TODO do something(remove conflicting files..) when connection failed
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private ServerToonManager serverToonManager;


    public ServerWorker(Socket socket) {
        this.socket = socket;
        serverToonManager = new ServerToonManager();
    }

    public void run() {
        if(!InitStreamAndReader()) {
            System.out.println("Something Wrong Initializing Stream and Reader");
            CloseBufferAndSocket();
            return;
        }
        try {
            while(Communicate()) {
            }
            System.out.println("Successfully done.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("클라이언트를 강제로 종료했습니다. ");
        }finally{
            CloseBufferAndSocket();
        }
    }
    private boolean InitStreamAndReader() {
        try {
            inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);
            outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private void CloseBufferAndSocket() {
        try {
            dataInputStream.close();
            dataOutputStream.close();
            if(socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean Communicate() throws IOException {
        CommunicationType communicationType = getCommunicationType();
        switch (communicationType) {
            case DONE:
                dataOutputStream.write(0);
                return false;
            case AUTHORIZE:
                authorization();
                break;
            case SEND_TOONID:
                receiveToonId();
                break;
            case ALLOCATION:
                allocAndSendToonId();
                break;
            case PUSH_EPISODE:
                receiveAndSaveEpisode();
                break;
            case PULL_EPISODE:
                pushEpisode();
                break;
        }
        return true;
    }

    private CommunicationType getCommunicationType() throws IOException {
        return CommunicationType.values()[dataInputStream.readInt()];
    }

    private void authorization() throws IOException {
        ServerAuthorizer serverAuthorizer = new ServerAuthorizer();
        String userId = dataInputStream.readUTF();
        String userPassword = dataInputStream.readUTF();
        serverAuthorizer.setUserInfo(userId, userPassword);
        boolean authorized = serverAuthorizer.authorize();
        dataOutputStream.writeBoolean(authorized);
    }

    private void receiveToonId() throws IOException {
        serverToonManager.toonId = dataInputStream.readLong();
        System.out.println("Toon ID: " + serverToonManager.toonId);
    }

    private void allocAndSendToonId() throws IOException {
        long newToonId = ServerResourceManager.ALLOCATE_TOON_ID();
        serverToonManager.toonId = newToonId;
        dataOutputStream.writeLong(newToonId);
    }

    private void receiveAndSaveEpisode() throws IOException {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ToonInfo toonInfo = (ToonInfo) objectInputStream.readObject();
            dataOutputStream.writeInt(0);
            Episode receivedEpisode = (Episode) objectInputStream.readObject();
            serverToonManager.saveToonInfo(toonInfo);
            serverToonManager.readyToSaveEpisode(receivedEpisode);
            serverToonManager.saveEpisodeInfo(receivedEpisode);
            int numCuts = dataInputStream.readInt();
            for (int i = 0 ; i < numCuts ; i++) {
                receiveAndSaveCut();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void receiveAndSaveCut() throws IOException {
        BigInteger cutId = new BigInteger(dataInputStream.readUTF());
        int numImages = dataInputStream.readInt();
        serverToonManager.readyToSaveCut(cutId);
        for (int i = 0 ; i < numImages ; i++) {
            receiveAndSaveImage(cutId);
        }
    }

    private void receiveAndSaveImage(BigInteger cutId) throws IOException {
        String imageIdstring = dataInputStream.readUTF();
        System.out.println(imageIdstring);
        BigInteger imageId = new BigInteger(imageIdstring);
        serverToonManager.pullAndSaveImage(imageId, inputStream);
        dataOutputStream.write(0);
    }

    private void pushEpisode() throws IOException {
        try {
            receiveToonId();
            ObjectInputStream objectInputStream = new ObjectInputStream(dataInputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(dataOutputStream);
            Episode receivedEpisode = (Episode) objectInputStream.readObject();
            System.out.println("A");
            objectOutputStream.writeInt(0);
            objectOutputStream.flush();
            System.out.println("for check: " + objectInputStream.readInt());
            if (!serverToonManager.findEpisode(receivedEpisode)) {
                objectOutputStream.writeBoolean(false);
                objectOutputStream.flush();
                return;
            }
            objectOutputStream.writeBoolean(true);
            objectOutputStream.flush();
            serverToonManager.pushCuts(objectInputStream, outputStream, objectOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
