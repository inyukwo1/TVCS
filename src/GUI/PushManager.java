package GUI;

import Client.ClientBase;
import Server.ServerResourceManager;
import TVCS.Toon.Cut;
import TVCS.Toon.Episode;
import TVCS.Toon.Toon;
import TVCS.Utils.GuiUtils;
import TVCS.WorkSpace;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

/**
 * Created by ina on 2017-07-01.
 */
public class PushManager {

    Toon toon;
    Button registerToonButton = new Button("Register toon");
    Button pushAllButton = new Button("Push All Changes");
    Button  pushEpisodeButton = new Button("Push This Episode");

    public PushManager(Toon toon) {
        this.toon = toon;
        setRegisterButtonHandler();
        setPushAllButtonHandler();
        setPushEpisodeButtonHandler();
    }

    public Button getRegisterToonButton() {
        return registerToonButton;
    }

    public Button getPushAllButton() {
        return pushAllButton;
    }

    public Button getPushEpisodeButton() {
        return pushEpisodeButton;
    }

    private void setRegisterButtonHandler() {
        registerToonButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO make below separate thread and make loading gui
                if (registerToon()) {
                    GuiUtils.showAlert("Succeed", "Register succeed!");
                }
            }
        });
    }

    private void setPushAllButtonHandler() {
        pushAllButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (toon.toonId() == -1) {
                    GuiUtils.showAlert("Register Toon First", "Register Toon First");
                    return;
                }
                if(toon.hasToSave()) {
                    if(SaveLoad.askWantToSave()) {
                        SaveLoad.saveToonAction();
                    } else {
                        return;
                    }
                }
                //TODO make below separate thread and make loading gui
                if (pushAll()) {
                    GuiUtils.showAlert("Succeed", "Push succeed!");
                }
            }
        });
    }

    private void setPushEpisodeButtonHandler() {
        pushEpisodeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (toon.toonId() == -1) {
                    GuiUtils.showAlert("Register Toon First", "Register Toon First");
                    return;
                }
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
                //TODO make below separate thread and make loading gui
                if (pushEpisode(selectedEpisode)) {
                    GuiUtils.showAlert("Succeed", "Push succeed!");
                }
            }
        });
    }

    private boolean pushAll() {
        ClientBase clientBase = new ClientBase(ServerResourceManager.SERVER_IP,
                ServerResourceManager.SERVER_PORT, toon.toonId());
        if (clientBase.disabled) {
            return false;
        }
        if (!clientBase.authorizeWithGui(new GuiClientAuthorizer(clientBase))) {
            return false;
        }
        if (clientBase.disabled) {
            return false;
        }
        for (Episode episode : toon.loadedEplisodes) {
            clientBase.pushEpisode(episode);
            if (clientBase.disabled) {
                return false;
            }
        }
        clientBase.clientEnd();
        return true;
    }

    private boolean pushEpisode(Episode episode) {
        ClientBase clientBase = new ClientBase(ServerResourceManager.SERVER_IP,
                ServerResourceManager.SERVER_PORT, toon.toonId());
        if (clientBase.disabled) {
            return false;
        }
        if (!clientBase.authorizeWithGui(new GuiClientAuthorizer(clientBase))) {
            return false;
        }
        if (clientBase.disabled) {
            return false;
        }
        clientBase.pushEpisode(episode);
        clientBase.clientEnd();
        return true;
    }

    private boolean registerToon() {
        ClientBase clientBase = new ClientBase(ServerResourceManager.SERVER_IP,
                ServerResourceManager.SERVER_PORT, toon.toonId());
        if (clientBase.disabled) {
            return false;
        }
        if (!clientBase.authorizeWithGui(new GuiClientAuthorizer(clientBase))) {
            return false;
        }
        if (clientBase.disabled) {
            return false;
        }
        clientBase.allocToon(toon);
        clientBase.clientEnd();
        return true;
    }
}
