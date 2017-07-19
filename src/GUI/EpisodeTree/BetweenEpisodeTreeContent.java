package GUI.EpisodeTree;

import TVCS.Toon.EpisodeTree.EpisodeTree;
import TVCS.Utils.DiscreteLocation;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Created by ina on 2017-07-18.
 */
public class BetweenEpisodeTreeContent {
    VBox invisibleContainer = new VBox();
    DiscreteLocation beforeLocation;

    public BetweenEpisodeTreeContent(DiscreteLocation location, EpisodeTreePane episodeTreePane) {
        this.beforeLocation = new DiscreteLocation(location.x, location.y);
        setSize(location.x == 0);
        setLocation(location);

        setEventHandlers(episodeTreePane);
        setParent(episodeTreePane);
    }

    private void setSize(boolean firstEntry) {
        invisibleContainer.setMaxHeight(EpisodeTreeContent.CONTENT_BLOCK_HEIGHT());
        invisibleContainer.setMinHeight(EpisodeTreeContent.CONTENT_BLOCK_HEIGHT());
        if (firstEntry) {
            invisibleContainer.setMaxWidth(EpisodeTreeContent.CONTENT_BLOCK_WIDTH() / 2);
            invisibleContainer.setMinWidth(EpisodeTreeContent.CONTENT_BLOCK_WIDTH() / 2);
        } else {
            invisibleContainer.setMaxWidth(EpisodeTreeContent.CONTENT_BLOCK_WIDTH());
            invisibleContainer.setMinWidth(EpisodeTreeContent.CONTENT_BLOCK_WIDTH());
        }
    }

    private void setLocation(DiscreteLocation location) {
        if (location.x == 0) {
            invisibleContainer.setLayoutX(0);
        } else {
            invisibleContainer.setLayoutX(
                    (-0.5 + location.x) * EpisodeTreeContent.CONTENT_BLOCK_WIDTH());
        }
        invisibleContainer.setLayoutY(location.y * EpisodeTreeContent.CONTENT_BLOCK_HEIGHT());
    }

    private void setEventHandlers(EpisodeTreePane episodeTreePane) {
      /*  invisibleContainer.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });
        invisibleContainer.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                //TODO
            }
        });
        invisibleContainer.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                //TODO
            }
        });
        invisibleContainer.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                //TODO align
                Dragboard dragboard = event.getDragboard();
                DiscreteLocation draggedLocation
                        = (DiscreteLocation) dragboard.getContent(DiscreteLocation.DISCRETE_LOCATION_DATAFORMAT);
               episodeTreePane.moveAndPushRight(draggedLocation, beforeLocation);
                episodeTreePane.contentsSetLocation();
            }
        });*/
    }

    private void setParent(EpisodeTreePane episodeTreePane) {
        episodeTreePane.pane.getChildren().add(invisibleContainer);
    }
}
