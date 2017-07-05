package GUI;

import Client.ClientBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

/**
 * Created by ina on 2017-07-01.
 */
public class GuiClientAuthorizer extends AskWithGridPane{
    TextField idField = new TextField();
    PasswordField passwordField = new PasswordField();
    ClientBase clientBase;

    public GuiClientAuthorizer(ClientBase clientBase) {
        this.clientBase = clientBase;
    }

    protected void setStageTitle() {
        stage.setTitle("Log In");
    }

    protected void fillGridPane() {
        idField.setPromptText("Username");
        passwordField.setPromptText("Password");

        pane.add(new Label("ID:"), 0, 0);
        pane.add(idField, 1, 0);
        pane.add(new Label("Password:"), 0, 1);
        pane.add(passwordField, 1, 1);

        addLoginButton();
        addCancelButton();
    }

    private void addLoginButton() {
        Button loginButton = new Button("Login");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                succeed = clientBase.authorize(idField.getText(), passwordField.getText());
                if(succeed == false) {
                    authorizeFailed();
                }
                stage.close();
            }
        });
        HBox hBoxOkButton= new HBox(10);
        hBoxOkButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxOkButton.getChildren().add(loginButton);
        pane.add(hBoxOkButton, 1, 3);
    }

    private void addCancelButton() {
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                succeed = false;
                stage.close();
            }
        });
        HBox hBoxOkButton= new HBox(10);
        hBoxOkButton.setAlignment(Pos.BOTTOM_RIGHT);
        hBoxOkButton.getChildren().add(cancelButton);
        pane.add(hBoxOkButton, 1, 4);
    }

    private void authorizeFailed() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Authorize Failed");
        alert.setContentText("Authorize Failed");
        alert.showAndWait();
    }
}
