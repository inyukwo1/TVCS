package GUI;


import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * Created by ina on 2017-06-20.
 */
class MenuManager {

    private MenuBar menuBar;

    public MenuManager() {
        menuBar = new MenuBar();
    }

    public void constructMenuBar() {
        Menu menuFile = new Menu("File");
        constructMenuFile(menuFile);

        menuBar.getMenus().addAll(menuFile);
    }

    private void constructMenuFile(Menu menuFile) {
        MenuItem newToon = new MenuItem("New Toon");
        MenuItem newScene = new MenuItem("New Scene");
        MenuItem loadToon = new MenuItem("Load Toon");
        MenuItem loadScene = new MenuItem("Load Scene");
        MenuItem saveToon = new MenuItem("Save Toon");
        MenuItem saveScene = new MenuItem("Save Scene");
        MenuItem pushToServer = new MenuItem("Push To Server");
        MenuItem pullFromServer = new MenuItem("Pull From Server");

        menuFile.getItems().addAll(newToon,
                newScene,
                loadToon,
                loadScene,
                saveToon,
                saveScene,
                pushToServer,
                pullFromServer);
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }
}
