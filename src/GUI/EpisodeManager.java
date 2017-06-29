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
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ina on 2017-06-26.
 */
public class EpisodeManager {
    Episode episode;
    EpisodeTab tab;
    ScrollPane pane = new ScrollPane();
    Pane workPane = new Pane();
    ArrayList<CutManager> cutManagers = new ArrayList<>();

    boolean addingCut = false;
    EventHandler<MouseEvent> pressHandler;
    EventHandler<MouseEvent> dragHandler;
    EventHandler<MouseEvent> releaseHandler;



    public EpisodeManager(Episode episode) {
        this.episode = episode;
        tab = new EpisodeTab(this);
    }

    public void start(TabPane tabPane) {
        constructTab();
        tabPane.getTabs().add(tab);
    }

    public void startAddCutMode() {
        if(addingCut) {
            return;
        }
        addingCut = true;

        dragHandler = makeDragHandler();
        pressHandler = makePressEventHandler();
        releaseHandler = makeReleaseHandler();
        workPane.addEventHandler(MouseEvent.MOUSE_PRESSED, pressHandler);
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

    private EventHandler<MouseEvent> makePressEventHandler() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cutManagers.add(new CutManager(
                        episode.AddNewCut(event.getX(), event.getY(), 0, 0), workPane));
                workPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, dragHandler);
                workPane.addEventHandler(MouseEvent.MOUSE_RELEASED, releaseHandler);
            }
        };
    }

    private EventHandler<MouseEvent> makeDragHandler() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                CutManager cutManager = cutManagers.get(cutManagers.size() - 1);
                cutManager.moveEndPoint(event.getX(), event.getY());
            }
        };
    }

    private EventHandler<MouseEvent> makeReleaseHandler() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                CutManager cutManager = cutManagers.get(cutManagers.size() - 1);
                cutManager.setUp();
                addingCut = false;
                removeHandlers();
            }
        };
    }

    private void removeHandlers() {
        workPane.removeEventHandler(MouseEvent.MOUSE_PRESSED, pressHandler);
        workPane.removeEventHandler(MouseEvent.MOUSE_DRAGGED, dragHandler);
        workPane.removeEventHandler(MouseEvent.MOUSE_RELEASED, releaseHandler);
    }
}
