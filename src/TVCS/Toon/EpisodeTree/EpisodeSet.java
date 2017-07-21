package TVCS.Toon.EpisodeTree;

import TVCS.Utils.DiscreteLocation;
import javafx.scene.image.Image;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ina on 2017-07-07.
 */
public class EpisodeSet extends EpisodeVertexBase {
    private static Image DEFAULT_THUMBNAIL = new Image("Bookshelf.jpg");
    private static String DEFAULT_NAME = "Episodes";

    public ArrayList<EpisodeVertexBase> children = new ArrayList<>();
    String name;

    public EpisodeSet(String name, DiscreteLocation contentLocation) {
        super(contentLocation);
        if (name != null) {
            this.name = name;
        } else {
            this.name = DEFAULT_NAME;
        }
    }

    public void addChild(EpisodeVertexBase child) {
        children.add(child);
        child.parent = this;
    }

    public void removeChild(EpisodeVertexBase child) {
        children.remove(child);
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Image thumbnail() {
        //TODO
        return DEFAULT_THUMBNAIL;
    }

    @Override
    public boolean isSet() {
        return true;
    }
}
