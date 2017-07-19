package TVCS.Toon.EpisodeTree;

import TVCS.Toon.Episode;
import TVCS.Utils.DiscreteLocation;
import TVCS.Utils.FileManager;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ina on 2017-07-07.
 */
public class EpisodeTree implements Serializable {
    public static final String FILE_NAME = "EpisodeTree";
    EpisodeSet root;

    public EpisodeTree() {
        root = new EpisodeSet("ROOT", null);
    }

    public EpisodeVertex addNewVertex(Episode newEpisode, DiscreteLocation nextContentLocation) {
        EpisodeVertex newVertex = new EpisodeVertex(newEpisode, nextContentLocation);
        root.addChild(newVertex);
        return newVertex;
    }

    public void save(String toonPath) {
        FileManager.SaveSerializableObject(this, toonPath + File.separator + FILE_NAME);
    }

    static public EpisodeTree load(String toonPath) {
        EpisodeTree episodeTree
                = (EpisodeTree) FileManager.LoadSerializableObject(toonPath + File.separator + FILE_NAME);
        return episodeTree;
    }

    public ArrayList<EpisodeVertexBase> firstLevelVertices() {
        return root.children;
    }
}
