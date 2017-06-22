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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by ina on 2017-06-21.
 */
public class MakeNewToon {
    Stage stage;
    TextField askNameTextField;

    public void start() {
        stage = new Stage();
        stage.setTitle("Make New Toon");
        GridPane pane = makeGridPane();
        Scene scene = new Scene(pane, 300, 200);

        addAskingToonName(pane);

        addOkButton(pane);

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    private GridPane makeGridPane() {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(25,25,25,25));
        return pane;
    }

    private void addAskingToonName(GridPane pane) {
        Label askName = new Label("Toon Name: ");
        pane.add(askName, 0, 0);
        askNameTextField = new TextField();
        pane.add(askNameTextField, 1, 0);
    }

    private void addOkButton(GridPane pane) {
        Button okButton = new Button("OK");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                WorkSpace.WorkingToon = new Toon(askNameTextField.getText());
                stage.close();
            }
        });
        HBox hBoxOkButton= new HBox(10);
        hBoxOkButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxOkButton.getChildren().add(okButton);
        pane.add(hBoxOkButton, 1, 4);
    }

}
