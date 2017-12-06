package Client;

import GUI.CutManager;
import GUI.EpisodeManager;
import GUI.GuiClientAuthorizer;
import TVCS.Toon.*;
import TVCS.Utils.FileManager;
import TVCS.WorkSpace;
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
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            pushToonInfo(episode.parentToon.toon_info, objectOutputStream);
            dataInputStream.readInt();
            pushEpisodeInfo(episode, objectOutputStream);
            dataOutputStream.writeInt(episode.numCuts());
            for (Cut cut : episode.cuts) {
                pushCut(cut);
            }
        } catch (IOException e) {
            showDialogConnectingError();
            closeConnection();
        }
    }

    private void pushToonInfo(ToonInfo toonInfo, ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(toonInfo);
    }

    private void pushEpisodeInfo(Episode episode, ObjectOutputStream objectOutputStream) throws IOException {
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
        System.out.println(image.id().toString());
        FileManager.smallFilePush(image.cutImagePath(), outputStream);
        dataInputStream.read();
        System.out.println("Image pushed");
    }

    public void pullEpisode(EpisodeManager episodeManager) {
        try {
            dataOutputStream.writeInt(CommunicationType.PULL_EPISODE.ordinal());
            dataOutputStream.writeLong(episodeManager.episode.toonId());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(dataOutputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(dataInputStream);
            pushEpisodeInfo(episodeManager.episode, objectOutputStream);
            System.out.println("A");
            System.out.println("for check: " + objectInputStream.readInt());
            objectOutputStream.writeInt(0);
            objectOutputStream.flush();
            if (!objectInputStream.readBoolean()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No related episode found");
                alert.setContentText("No related episode found");
                alert.showAndWait();
                return;
            }
            int numCuts = objectInputStream.readInt();
            System.out.println("Have to receive " + numCuts +" cuts");
            for (int i = 0 ; i < numCuts; i++) {
                receiveCut(episodeManager, objectInputStream, objectOutputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showDialogConnectingError();
            closeConnection();
        }
    }

    private void receiveCut(EpisodeManager episodeManager, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) throws IOException {
        try {
            Cut receiveCut = (Cut) objectInputStream.readObject();
            Episode episode = WorkSpace.WorkingEpisode();
            boolean cutExists = false;
            Cut correspondingCut;
            for (Cut cut: episode.cuts) {
                if (cut.id() == receiveCut.id()) {
                    cutExists = true;
                    correspondingCut = cut;
                    break;
                }
            }

            if (cutExists) {
                //TODO handle conflict
                System.out.println("Not implemented Yet");
            } else {
                String cutPath = episode.sceneDirPath() + File.separator + receiveCut.id();
                FileManager.MakeDirectory(cutPath);
                System.out.println("Have to receive " + receiveCut.images.size() + " images");
                for (CutImage image: receiveCut.images) {
                    FileManager.smallFilePull(cutPath +  File.separator + image.id() + ".png", inputStream);
                    objectOutputStream.writeInt(0);
                    objectOutputStream.flush();
                    System.out.println("for check: " + objectInputStream.readInt());
                }
                System.out.println("Images received");
                receiveCut.Loadtransient(WorkSpace.WorkingToon(), episode);
                episode.cuts.add(receiveCut);
                receiveCut.makeShowingImage();

                CutManager newCutManager = new CutManager(receiveCut, episodeManager);
                episodeManager.cutManagers.add(newCutManager);
                newCutManager.setUp();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void clientEnd() {
        try {
            dataOutputStream.writeInt(CommunicationType.DONE.ordinal());
            dataInputStream.read();
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
