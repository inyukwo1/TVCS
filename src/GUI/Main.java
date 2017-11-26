package GUI;

import TVCS.Toon.Toon;
import TVCS.WorkSpace;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.opencv.core.Core;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootPane;
    private MenuManager menuManager;
    public ToonManager toonManager = null;
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    @Override
    public void start(Stage primaryStage) throws Exception{
        WorkSpace.mainApp = this;
        WorkSpace.primaryStage = primaryStage;
        this.primaryStage = primaryStage;
        primaryStage.setTitle("ToonVCS");
        initRootLayout();
        primaryStage.show();
    }

    public void initRootLayout() {
        initRootScene();
        initMenu();
    }

    public void initRootScene() {
        rootPane = new BorderPane();
        primaryStage.setScene(new Scene(rootPane, 1000, 800));
    }

    public void initMenu() {
        menuManager = new MenuManager();
        menuManager.constructMenuBar();
        rootPane.setTop(menuManager.getMenuBar());
    }

    public void stopToon() {
        if(toonManager != null) {
            toonManager.stop(rootPane);
        }
    }

    public void initToon(Toon toon) {
        toonManager = new ToonManager(toon);
        toonManager.start(rootPane);
    }

    public boolean loadToon(String toonPath) {
        Toon toon = new Toon();
        if (toon.LoadToon(toonPath) == false) {
            return false;
        }
        toonManager = new ToonManager(toon);
        toonManager.start(rootPane);
        return true;
    }

    public void makeNewEpisode(String episodeName) {
        toonManager.makeNewEpisode(episodeName);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
