package GUI;

import TVCS.Toon.Cut;
import TVCS.Toon.CutImage;
import TVCS.Utils.ImageUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ina on 2017-11-16.
 */
public class LayerManager {
    public static int IMAGEVIEW_WIDTH = 180;
    public static int IMAGEVIEW_HEIGHT = 180;

    Button showLayersButton = new Button("Show Layers");
    Stage dialog = new Stage();
    ArrayList<CheckBox> checkBoxes = new ArrayList<>();
    ArrayList<ImageView> imageViews = new ArrayList<>();
    ArrayList<BufferedImage> images = new ArrayList<>();
    CutManager cutManager;
    Cut cut;

    public LayerManager(CutManager cutManager) {
        this.cutManager = cutManager;
        this.cut = cutManager.cut;
        dialog.initStyle(StageStyle.DECORATED);
        dialog.setTitle("Layers");
        showLayersButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showLayers();
            }
        });
    }

    public BufferedImage getFirstSelectedImage() {
        for (int i = 0 ; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                return images.get(i);
            }
        }
        return null;
    }

    public void changeFirstSelectedImage(BufferedImage image) {
        for (int i = 0 ; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                images.set(i, image);
                imageViews.get(i).setImage(ImageUtils.BufferedImageToFxImage(ImageUtils.AlphaRepresentingImage(image)));
                return;
            }
        }
    }

    private void showLayers() {
        VBox container = new VBox();
        fillLayerContainer(container);
        Scene scene = new Scene(container, 700, 300);

        dialog.setScene(scene);
        dialog.show();
    }

    private void fillLayerContainer(VBox container) {
        ScrollPane layersPaneContainer = new ScrollPane();
        layersPaneContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        layersPaneContainer.setMaxHeight(250);
        layersPaneContainer.setMinHeight(250);
        layersPaneContainer.setMaxWidth(650);
        layersPaneContainer.setMinWidth(650);
        Pane layersPane = new Pane();
        layersPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        layersPane.setMinHeight(230);
        layersPane.setMaxHeight(230);
        layersPane.setMaxWidth((IMAGEVIEW_WIDTH + 40) * cut.images.size());
        layersPane.setMinWidth((IMAGEVIEW_WIDTH + 40) * cut.images.size());
        layersPaneContainer.setContent(layersPane);
        fillLayers(layersPane);
        HBox buttons = makeButtons();

        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(layersPaneContainer, buttons);
    }

    private void fillLayers(Pane layersPane) {
        HBox layers = new HBox();
        layers.setAlignment(Pos.CENTER);
        layers.setMinHeight(230);
        layers.setMaxHeight(230);
        for (int i = 0 ; i < cut.images.size(); i++) {
            final int index = i;
            CutImage image = cut.images.get(i);
            VBox imageAndCheckbox = new VBox();
            imageAndCheckbox.setAlignment(Pos.CENTER);
            imageAndCheckbox.setPadding(new Insets(20));

            HBox imageViewContainer = new HBox();
            imageViewContainer.setPrefWidth(IMAGEVIEW_WIDTH);
            imageViewContainer.setPrefHeight(IMAGEVIEW_HEIGHT);
            ImageView imageView = new ImageView();
            imageView.setPreserveRatio(false);
            imageView.setImage(image.alphaRepresentingImage());
            imageView.setFitHeight(IMAGEVIEW_HEIGHT);
            imageView.setFitWidth(IMAGEVIEW_WIDTH);
            imageViews.add(imageView);
            images.add(image.image);
            imageViewContainer.getChildren().add(imageView);
            imageViewContainer.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    Dragboard dragboard = event.getDragboard();
                    if(dragboard.hasFiles()) {
                        event.acceptTransferModes(TransferMode.COPY);
                    } else {
                        event.consume();
                    }
                }
            });
            imageViewContainer.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    try {
                        Dragboard dragboard = event.getDragboard();
                        boolean success = false;
                        if (dragboard.hasFiles()) {
                            success = true;
                            for (File file : dragboard.getFiles()) {
                                BufferedImage loadedimage = ImageIO.read(file);
                                BufferedImage image = new BufferedImage(loadedimage.getWidth(), loadedimage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                                image.getGraphics().drawImage(loadedimage, 0, 0, null);
                                images.set(index, image);
                                imageViews.get(index).setImage(ImageUtils.BufferedImageToFxImage(ImageUtils.AlphaRepresentingImage(image)));
                            }
                        }
                        event.setDropCompleted(success);
                        event.consume();
                    } catch (IOException e){}
                }
            });

            CheckBox checkBox = new CheckBox();
            checkBoxes.add(checkBox);

            imageAndCheckbox.getChildren().addAll(imageViewContainer, checkBox);
            layers.getChildren().add(imageAndCheckbox);
        }
        layersPane.getChildren().add(layers);
    }

    private HBox makeButtons() {
        LayerManager myLayerManager = this;
        HBox buttons = new HBox();
        Button okButton = new Button("OK");
        setOkButton(okButton);
        okButton.setPadding(new Insets(10));
        Button floodFillButton = new Button("Transparent");
        setfloodFillButton(floodFillButton);
        floodFillButton.setPadding(new Insets(10));
        Button effectGeneratorButton = new Button("Effect Generator");
        effectGeneratorButton.setPadding(new Insets(10));
        effectGeneratorButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                (new EffectGenerator(myLayerManager)).showThis();
            }
        });
        Button styleTransferButton = new Button("Style Transfer");
        styleTransferButton.setPadding(new Insets(10));
        styleTransferButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                (new StyleTransfer(myLayerManager)).showThis();
            }
        });
        Button saveButton = new Button("Save");
        saveButton.setPadding(new Insets(10));
        setSaveButton(saveButton);
        buttons.getChildren().addAll(okButton, floodFillButton, effectGeneratorButton, styleTransferButton, saveButton);

        return buttons;
    }

    private void setOkButton(Button okButton) {
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cutManager.resetImage();
                dialog.close();
            }
        });
    }

    private void setfloodFillButton(Button floodFillButton) {
        floodFillButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for(int i = 0; i < imageViews.size(); i++) {
                    final int index = i;
                    imageViews.get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            BufferedImage relatedImage = images.get(index);
                            ImageView imageView = imageViews.get(index);
                            BufferedImage newImage = ImageUtils.TransParentFloodFilledImage(relatedImage,
                                    (int) event.getX() * (int)(relatedImage.getWidth() / imageView.getFitWidth()),
                                    (int) event.getY()* (int)(relatedImage.getHeight() / imageView.getFitHeight()));
                            images.set(index, newImage);
                            imageView.setImage(ImageUtils.BufferedImageToFxImage(ImageUtils.AlphaRepresentingImage(newImage)));
                        }
                    });
                }
            }
        });
    }

    private void setSaveButton(Button saveButton) {
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for(int i = 0; i < images.size(); i++) {
                    CutImage relatedCutImage = cut.images.get(i);
                    if (ImageUtils.isImageDifferent(relatedCutImage.image, images.get(i))) {
                        relatedCutImage.image = images.get(i);
                        relatedCutImage.updated();
                    }
                }
                cutManager.resetImage();
            }
        });
    }
}
