package Client;

import GUI.GuiClientAuthorizer;
import TVCS.Toon.Cut;
import TVCS.Toon.CutImage;
import TVCS.Toon.Episode;
import TVCS.Toon.Toon;
import TVCS.Utils.FileManager;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.Socket;
/**
 * Created by ina on 2017-05-31.
 */
public class ClientBase {
    //TODO notice connection failed
    private Socket socket;

    private InputStream inputStream;
    private OutputStream outputStream;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public boolean disabled = false;

    long toonId;

    public ClientBase(String ip, int port, long toonId) {
        try {
            socket = getSocket(ip, port);
            this.toonId = toonId;
            InitStream();
            sendToonId(toonId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendToonId(long toonId) {
        try {
            dataOutputStream.writeInt(CommunicationType.SEND_TOONID.ordinal());
            dataOutputStream.writeLong(toonId);
        } catch (IOException e) {
            showDialogConnectingError();
            closeConnection();
        }
    }

    public boolean authorizeWithGui(GuiClientAuthorizer guiClientAuthorizer) {
        return guiClientAuthorizer.start();
    }

    public boolean authorize(String userId, String userPassword) {
        boolean succeed;
        try {
            dataOutputStream.writeInt(CommunicationType.AUTHORIZE.ordinal());
            dataOutputStream.writeUTF(userId);
            dataOutputStream.writeUTF(userPassword);
            succeed = dataInputStream.readBoolean();
            if (!succeed) {
                closeConnection();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showDialogConnectingError();
            closeConnection();
            return false;
        }
        return succeed;
    }

    public void allocToon(Toon toon) {
        try {
            dataOutputStream.writeInt(CommunicationType.ALLOCATION.ordinal());
            toonId = dataInputStream.readLong();
            toon.setToonId(toonId);
        } catch (IOException e) {
            showDialogConnectingError();
            closeConnection();
        }
    }

    public void pushEpisode(Episode episode) {
        try {
            dataOutputStream.writeInt(CommunicationType.PUSH_EPISODE.ordinal());
            pushEpisodeInfo(episode);
            dataOutputStream.writeInt(episode.numCuts());
            for (Cut cut : episode.cuts) {
                pushCut(cut);
            }
        } catch (IOException e) {
            showDialogConnectingError();
            closeConnection();
        }
    }

    private void pushEpisodeInfo(Episode episode) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(episode);
    }

    private void pushCut(Cut cut) throws IOException {
        dataOutputStream.writeUTF(cut.id().toString());
        dataOutputStream.writeInt(cut.imageNum());
        for(CutImage image : cut.images) {
            pushImage(image);
        }
    }

    private void pushImage(CutImage image) throws IOException {
        dataOutputStream.writeUTF(image.id().toString());
        FileManager.smallFilePush(image.cutImagePath(), outputStream);
    }

    public void clientEnd() {
        try {
            dataOutputStream.writeInt(CommunicationType.DONE.ordinal());
            closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            disabled = true;
        }
    }

    private Socket getSocket(String ip, int port) {
        Socket socket = null;
        try {
            socket = new Socket(ip, port);
        } catch (Exception e) {
            showDialogConnectingError();
        }
        if (socket == null) {
            showDialogConnectingError();
            disabled = true;
        }
        return socket;
    }

    private void InitStream() throws IOException {
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        dataOutputStream = new DataOutputStream(outputStream);
        dataInputStream = new DataInputStream(inputStream);
    }

    private void showDialogConnectingError() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Something wrong");
        alert.setContentText("Connection with server broken");
        alert.showAndWait();
    }
}
