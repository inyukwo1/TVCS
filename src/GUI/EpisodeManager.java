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
import javafx.scene.shape.Line;

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
    ArrayList<Line> gridVerticalLines = new ArrayList<>();
    ArrayList<Line> gridHorizontalLines = new ArrayList<>();

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
        for (int i = gridVerticalLines.size() - 1; i >= 0; i--) {
            Line verticalLine = gridVerticalLines.get(i);
            if (verticalLine.getStartX() >= width) {
                gridVerticalLines.remove(verticalLine);
                workPane.getChildren().remove(verticalLine);
            }
        }
        int verticalGridPosition = episode.getGridSize();
        while (verticalGridPosition < episode.getWidth()) {
            verticalGridPosition += episode.getGridSize();
        }
        addVerticalGridLine(verticalGridPosition, width, episode.getGridSize());
        for (Line horizontalLine : gridHorizontalLines) {
            horizontalLine.setEndX(width);
        }
        episode.resizeWidth(width);
    }

    public void resizeHeight(int height) {
        workPane.setMinHeight(height);
        workPane.setMaxHeight(height);
        for (Line verticalLine : gridVerticalLines) {
            verticalLine.setEndY(height);
        }
        for (int i = gridHorizontalLines.size() - 1; i >= 0; i--) {
            Line horizontalLine = gridHorizontalLines.get(i);
            if (horizontalLine.getStartY() >= height) {
                gridVerticalLines.remove(horizontalLine);
                workPane.getChildren().remove(horizontalLine);
            }
        }
        int horizontalGridPosition = episode.getGridSize();
        while (horizontalGridPosition < episode.getHeight()) {
            horizontalGridPosition += episode.getGridSize();
        }
        addHorizontalGridLine(horizontalGridPosition, height, episode.getGridSize());
        episode.resizeHeight(height);
    }

    public void resizeGrid(int gridSize) {
        int verticalGridPosition = gridSize;
        for (int i = 0; i < gridVerticalLines.size(); i++) {
            if (verticalGridPosition >= episode.getWidth()) {
                for (int j = i; j < gridVerticalLines.size();) {
                    workPane.getChildren().remove(gridVerticalLines.get(j));
                    gridVerticalLines.remove(j);
                }
                break;
            }
            Line verticalLine = gridVerticalLines.get(i);
            verticalLine.setStartX(verticalGridPosition);
            verticalLine.setEndX(verticalGridPosition);

            verticalGridPosition += gridSize;
        }

        addVerticalGridLine(verticalGridPosition, episode.getWidth(), gridSize);

        int horizontalGridPosition = gridSize;
        for (int i = 0; i < gridHorizontalLines.size(); i++) {
            if (horizontalGridPosition >= episode.getHeight()) {
                for (int j = i; j < gridHorizontalLines.size();) {
                    workPane.getChildren().remove(gridHorizontalLines.get(j));
                    gridHorizontalLines.remove(j);
                }
                break;
            }
            Line horizontalLine = gridHorizontalLines.get(i);
            horizontalLine.setStartY(horizontalGridPosition);
            horizontalLine.setEndY(horizontalGridPosition);

            horizontalGridPosition += gridSize;
        }
        addHorizontalGridLine(horizontalGridPosition, episode.getHeight(), gridSize);

        episode.setGridSize(gridSize);
    }

    private void addVerticalGridLine(int startX, int endX, int gridSize) {
        for (; startX < endX ; startX += gridSize) {
            Line verticalLine = new Line(startX, 0, startX, episode.getHeight());
            verticalLine.getStrokeDashArray().addAll(2d, 4d);
            gridVerticalLines.add(verticalLine);
            workPane.getChildren().add(verticalLine);
            verticalLine.toBack();
        }
    }

    private void addHorizontalGridLine(int startY, int endY, int gridSize) {
        for (; startY < endY ; startY += gridSize) {
            Line horizontalLine = new Line(0, startY, episode.getWidth(), startY);
            horizontalLine.getStrokeDashArray().addAll(2d, 4d);
            gridHorizontalLines.add(horizontalLine);
            workPane.getChildren().add(horizontalLine);
            horizontalLine.toBack();
        }
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
        hboxForWorkPane.setBackground(
                new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        hboxForWorkPane.prefWidthProperty().bind(pane.widthProperty());
        workPane.setMaxSize(episode.getWidth(), episode.getHeight());
        workPane.setMinSize(episode.getWidth(), episode.getHeight());
        workPane.setBackground(
                new Background(new BackgroundFill(episode.getBackgroundColor(), CornerRadii.EMPTY, Insets.EMPTY)));
        makeGrids();
        pane.setContent(hboxForWorkPane);
        tab.setContent(pane);
        tab.setText(episode.name());
    }

    private void makeGrids() {
        for (int verticalGridPosition = episode.getGridSize() ;
             verticalGridPosition < episode.getWidth() ; verticalGridPosition += episode.getGridSize()) {
            Line verticalLine = new Line(verticalGridPosition, 0, verticalGridPosition, episode.getHeight());
            verticalLine.getStrokeDashArray().addAll(2d, 4d);
            gridVerticalLines.add(verticalLine);
            workPane.getChildren().add(verticalLine);
        }
        for (int horizontalGridPosition = episode.getGridSize() ;
             horizontalGridPosition < episode.getHeight() ; horizontalGridPosition += episode.getGridSize()) {
            Line horizontalLine = new Line(0, horizontalGridPosition, episode.getWidth(), horizontalGridPosition);
            horizontalLine.getStrokeDashArray().addAll(2d, 4d);
            gridHorizontalLines.add(horizontalLine);
            workPane.getChildren().add(horizontalLine);
        }
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
