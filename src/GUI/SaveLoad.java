package GUI;

import TVCS.Toon.Episode;
import TVCS.WorkSpace;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Optional;

/**
 * Created by ina on 2017-07-01.
 */
public class SaveLoad {

    public static void saveToonAction() {
        if(WorkSpace.mainApp.toonManager.hasPath()) {
            WorkSpace.mainApp.toonManager.saveToon();
        } else {
            SaveLoad.saveToonAsAction();
        }
    }

    public static void saveToonAsAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Toon");
        File file = fileChooser.showSaveDialog(WorkSpace.primaryStage);
        if (file != null) {
            if (WorkSpace.mainApp.toonManager.saveToonAs(file.getAbsolutePath()) == false) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Save Failed");
                alert.setContentText("Save Failed");
                alert.showAndWait();
            }
        }
    }

    public static void saveEpisode(Episode episode) {
        episode.Save();
    }

    public static boolean askWantToSave() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Have to Save First");
        alert.setHeaderText("You have to save first.");
        alert.setContentText("Do you want to save?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public static void loadToonAsAction() {
        WorkSpace.mainApp.stopToon();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Toon");
        File toonInfoFile = fileChooser.showOpenDialog(WorkSpace.primaryStage);
        String toonDirPath = toonInfoFile.getParent();
        if (!WorkSpace.mainApp.loadToon(toonDirPath)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Load Failed");
            alert.setContentText("Load Failed");
            alert.showAndWait();
        }
    }
}
