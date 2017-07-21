package TVCS.Toon.EpisodeTree;

import TVCS.Utils.DiscreteLocation;
import javafx.scene.image.Image;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ina on 2017-07-13.
 */
abstract public class EpisodeVertexBase implements Serializable {
    EpisodeSet parent;
    //TODO need to set this
    DiscreteLocation locationInParent;

    public EpisodeVertexBase(DiscreteLocation contentLocation) {
        locationInParent = contentLocation;
    }

    abstract public String name();
    abstract public Image thumbnail();
    abstract public boolean isSet();
    public DiscreteLocation getLocationInParent() {
        return locationInParent;
    }
    public void removeParent() {
        parent.children.remove(this);
    }
}
