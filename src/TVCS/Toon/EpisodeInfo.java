package TVCS.Toon;

import TVCS.Toon.Branch.Branch;
import javafx.scene.image.Image;
import javafx.util.Pair;

/**
 * Created by ina on 2017-06-07.
 */
public class EpisodeInfo extends ObjectMetaInfo {
    public String name;
    public long id;
    public int width, height;

    public Image thumbnail; //TODO 저장, 로드, push, pull 등
    public Pair<Integer, Integer> episodeTreePaneLocation;

    public EpisodeInfo(String name, long id, int width, int height) {
        this.name = name;
        this.id = id;
        this.width = width;
        this.height = height;
    }
}
