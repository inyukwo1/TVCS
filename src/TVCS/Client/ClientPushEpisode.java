package TVCS.Client;

import TVCS.Toon.Cut;
import TVCS.Toon.CutImage;
import TVCS.Toon.Episode;
import TVCS.Utils.FileManager;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by ina on 2017-06-15.
 */
public class ClientPushEpisode extends ClientBase{
    int clientId;
    Episode episode;

    public ClientPushEpisode(String ip, int port, int clientId, Episode episode) {
        super(ip, port);
        this.clientId = clientId;
        this.episode = episode;
    }

    @Override
    public void run() throws IOException {
        SendCommunicationType();
        System.out.println("Communication type push sent");
        PushEpisode();
        System.out.println("Episode pushed");
    }

    private void SendCommunicationType() throws IOException {
        data_output_stream.writeUTF("PUSH");
        data_output_stream.writeInt(clientId);
        data_output_stream.writeUTF("episode");
    }

    private void PushEpisode() throws IOException {
        data_output_stream.writeUTF(episode.name());
        PushEpisodeInfo();
        data_output_stream.writeInt(episode.numCuts());
        for(Cut cut : episode.cuts) {
            PushCut(cut);
        }
    }

    private void PushEpisodeInfo() throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(data_output_stream);
        objectOutputStream.writeObject(episode);
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
        FileManager.smallFilePush(image.cutImagePath(), output_stream);
    }
}
