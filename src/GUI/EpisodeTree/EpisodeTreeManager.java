package GUI.EpisodeTree;

import GUI.EpisodeTree.EpisodeTreePane;
import TVCS.Toon.EpisodeTree.EpisodeTree;
import TVCS.Utils.DiscreteLocation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Created by ina on 2017-06-21.
 */
public class EpisodeTreeManager {
    EpisodeTree episodeTree;
    Button showButton;
    ScrollPane scrollPane = new ScrollPane();
    public EpisodeTreePane episodeTreePane = new EpisodeTreePane();

    boolean showing = false;

    int paneHeight = 300;

    public EpisodeTreeManager(EpisodeTree episodeTree, BorderPane centerPane) {
        this.episodeTree = episodeTree;
        initPanes(centerPane);
    }

    public Button makeShowButton() {
        showButton = new Button("Show Episodes");
        showButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (showing) {
                    showButton.setText("Show Episodes");
                    showing = false;
                    hidePane();

                } else {
                    showButton.setText("Hide Episodes");
                    showing = true;
                    showPane();

                }
            }
        });
        return showButton;
    }

    public DiscreteLocation nextContentLocation() {
        return episodeTreePane.nextContentLocation();
    }

    private void hidePane() {
        scrollPane.setMaxHeight(0);
        scrollPane.setMinHeight(0);
    }

    private void showPane() {
        scrollPane.setMinHeight(paneHeight);
        scrollPane.setMaxHeight(paneHeight);
    }

    private void initPanes(BorderPane centerPane) {
        scrollPane.setBorder(new Border(new BorderStroke(Color.GRAY,  BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(3), Insets.EMPTY)));
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        episodeTreePane.setEpisodeTree(episodeTree);
        episodeTreePane.showPane();
        scrollPane.setContent(episodeTreePane.pane);
        centerPane.setBottom(scrollPane);

        hidePane();
    }
}
