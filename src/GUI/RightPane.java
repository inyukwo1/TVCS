package GUI;

import TVCS.Toon.Episode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Created by ina on 2017-07-29.
 */
public class RightPane {
    //TODO 예쁘게 디자인
    private static int WIDTH = 250;

    VBox container = new VBox();
    GridPane toonRelatedPane = new GridPane();
    GridPane episodeRelatedPane = new GridPane();
    GridPane cutRelatedPane = new GridPane();

    Slider widthSlider;
    Slider heightSlider;

    public RightPane() {
        setupContainer();
        setupToonRelatedPane();
        setupEpisodeRelatedPane();
        setupCutRelatedPane();
    }

    private void setupContainer() {
        container.getChildren().add(toonRelatedPane);
        container.getChildren().add(episodeRelatedPane);
        container.getChildren().add(cutRelatedPane);
        container.setMaxWidth(WIDTH);
        container.setMinWidth(WIDTH);
        container.setBorder(new Border(new BorderStroke(Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY,
                BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, BorderWidths.DEFAULT, Insets.EMPTY)));
        container.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void setupToonRelatedPane() {
        toonRelatedPane.setBorder(new Border(new BorderStroke(Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY,
                BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                CornerRadii.EMPTY, BorderWidths.DEFAULT, Insets.EMPTY)));
        toonRelatedPane.setPadding(new Insets(10));
        toonRelatedPane.setAlignment(Pos.CENTER);
    }

    private void setupEpisodeRelatedPane() {
        episodeRelatedPane.setBorder(new Border(new BorderStroke(Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY,
                BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                CornerRadii.EMPTY, BorderWidths.DEFAULT, Insets.EMPTY)));
        episodeRelatedPane.setPadding(new Insets(10));
        episodeRelatedPane.setAlignment(Pos.CENTER);
    }

    private void setupCutRelatedPane() {
        cutRelatedPane.setBorder(new Border(new BorderStroke(Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY,
                BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                CornerRadii.EMPTY, BorderWidths.DEFAULT, Insets.EMPTY)));
        cutRelatedPane.setPadding(new Insets(10));
        cutRelatedPane.setAlignment(Pos.CENTER);
    }

    public void fillToonRelatedPane(Button episodeTreeShow, Button registerToon,
                                   Button pushAll, Button makeNewEpisode) {
        toonRelatedPane.add(episodeTreeShow, 0, 0);
        toonRelatedPane.add(registerToon, 0, 1);
        toonRelatedPane.add(pushAll, 0, 2);
        toonRelatedPane.add(makeNewEpisode, 0, 3);
    }

    public void setWidthSlider(Slider widthSlider) {
        this.widthSlider = widthSlider;
    }

    public void setHeightSlider(Slider heightSlider) {
        this.heightSlider = heightSlider;
    }

    public void syncEpisode(Episode episode) {
        widthSlider.setValue(episode.getWidth());
        heightSlider.setValue(episode.getHeight());
    }

    public void fillEpisodeRelatedPane(Button pushEpisode, Button addNewCut, Button extractPdf,
                                       GridPane backgroudColorController, GridPane episodeSizeController,
                                       GridPane gridSizeController) {
        episodeRelatedPane.add(pushEpisode, 0, 0);
        episodeRelatedPane.add(addNewCut, 0, 1);
        episodeRelatedPane.add(extractPdf, 0, 2);
        episodeRelatedPane.add(backgroudColorController, 0, 3);
        episodeRelatedPane.add(episodeSizeController, 0, 4);
        episodeRelatedPane.add(gridSizeController, 0, 5);
    }

    public void fillCutRelatedPane(Button preserveRatioButton) {
        cutRelatedPane.add(preserveRatioButton, 0, 0);
    }

    public void clearCutRelatedPane() {
        cutRelatedPane.getChildren().clear();
    }
}

