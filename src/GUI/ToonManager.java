package GUI;

import GUI.EpisodeTree.EpisodeTreeManager;
import GUI.Utils.IntField;
import TVCS.Toon.Episode;
import TVCS.Toon.EpisodeInfo;
import TVCS.Toon.EpisodeTree.EpisodeVertex;
import TVCS.Toon.Toon;
import TVCS.Utils.DiscreteLocation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by ina on 2017-06-23.
 */

public class ToonManager {
    //TODO makeButton 함수들 buttonfactory클래스 만들어서 거길로 옮기기
    public Toon toon;
    EpisodeTreeManager episodeTreeManager;
    ArrayList<EpisodeManager> episodeManagers = new ArrayList<>();
    PushManager pushManager;
    BorderPane toonPane = new BorderPane();

    StackPane topPane;
    BorderPane centerContainerPane = new BorderPane();
    TabPane centerPane = new TabPane();
    RightPane rightPane = new RightPane();

    Button addNewCutButton;

    public ToonManager(Toon toon) {
        this.toon = toon;
        this.pushManager = new PushManager(toon);
        this.episodeTreeManager = new EpisodeTreeManager(toon.getEpisodeTree(), centerContainerPane);
        this.addNewCutButton = makeAddNewCutButton();
        setTabChangeListener();
    }

    public void stop(BorderPane rootPane) {
        rootPane.getChildren().remove(toonPane);
    }

    public void start(BorderPane rootPane) {
        fillBorderPane();
        rootPane.setCenter(toonPane);
    }

    public boolean saveToonAs(String toonPath) {
        return toon.SaveToon(toonPath);
    }

    public boolean hasPath() {
        return toon.hasPath();
    }

    public boolean saveToon() {
        return toon.SaveToon();
    }

    public void makeNewEpisode(String episodeName) {
        DiscreteLocation nextContentLocation = episodeTreeManager.nextContentLocation();
        if (nextContentLocation == null) {
            //TODO fail control
            return;
        }
        Episode newEpisode = toon.AddNewEpisode(episodeName,
                nextContentLocation, episodeTreeManager.episodeTreePane);
        addEpisode(newEpisode);
    }

    public boolean loadEpisode(EpisodeVertex episodeVertex) {
        Episode episode = toon.LoadEpisode(episodeVertex);
        if (episode == null) {
            return false;
        }
        addEpisode(episode);
        rightPane.syncEpisode(episode);
        return true;
    }

    private void setTabChangeListener() {
        centerPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
                        for (EpisodeManager episodeManager: episodeManagers) {
                            if (episodeManager.tab == newTab) {
                                rightPane.syncEpisode(episodeManager.episode);
                            }
                        }
                    }
                }
        );
    }

    private void addEpisode(Episode episode) {
        EpisodeManager newEpisodeManager = new EpisodeManager(episode);
        newEpisodeManager.start(centerPane);
        episodeManagers.add(newEpisodeManager);
        if(episodeManagers.size() == 1) {
            whenFirstEpisodeMade();
        }
    }

    public EpisodeManager selectedEpisodeManager() {
        EpisodeTab selectedTab = (EpisodeTab) centerPane.getSelectionModel().getSelectedItem();
        return selectedTab.parentEpisodeManager;
    }

    public void selectTab(Episode episode) {
        for (EpisodeManager episodeManager: episodeManagers) {
            if (episodeManager.episode == episode) {
                centerPane.getSelectionModel().select(episodeManager.tab);
            }
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
        rightPane.fillToonRelatedPane(episodeTreeManager.makeShowButton(), pushManager.getRegisterToonButton(),
                pushManager.getPushAllButton(), makeNewEpisodeButton());
        toonPane.setRight(rightPane.container);
    }

    private void whenFirstEpisodeMade() {
        // TODO 에피소드가 하나도 안남게 되었을 때 이거 삭제하는거 구현
        rightPane.fillEpisodeRelatedPane(pushManager.getPushEpisodeButton(),
                addNewCutButton, makeEpisodeSizeController());
    }

    private Button makeNewEpisodeButton() {
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
                selectedEpisodeManager().startAddCutMode();
            }
        });
        return newSceneButton;
    }

    private GridPane makeEpisodeSizeController() {
        GridPane episodeSizeController = new GridPane();
        Label widthLabel = new Label("Width: ");
        widthLabel.setMinWidth(50);
        widthLabel.setMinWidth(50);
        Slider widthSlider = new Slider(Episode.MIN_WIDTH, Episode.MAX_WIDTH, Episode.DEFAULT_WIDTH);
        Label widthValue = new Label(Integer.toString((int) widthSlider.getValue()));
        widthSlider.valueProperty().addListener(
                (ObservableValue<? extends Number> ov, Number oldValue, Number newValue) -> {
                    int newWidth = newValue.intValue();
                    selectedEpisodeManager().resizeWidth(newWidth);
                    widthValue.setText(((Integer) newWidth).toString());
                }
        );

        Label heightLabel = new Label("Height: ");
        heightLabel.setMinWidth(50);
        heightLabel.setMaxWidth(50);
        Slider heightSlider = new Slider(Episode.MIN_HEIGHT, Episode.MAX_HEIGHT, Episode.DEFAULT_HEIGHT);
        Label heightValue = new Label(Integer.toString((int) heightSlider.getValue()));
        heightSlider.valueProperty().addListener(
                (ObservableValue<? extends Number> ov, Number oldValue, Number newValue) -> {
                    int newHeight = newValue.intValue();
                    selectedEpisodeManager().resizeHeight(newHeight);
                    heightValue.setText(((Integer) newHeight).toString());
                }
        );

        episodeSizeController.add(widthLabel, 0, 0);
        episodeSizeController.add(widthSlider, 1, 0);
        episodeSizeController.add(widthValue, 2, 0);
        episodeSizeController.add(heightLabel, 0, 1);
        episodeSizeController.add(heightSlider, 1, 1);
        episodeSizeController.add(heightValue, 2, 1);

        rightPane.setWidthSlider(widthSlider);
        rightPane.setHeightSlider(heightSlider);
        return episodeSizeController;
    }

    private void addCenterPane() {
        centerPane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        toonPane.setCenter(centerContainerPane);
        centerContainerPane.setCenter(centerPane);
    }


    //TODO When there become no episode, we have to remove some buttons
}
