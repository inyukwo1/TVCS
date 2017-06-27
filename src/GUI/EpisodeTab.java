package GUI;

import javafx.scene.control.Tab;

/**
 * Created by ina on 2017-06-27.
 */
public class EpisodeTab extends Tab {
    EpisodeManager parentEpisodeManager;
    public EpisodeTab(EpisodeManager parentEpisodeManager) {
        this.parentEpisodeManager = parentEpisodeManager;
    }
}
