package GUI;

import TVCS.WorkSpace;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootPane;
    private MenuManager menuManager;
    public BranchManager branchManager = new BranchManager();

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

    public static void main(String[] args) {
        launch(args);
    }


}
