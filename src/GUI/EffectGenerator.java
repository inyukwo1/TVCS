package GUI;


import TVCS.Utils.ImageUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.image.BufferedImage;

/**
 * Created by ina on 2017-11-17.
 */
public class EffectGenerator {
    LayerManager parentLayerManager;
    BufferedImage handlingImage;
    BufferedImage newImage;
    Stage dialog = new Stage();

    public EffectGenerator(LayerManager parentLayerManager) {
        this.parentLayerManager = parentLayerManager;
        dialog.initStyle(StageStyle.DECORATED);
        dialog.setTitle("Effect Generator");
    }

    public Button makeEffectGeneratorButton() {
        Button effectgeneratorButtton = new Button("EffectGenerator");

        effectgeneratorButtton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                VBox container = new VBox();
                fillLayerContainer(container);
                Scene scene = new Scene(container, 600, 700);

                dialog.setScene(scene);
                dialog.show();
            }
        });

        return effectgeneratorButtton;
    }

    private void fillLayerContainer(VBox container) {
        ImageView imageView = new ImageView();
        setImageView(imageView);

        HBox buttons = new HBox();
        fillButtons(buttons);

        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(imageView, buttons);
    }

    private void setImageView(ImageView imageView) {
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(600);
        handlingImage = parentLayerManager.getFirstSelectedImage();
        newImage = ImageUtils.CopyBufferedImage(handlingImage);
        if (handlingImage == null) {
            //TODO do something
        }
        imageView.setImage(ImageUtils.BufferedImageToFxImage(handlingImage));
    }

    private void fillButtons(HBox buttons) {
        Button startButton = new Button("Start");
        setStartButton(startButton);
        Button saveButton = new Button("Save");
        setSaveButton(saveButton);
        Button okButton = new Button("Ok");
        setOkButton(okButton);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(startButton, saveButton, okButton);
    }

    private void setStartButton(Button startButton) {
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                takeFirstPivot();
                startButton.setOnAction(null);
            }
        });
    }

    private void takeFirstPivot() {
        //TODO
    }

    private void takeSecondPivot() {
        //TODO
    }

    private void takeThirdPivot() {
        //TODO
    }

    private void takeFourthPivot() {
        //TODO
    }

    private void generateImages() {
        //TODO
    }

    private void setSaveButton(Button saveButton) {
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                parentLayerManager.changeFirstSelectedImage(newImage);
            }
        });
    }

    private void setOkButton (Button okButton) {
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
    }
}
