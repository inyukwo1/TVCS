package GUI.EpisodeTree;

import TVCS.Utils.DiscreteLocation;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by ina on 2017-07-21.
 */
public class SetPane {
    ArrayList<LinkedList<EpisodeTreeContent>> episodeTreeContents = new ArrayList<>();

    public void addContent(EpisodeTreeContent content) {
        while (episodeTreeContents.size() <= content.location().y) {
            episodeTreeContents.add(new LinkedList<>());
        }
        setNextLocation(content.location());
        episodeTreeContents.get(content.location().y).add(content.location().x, content);
    }

    private void setNextLocation(DiscreteLocation location) {
        //TODO 삐져나가는거 처리
        location.y = 0;
        location.x = episodeTreeContents.get(0).size();
    }
}
