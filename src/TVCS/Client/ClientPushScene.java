package TVCS.Client;

import TVCS.Toon.Cut;
import TVCS.Toon.CutImage;
import TVCS.Toon.Toon;
import TVCS.Toon.ToonScene;
import TVCS.Utils.FileManager;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by ina on 2017-06-15.
 */
public class ClientPushScene extends ClientBase{
    int clientId;
    ToonScene scene;

    public ClientPushScene(String ip, int port, int clientId, ToonScene scene) {
        super(ip, port);
        this.clientId = clientId;
        this.scene = scene;
    }

    @Override
    public void run() throws IOException {
        SendCommunicationType();
        System.out.println("Communication type push sent");
        PushScene();
        System.out.println("Scene pushed");
    }

    private void SendCommunicationType() throws IOException {
        data_output_stream.writeUTF("PUSH");
        data_output_stream.writeInt(clientId);
        data_output_stream.writeUTF("scene");
    }

    private void PushScene() throws IOException {
        data_output_stream.writeUTF(scene.name());
        PushSceneInfo();
        data_output_stream.writeInt(scene.numCuts());
        for(Cut cut : scene.cuts) {
            PushCut(cut);
        }
    }

    private void PushSceneInfo() throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(data_output_stream);
        objectOutputStream.writeObject(scene);
        objectOutputStream.flush();
    }

    private void PushCut(Cut cut) throws IOException {
        data_output_stream.writeLong(cut.id());
        data_output_stream.writeInt(cut.imageNum());
        for(CutImage image : cut.images) {
            PushImage(image);
        }
    }

    private void PushImage(CutImage image) throws IOException {
        data_output_stream.writeLong(image.id());
        System.out.println("Image id pushed");
        FileManager.smallFilePush(image.path, output_stream);
    }
}
