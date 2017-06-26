package TVCS.Toon;

import java.io.*;

/**
 * Created by ina on 2017-06-07.
 */
public class EpisodeInfo extends ObjectMetaInfo{
    public transient BranchVertex branchVertex;
    public String name;
    public long id;
    public int width, height;
    public EpisodeInfo(String name, long id, int width, int height) {
        this.name = name;
        this.id = id;
        this.width = width;
        this.height = height;
    }
}