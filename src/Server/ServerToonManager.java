package Server;

import TVCS.Toon.*;
import TVCS.Utils.FileManager;

import java.io.*;
import java.math.BigInteger;

/**
 * Created by ina on 2017-06-12.
 */
public class ServerToonManager {

    public long toonId;
    String episodePath;
    String cutPath;

    Episode episode;

    public ServerToonManager() {
    }

    public void saveToonInfo(ToonInfo toonInfo) {
        if (!FileManager.PathExists(toonPath())) {
            FileManager.MakeDirectory(toonPath());
        }
        toonInfo.Save(toonPath() + File.separator + "tooninfo");
    }
    public void readyToSaveEpisode(Episode episode) {
        this.episodePath = toonPath() + File.separator + episode.name();

        if (!FileManager.PathExists(episodePath)) {
            FileManager.MakeDirectory(episodePath);
        }
    }

    public void saveEpisodeInfo(Episode episode) {
        System.out.println("Saving episode, cuts: " + episode.cuts.size());
        String episodeInfoPath = this.episodePath + File.separator + episode.name();
        FileManager.SaveSerializableObject(episode, episodeInfoPath);
    }

    public void readyToSaveCut(BigInteger cutId) {
        this.cutPath = episodePath + File.separator + cutId.toString();
        if (!FileManager.PathExists(cutPath)) {
            FileManager.MakeDirectory(cutPath);
        }
    }

    public void pullAndSaveImage(BigInteger imageId, InputStream inputStream) {
        String imagePath = cutPath + File.separator + imageId.toString() + ".png";
        FileManager.smallFilePull(imagePath, inputStream);
    }

    public boolean findEpisode(Episode episode) {
        this.episodePath = toonPath() + File.separator + episode.name();
        if (!FileManager.PathExists(episodePath)) {
            return false;
        }
        Toon toon = new Toon();
        toon.LoadToon(toonPath());
        this.episode = Episode.load(toon, episode.episodeInfo);
        return true;
    }


    public void pushCuts(ObjectInputStream objectInputStream, OutputStream outputStream, ObjectOutputStream objectOutputStream) throws IOException {
        System.out.println("Have to send " + episode.numCuts() + " cuts");
        objectOutputStream.writeInt(episode.numCuts());
        objectOutputStream.flush();
        for (Cut cut: episode.cuts) {
            objectOutputStream.writeObject(cut);
            objectOutputStream.flush();
            for (CutImage image : cut.images) {
                FileManager.smallFilePush(toonPath() + File.separator + episode.name() +
                        File.separator + cut.id() + File.separator + File.separator + image.id() + ".png", outputStream);
                System.out.println("for check: " + objectInputStream.readInt());
                objectOutputStream.writeInt(0);
                objectOutputStream.flush();
            }
        }
    }

    private String toonPath() {
        return  ServerResourceManager.HOME_PATH + File.separator + toonId;
    }
}
