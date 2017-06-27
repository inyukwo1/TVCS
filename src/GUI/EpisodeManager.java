package GUI;

import TVCS.Toon.Episode;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;

/**
 * Created by ina on 2017-06-26.
 */
public class EpisodeManager {
    Episode episode;
    EpisodeTab tab;
    ScrollPane pane = new ScrollPane();
    StackPane workPane = new StackPane();

    public EpisodeManager(Episode episode) {
        this.episode = episode;
        tab = new EpisodeTab(this);
    }

    public void start(TabPane tabPane) {
        constructTab();
        tabPane.getTabs().add(tab);
    }

    public void startAddCutMode() {

    }
    private void constructTab() {
        HBox hboxForWorkPane = new HBox();
        hboxForWorkPane.getChildren().add(workPane);
        hboxForWorkPane.setAlignment(Pos.CENTER);
        hboxForWorkPane.setPadding(new Insets(50));
        hboxForWorkPane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        hboxForWorkPane.prefWidthProperty().bind(pane.widthProperty());
        workPane.setMaxSize(episode.getWidth(), episode.getHeight());
        workPane.setMinSize(episode.getWidth(), episode.getHeight());
        workPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setContent(hboxForWorkPane);
        tab.setContent(pane);
        tab.setText(episode.name());
    }


}
