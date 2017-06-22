package GUI;


import TVCS.Toon.Toon;
import TVCS.WorkSpace;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * Created by ina on 2017-06-20.
 */
class MenuManager {
    private MenuBar menuBar;

    MenuItem newToon = new MenuItem("New Toon");
    MenuItem newScene = new MenuItem("New Scene");
    MenuItem loadToon = new MenuItem("Load Toon");
    MenuItem loadScene = new MenuItem("Load Scene");
    MenuItem saveToon = new MenuItem("Save Toon");
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
        newScene.setDisable(true);
        loadScene.setDisable(true);
        saveToon.setDisable(true);
        saveScene.setDisable(true);
        pushToServer.setDisable(true);
        menuFile.getItems().addAll(newToon,
                newScene,
                loadToon,
                loadScene,
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
                makeNewToon.start();
            }
        });
    }

    private void afterToonLoad() {
        newScene.setDisable(false);
        loadScene.setDisable(false);
        saveToon.setDisable(false);
        pushToServer.setDisable(false);
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }
}
