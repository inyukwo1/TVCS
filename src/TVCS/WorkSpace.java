package TVCS;

import GUI.Main;
import Client.ClientBase;
import TVCS.Toon.Episode;
import TVCS.Toon.Toon;
import javafx.stage.Stage;

/**
 * Created by ina on 2017-06-21.
 */
public class WorkSpace {
    public static Main mainApp = null;
    public static Stage primaryStage = null;

    public static Toon WorkingToon() {
        return mainApp.toonManager.toon;
    }

    public static Episode WorkingEpisode() {
        return mainApp.toonManager.selectedEpisodeManager().episode;
    }


}
