package GUI;

import TVCS.Toon.Episode;
import TVCS.Toon.Toon;
import TVCS.WorkSpace;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import java.util.Optional;

/**
 * Created by ina on 2017-07-01.
 */
public class PushManager {
    public static final String ip = "120.0.0.1";
    public static final int port = 3000;

    Toon toon;
    Button pushAllButton = new Button("Push All Changes");
    Button  pushEpisodeButton = new Button("Push This Episode");

    public PushManager(Toon toon) {
        this.toon = toon;
        setPushAllButtonHandler();
        setPushEpisodeButtonHandler();
    }

    public Button getPushAllButton() {
        return pushAllButton;
    }

    public Button getPushEpisodeButton() {
        return pushEpisodeButton;
    }

    private void setPushAllButtonHandler() {
        pushAllButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(toon.hasToSave()) {
                    if(SaveLoad.askWantToSave()) {
                        SaveLoad.saveToonAction();
                    } else {
                        return;
                    }
                }
                ClientAuthorizer clientAuthorizer = new ClientAuthorizer();
                if(clientAuthorizer.authorize()) {
                    toon.pushAll(ip, port);
                }
            }
        });
    }

    private void setPushEpisodeButtonHandler() {
        pushEpisodeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToonManager workingToonManager = WorkSpace.mainApp.toonManager;
                Episode selectedEpisode = workingToonManager.selectedEpisodeManager().episode;
                if (selectedEpisode.hasToSave()) {
                    if (SaveLoad.askWantToSave()){
                        if (!toon.hasPath()) {
                            SaveLoad.saveToonAction();
                        }
                        SaveLoad.saveEpisode(selectedEpisode);
                    } else {
                        return;
                    }
                }
                ClientAuthorizer clientAuthorizer = new ClientAuthorizer();
                if (clientAuthorizer.authorize()) {
                    toon.pushEpisode(ip, port, selectedEpisode);
                }
            }
        });
    }
}
