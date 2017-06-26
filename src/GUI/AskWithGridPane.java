package GUI;

import TVCS.Toon.Toon;
import TVCS.WorkSpace;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by ina on 2017-06-26.
 */
public abstract class AskWithGridPane {
    protected Stage stage;
    protected GridPane pane;
    protected  boolean succeed = false;




    protected abstract void setStageTitle();

    protected abstract void fillGridPane();

    public boolean start() {

        stage = new Stage();
        setStageTitle();

        pane = makeGridPane();
        Scene scene = new Scene(pane, 300, 200);

        fillGridPane();

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        return succeed;
    }

    private GridPane makeGridPane() {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(25,25,25,25));
        return pane;
    }
}
