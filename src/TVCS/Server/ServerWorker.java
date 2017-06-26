package TVCS.Server;

import TVCS.Toon.Branch;
import TVCS.Toon.Episode;
import TVCS.Toon.ToonInfo;
import TVCS.Utils.FileManager;

import java.io.*;
import java.net.Socket;

/**
 * Created by ina on 2017-05-31.
 */
public class ServerWorker implements Runnable {
    private Socket socket;
    private InputStream input_stream;
    private OutputStream output_stream;
    private DataInputStream data_input_stream;
    private DataOutputStream data_output_stream;

    private ServerAuthorizer serverAuthorizer;
    private ServerToonManager serverToonManager;

    public ServerWorker(Socket socket) {
        this.socket = socket;
        serverAuthorizer = new ServerAuthorizer();
        serverToonManager = new ServerToonManager();
    }

    public void run() {
        if(!InitStreamAndReader()) {
            System.out.println("Something Wrong Initializing Stream and Reader");
            CloseBufferAndSocket();
            return;
        }
        if(!Authorization()) {
            System.out.println("Authorization failed");
            CloseBufferAndSocket();
            return;
        }
        try {
            while(Communicate()) {
            }
            System.out.println("Successfully done.");
        } catch (Exception e) {
            System.out.println("클라이언트를 강제로 종료했습니다. ");
        }finally{
            CloseBufferAndSocket();
        }
    }
    private boolean InitStreamAndReader() {
        try {
            input_stream = socket.getInputStream();
            data_input_stream = new DataInputStream(input_stream);
            output_stream = socket.getOutputStream();
            data_output_stream = new DataOutputStream(output_stream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private void CloseBufferAndSocket() {
        try {
            if(socket != null) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean Authorization() {
        try {
            serverAuthorizer.setUserName(data_input_stream.readUTF());
            boolean authorized = serverAuthorizer.Authorize(data_input_stream.readUTF());
            data_output_stream.writeBoolean(authorized);
            return authorized;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean Communicate() throws IOException{
        String worktype = GetTypeOfWork();
        if (worktype.equals("ALLOC")) {
            AllocCommunicate();
        } else if (worktype.equals("PUSH")) {
            PushCommunicate();
        } else if (worktype.equals("PULL")) {
        } else if (worktype.equals("DONE")) {
            return false;
        }
        return true;
    }

    private String GetTypeOfWork() throws IOException{
        return data_input_stream.readUTF();
    }

    private void AllocCommunicate() throws IOException {
        int newToonId = serverToonManager.AllocateToonId();
        System.out.println("Toon ID allocated");
        data_output_stream.writeInt(newToonId);
        System.out.println("Toon ID sent");
        ToonInfoPushCommunicate(newToonId);
    }

    private void PushCommunicate() throws IOException {
        //TODO Can cause performance issue
        int toonId = data_input_stream.readInt();
        String pushType = data_input_stream.readUTF();
        //TODO We just overlay all the files.. to be developed
        if (pushType.equals("tooninfo")) {
            ToonInfoPushCommunicate(toonId);
        } else if (pushType.equals("branch")) {
            BranchPushCommunicate(toonId);
        } else if (pushType.equals("episode")) {
            ScenePushCommunicate(toonId);
        } else {
            System.out.println("Wrong communication!");
        }
    }

    private void ToonInfoPushCommunicate(int toonId) throws IOException {
        ObjectInputStream objectInputStream = new ObjectInputStream(data_input_stream);
        try {
            ToonInfo receivedToonInfo = (ToonInfo) objectInputStream.readObject();
            String toonInfoPath = ServerToonManager.homePath + File.separator + toonId + File.separator + "tooninfo";
            FileManager.SaveSerializableObject(receivedToonInfo, toonInfoPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void BranchPushCommunicate(int toonId) throws IOException {
        ObjectInputStream objectInputStream = new ObjectInputStream(data_input_stream);
        try {
            Branch receivedBranch = (Branch) objectInputStream.readObject();
            String branchPath = ServerToonManager.homePath + File.separator + toonId + File.separator + "branch";
            FileManager.SaveSerializableObject(receivedBranch, branchPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void ScenePushCommunicate(int toonId) throws IOException {
        String sceneName = data_input_stream.readUTF();
        String scenePath = serverToonManager.homePath + File.separator + toonId + File.separator + sceneName;
        FileManager.DeleteDirectory(scenePath);
        FileManager.MakeDirectory(scenePath);
        SceneInfoPushCommunicate(scenePath + File.separator + sceneName);
        int numCuts = data_input_stream.readInt();
        for(int i = 0 ; i < numCuts ; i++) {
            CutPushCommunicate(scenePath);
        }
    }

    private void SceneInfoPushCommunicate(String sceneInfoPath) throws IOException{
        ObjectInputStream objectInputStream = new ObjectInputStream(data_input_stream);
        try {
            Episode receivedEpisode = (Episode) objectInputStream.readObject();
            FileManager.SaveSerializableObject(receivedEpisode, sceneInfoPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void CutPushCommunicate(String scenePath) throws IOException{
        long cutId= data_input_stream.readLong();
        int imageNum = data_input_stream.readInt();
        FileManager.MakeDirectory(scenePath + File.separator + cutId);
        for(int i = 0 ; i < imageNum ; i++) {
            long imageId = data_input_stream.readLong();
            String imagePath = scenePath + File.separator + cutId + File.separator + imageId + ".jpg";
            FileManager.smallFilePull(imagePath, input_stream);
        }
    }
}
