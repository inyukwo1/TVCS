package GUI;

import javafx.scene.control.Alert;

/**
 * Created by ina on 2017-07-01.
 */
public class ClientAuthorizer {

    public boolean authorize() {


        authorizeFailed();
        return true;
    }

    private void authorizeFailed() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Load Failed");
        alert.setContentText("Load Failed");
        alert.showAndWait();
    }
}
