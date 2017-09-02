package GUI;

import TVCS.Toon.Cut;
import TVCS.Toon.Episode;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by ina on 2017-06-26.
 */
public class EpisodeManager {
    ToonManager parentToonManager;

    Episode episode;
    EpisodeTab tab;
    ScrollPane pane = new ScrollPane();
    Pane workPane = new Pane();
    ArrayList<CutManager> cutManagers = new ArrayList<>();

    boolean addingCut = false;
    CutManager selectedCut = null; // if null, nothing selected

    //drag related events
    EventHandler<MouseEvent> pressHandler;
    EventHandler<MouseEvent> dragHandler;
    EventHandler<MouseEvent> releaseHandler;

    EventHandler<MouseEvent> unselectCutHandeler = makeUnselectCutHandler();

    Extractor extractor = new Extractor();

    public EpisodeManager(Episode episode, ToonManager parentToonManager) {
        this.episode = episode;
        this.parentToonManager = parentToonManager;
        loadCut();
        tab = new EpisodeTab(this);
    }

    private void loadCut() {
        for (Cut cut: episode.cuts) {
            CutManager newCutManager = new CutManager(cut, this);
            cutManagers.add(newCutManager);
            newCutManager.setUp();
        }
    }

    public void start(TabPane tabPane) {
        constructTab();
        tabPane.getTabs().add(tab);
    }

    //TODO 중심을 기준으로 resize
    public void resizeWidth(int width) {
        workPane.setMinWidth(width);
        workPane.setMaxWidth(width);
        episode.resizeWidth(width);
    }

    public void resizeHeight(int height) {
        workPane.setMinHeight(height);
        workPane.setMaxHeight(height);
        episode.resizeHeight(height);
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

    public void selectCut(CutManager cutManager) {
        selectedCut = cutManager;
        selectedCut.select();
        workPane.addEventHandler(MouseEvent.MOUSE_CLICKED, unselectCutHandeler);
        parentToonManager.rightPane.fillCutRelatedPane(cutManager.makePreserveRatioButton());
    }

    public void unselect() {
        if (selectedCut == null) {
            return;
        }
        workPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, unselectCutHandeler);
        selectedCut.unselect();
        parentToonManager.rightPane.clearCutRelatedPane();
    }

    public void extract() {
        extractor.extract(this.episode);
    }

    public void setBackgroundColor(Color color) {
        episode.setBackgroundColor(color);
        workPane.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
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
        EpisodeManager thisEpisodeManager = this;
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cutManagers.add(new CutManager(
                        episode.AddNewCut(event.getX(), event.getY(), 0, 0), thisEpisodeManager));
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
                removeDragHandlers();
            }
        };
    }

    private EventHandler<MouseEvent> makeUnselectCutHandler() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                unselect();
            }
        };
    }

    private void removeDragHandlers() {
        workPane.removeEventHandler(MouseEvent.MOUSE_PRESSED, pressHandler);
        workPane.removeEventHandler(MouseEvent.MOUSE_DRAGGED, dragHandler);
        workPane.removeEventHandler(MouseEvent.MOUSE_RELEASED, releaseHandler);
    }
}
