package Server;

import TVCS.Toon.Episode;
import TVCS.Utils.FileManager;

import java.io.File;
import java.io.InputStream;

/**
 * Created by ina on 2017-06-12.
 */
public class ServerToonManager {

    public long toonId;
    String episodePath;
    String cutPath;

    public ServerToonManager() {
    }

    public void readyToSaveEpisode(Episode episode) {
        this.episodePath = toonPath() + File.separator + episode.name();
        if (!FileManager.PathExists(toonPath())) {
            FileManager.MakeDirectory(toonPath());
        }
        if (!FileManager.PathExists(episodePath)) {
            FileManager.MakeDirectory(episodePath);
        }
    }

    public void saveEpisodeInfo(Episode episode) {
        String episodeInfoPath = this.episodePath + File.separator + episode.name();
        FileManager.SaveSerializableObject(episode, episodeInfoPath);
    }

    public void readyToSaveCut(long cutId) {
        this.cutPath = episodePath + File.separator + cutId;
        if (!FileManager.PathExists(cutPath)) {
            FileManager.MakeDirectory(cutPath);
        }
    }

    public void pullAndSaveImage(long imageId, InputStream inputStream) {
        String imagePath = cutPath + File.separator + imageId + ".png";
        FileManager.smallFilePull(imagePath, inputStream);
    }

    private String toonPath() {
        return  ServerResourceManager.HOME_PATH + File.separator + toonId;
    }
}
