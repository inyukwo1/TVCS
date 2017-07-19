package TVCS.Toon.EpisodeTree;

import TVCS.Toon.Episode;
import TVCS.Toon.EpisodeInfo;
import TVCS.Utils.DiscreteLocation;
import javafx.scene.image.Image;

/**
 * Created by ina on 2017-07-07.
 */
public class EpisodeVertex extends EpisodeVertexBase {
    private static Image DEFAULT_THUMBNAIL = new Image("DefaultThumbnail.png");

    public EpisodeInfo episodeInfo;

    public EpisodeVertex(Episode episode, DiscreteLocation contentLocation) {
        super(contentLocation);
        this.episodeInfo = episode.episodeInfo;
    }

    @Override
    public String name() {
        return episodeInfo.name;
    }

    @Override
    public Image thumbnail() {
        if (episodeInfo.thumbnail == null) {
            return DEFAULT_THUMBNAIL;
        }
        return episodeInfo.thumbnail;
    }

}
