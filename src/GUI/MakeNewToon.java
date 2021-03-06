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
public class MakeNewToon extends AskWithGridPane{
    TextField askNameTextField;

    @Override
    protected void setStageTitle() {
        stage.setTitle("Make New Toon");
    }

    @Override
    protected void fillGridPane() {
        addAskingToonName();
        addOkButton();
    }

    private void addAskingToonName() {
        Label askName = new Label("Toon Name: ");
        pane.add(askName, 0, 0);
        askNameTextField = new TextField();
        pane.add(askNameTextField, 1, 0);
    }

    private void addOkButton() {
        Button okButton = new Button("OK");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                WorkSpace.mainApp.stopToon();
                WorkSpace.mainApp.initToon(new Toon(askNameTextField.getText()));
                succeed = true;
                stage.close();
            }
        });
        HBox hBoxOkButton= new HBox(10);
        hBoxOkButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxOkButton.getChildren().add(okButton);
        pane.add(hBoxOkButton, 1, 4);
    }
}
