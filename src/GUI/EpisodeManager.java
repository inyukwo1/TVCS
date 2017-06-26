package GUI;

import TVCS.Toon.Episode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;

/**
 * Created by ina on 2017-06-26.
 */
public class EpisodeManager {
    Episode episode;
    Tab tab = new Tab();
    StackPane pane = new StackPane();

    public EpisodeManager(Episode episode) {
        this.episode = episode;
    }

    public void start(TabPane tabPane) {
        tab.setText(episode.name());
        tab.setContent(pane);
        tabPane.getTabs().add(tab);
    }
}
