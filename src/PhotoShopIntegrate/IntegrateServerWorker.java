package PhotoShopIntegrate;

import Client.CommunicationType;
import GUI.CutManager;
import GUI.EpisodeManager;
import Server.ServerAuthorizer;
import Server.ServerResourceManager;
import Server.ServerToonManager;
import TVCS.Toon.Episode;
import TVCS.WorkSpace;
import javafx.application.Platform;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;

/**
 * Created by ina on 2017-12-01.
 */
public class IntegrateServerWorker implements Runnable {
    ///TODO do something(remove conflicting files..) when connection failed
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private String cutName = "-";
    private int[] bounds = new int[4];

    public static String INTEGRATE_PATH = "C:\\Users\\ina\\PhotoshopToTVCS\\";

    public IntegrateServerWorker(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        if(!InitStreamAndReader()) {
            System.out.println("Something Wrong Initializing Stream and Reader");
            CloseBufferAndSocket();
            return;
        }
        try {
            acceptCutName();
            acceptBounds();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addRectAndImages();
                }
            });
            System.out.println("Successfully done, cutname: " +
                    cutName + ", bounds: " + bounds[0] + "," + bounds[1] + "," + bounds[2] + "," + bounds[3]);
            dataOutputStream.writeChar(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("클라이언트를 강제로 종료했습니다. ");
        } finally{
            CloseBufferAndSocket();
        }
    }

    private void acceptCutName() throws IOException {
        byte ascii;
        while ((ascii = dataInputStream.readByte()) != 10) {
            cutName += Character.toString((char) ascii);
        }
    }

    private void acceptBounds() throws IOException {
        for (int i = 0 ; i < 4 ; i++) {
            byte ascii;
            String readStr = "";
            while ((ascii = dataInputStream.readByte()) != 10) {
                readStr += Character.toString((char) ascii);
            }
            bounds[i] = Integer.parseInt(readStr);
        }
    }

    private void addRectAndImages() {
        EpisodeManager workingEpisodeManager = WorkSpace.mainApp.toonManager.selectedEpisodeManager();
        CutManager cutManager = workingEpisodeManager.addCut(bounds[0], bounds[1], bounds[2], bounds[3]);
        File integrateDir = new File(INTEGRATE_PATH);

        for (int i = 0 ;; i++) {
            File layer = new File (INTEGRATE_PATH + cutName + i+ ".png");
            //System.out.println(INTEGRATE_PATH + cutName + i + ".png");
            if(!layer.exists())
                break;

            cutManager.addImage(layer.getAbsolutePath());
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
}

