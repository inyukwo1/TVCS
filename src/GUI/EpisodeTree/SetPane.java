package GUI.EpisodeTree;

import TVCS.Utils.DiscreteLocation;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by ina on 2017-07-21.
 */
public class SetPane extends EpisodeTreePane{

    public void setPaneSize() {
        pane.setMaxWidth(paneWidth() * EpisodeTreeContent.CONTENT_BLOCK_WIDTH());
        pane.setMinWidth(paneWidth() * EpisodeTreeContent.CONTENT_BLOCK_WIDTH());
        pane.setMaxHeight(paneHeight() * EpisodeTreeContent.CONTENT_BLOCK_HEIGHT());
        pane.setMinHeight(paneHeight() * EpisodeTreeContent.CONTENT_BLOCK_HEIGHT());
    }

    public void addContent(EpisodeTreeContent content) {
        while (episodeTreeContents.size() <= content.location().y) {
            episodeTreeContents.add(new LinkedList<>());
        }
        setNextLocation(content.location());
        if (episodeTreeContents.get(content.location().y).size() < content.location().x) {
            episodeTreeContents.get(content.location().y).add(content);
            content.location().x = episodeTreeContents.get(content.location().y).indexOf(content);
        } else {
            episodeTreeContents.get(content.location().y).add(content.location().x, content);
        }
        pane.getChildren().add(content.container);
        content.parentTreePane = this;
        content.setLocation();
    }

    public int paneWidth() {
        //TODO redesign
        return 2;
    }

    public int paneHeight() {
        //TODO redesign
        return 2;
    }

    private void setNextLocation(DiscreteLocation location) {
        //TODO 삐져나가는거 처리
        location.y = 0;
        location.x = episodeTreeContents.get(0).size();
    }

}
