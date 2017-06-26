package GUI;

import TVCS.Toon.Toon;
import TVCS.WorkSpace;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootPane;
    private MenuManager menuManager;
    public ToonManager toonManager = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        WorkSpace.mainApp = this;
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

    public void makeNewEpisode(String episodeName) {
        toonManager.makeNewEpisode(episodeName);
    }

    public static void main(String[] args) {
        launch(args);
    }


}
