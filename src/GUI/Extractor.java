package GUI;

import TVCS.Toon.Episode;
import TVCS.WorkSpace;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by ina on 2017-09-02.
 */
public class Extractor {

    public void extract(Episode episode) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Extract");
        fileChooser.setInitialFileName(episode.name());
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(jpgFilter);
        fileChooser.getExtensionFilters().add(pdfFilter);
        File file = fileChooser.showSaveDialog(WorkSpace.primaryStage);
        if (file != null) {
            if (file.getPath().endsWith(".jpg")) {
                episode.extract(file.getAbsolutePath(), "jpg");
            } else if (file.getPath().endsWith(".png")) {
                episode.extract(file.getAbsolutePath(), "png");
            }
        }
    }
}
