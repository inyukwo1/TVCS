package GUI;


import TVCS.WorkSpace;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by ina on 2017-06-20.
 */
class MenuManager {
    private MenuBar menuBar;

    MenuItem newToon = new MenuItem("New Toon");
    MenuItem newScene = new MenuItem("New Scene");
    MenuItem loadToon = new MenuItem("Load");
    MenuItem saveToonAs = new MenuItem("Save As");
    MenuItem saveToon = new MenuItem("Save All");
    MenuItem saveScene = new MenuItem("Save Scene");
    MenuItem pushToServer = new MenuItem("Push To Server");
    MenuItem pullFromServer = new MenuItem("Pull From Server");

    public MenuManager() {
        menuBar = new MenuBar();
    }

    public void constructMenuBar() {
        Menu menuFile = new Menu("File");
        constructMenuFile(menuFile);

        menuBar.getMenus().addAll(menuFile);
    }

    private void constructMenuFile(Menu menuFile) {
        newToonSetOnAction();
        loadToonSetOnAction();
        saveToonAsSetOnAction();
        saveToonSetOnAction();
        newScene.setDisable(true);
        saveToonAs.setDisable(true);
        saveToon.setDisable(true);
        saveScene.setDisable(true);
        pushToServer.setDisable(true);
        menuFile.getItems().addAll(newToon,
                newScene,
                loadToon,
                saveToonAs,
                saveToon,
                saveScene,
                pushToServer,
                pullFromServer);
    }

    private void newToonSetOnAction() {
        newToon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MakeNewToon makeNewToon = new MakeNewToon();
                if(makeNewToon.start() == true) {
                    afterToonLoad();
                }
            }
        });
    }

    private void loadToonSetOnAction() {
        loadToon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               SaveLoad.loadToonAsAction();
            }
        });
    }

    private void saveToonAsSetOnAction() {
        saveToonAs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SaveLoad.saveToonAsAction();
            }
        });
    }

    private void saveToonSetOnAction() {
        saveToon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SaveLoad.saveToonAction();
            }
        });
    }

    private void afterToonLoad() {
        newScene.setDisable(false);
        saveToonAs.setDisable(false);
        saveToon.setDisable(false);
        pushToServer.setDisable(false);
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }
}
