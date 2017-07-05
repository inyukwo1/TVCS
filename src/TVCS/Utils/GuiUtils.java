package TVCS.Utils;

import javafx.scene.control.Alert;

/**
 * Created by ina on 2017-07-05.
 */
public class GuiUtils {
    public static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
