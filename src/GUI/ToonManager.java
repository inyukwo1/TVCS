package GUI;

import TVCS.Toon.Episode;
import TVCS.Toon.Toon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by ina on 2017-06-23.
 */

public class ToonManager {
    Toon toon;
    BranchManager branchManager;
    ArrayList<EpisodeManager> episodeManagers = new ArrayList<>();
    BorderPane toonPane;

    StackPane topPane;
    TabPane centerPane;
    GridPane rightPane;

    Button addNewCutButton;

    int defaultWidth = 500;
    int defaultHeight = 3000;

    public ToonManager(Toon toon) {
        this.toon = toon;
        this.branchManager = new BranchManager(toon.getBranch());
        this.addNewCutButton = makeAddNewCutButton();
    }

    public void stop(BorderPane rootPane) {
        rootPane.getChildren().remove(toonPane);
    }

    public void start(BorderPane rootPane) {
        this.toonPane = new BorderPane();
        fillBorderPane();
        rootPane.setCenter(toonPane);
    }

    public void makeNewEpisode(String episodeName) {
        Episode newEpisode = toon.AddNewEpisode(episodeName, defaultWidth, defaultHeight);
        EpisodeManager newEpisodeManager = new EpisodeManager(newEpisode);
        newEpisodeManager.start(centerPane);
        episodeManagers.add(newEpisodeManager);
        if(episodeManagers.size() == 1) {
            rightPane.add(addNewCutButton, 0, 2);
        }
    }

    private void fillBorderPane() {
        addTopPane();
        addRightPane();
        addCenterPane();
    }

    private void addTopPane() {
        topPane = new StackPane();
        topPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        topPane.setMaxHeight(25);
        topPane.setMinHeight(25);
        topPane.setPadding(new Insets(5,5,5,15));
        topPane.setBorder(new Border(new BorderStroke(Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY,
             BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                CornerRadii.EMPTY, BorderWidths.DEFAULT, Insets.EMPTY)));
        Label topLabel = new Label(toon.name());
        setTopPaneFont(topPane, topLabel);
        toonPane.setTop(topPane);
    }

    private void setTopPaneFont(StackPane topPane, Label label) {
        label.setAlignment(Pos.CENTER_LEFT);
        topPane.getChildren().add(label);
        topPane.setAlignment(label, Pos.CENTER_LEFT);
    }

    private void addRightPane() {
        rightPane = new GridPane();
        rightPane.setMinWidth(250);
        rightPane.setMaxWidth(250);
        rightPane.setBorder(new Border(new BorderStroke(Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY,
                BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, BorderWidths.DEFAULT, Insets.EMPTY)));
        rightPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        rightPane.setAlignment(Pos.CENTER);
        fillRightPane();
        toonPane.setRight(rightPane);
    }

    private void fillRightPane() {
        rightPane.add(branchManager.makeShowButton(), 0, 0);
        rightPane.add(makeNewSceneButton(), 0, 1);
    }

    private Button makeNewSceneButton() {
        Button newSceneButton = new Button("New Episode");
        newSceneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MakeNewEpisode makeNewEpisode = new MakeNewEpisode();
                makeNewEpisode.start();
            }
        });
        return newSceneButton;
    }

    private Button makeAddNewCutButton() {
        Button newSceneButton = new Button("Add New Cut");
        newSceneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                EpisodeTab selectedTab = (EpisodeTab) centerPane.getSelectionModel().getSelectedItem();
                EpisodeManager selectedEpisodeManager = selectedTab.parentEpisodeManager;
                selectedEpisodeManager.startAddCutMode();
            }
        });
        return newSceneButton;
    }

    private void addCenterPane() {
        centerPane = new TabPane();
        centerPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        toonPane.setCenter(centerPane);
    }
}
