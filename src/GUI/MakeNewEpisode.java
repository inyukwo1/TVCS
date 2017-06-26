package GUI;

import TVCS.Toon.Episode;
import TVCS.Toon.Toon;
import TVCS.WorkSpace;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Created by ina on 2017-06-26.
 */
public class MakeNewEpisode extends AskWithGridPane {
    TextField askNameTextField;

    @Override
    protected void setStageTitle() {
        stage.setTitle("Make New Episode");
    }

    @Override
    protected void fillGridPane() {
        addAskingToonName();
        addOkButton();
    }

    private void addAskingToonName() {
        Label askName = new Label("Episode Name: ");
        pane.add(askName, 0, 0);
        askNameTextField = new TextField();
        pane.add(askNameTextField, 1, 0);
    }

    private void addOkButton() {
        Button okButton = new Button("OK");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                WorkSpace.mainApp.makeNewEpisode(askNameTextField.getText());
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
