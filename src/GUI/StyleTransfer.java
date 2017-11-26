package GUI;

import TVCS.Utils.ImageUtils;
import TVCS.Utils.TVCSPoint;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ina on 2017-11-22.
 */
//TODO Make parent class with effectGenerator
public class StyleTransfer {
    LayerManager parentLayerManager;
    BufferedImage handlingImage;
    BufferedImage newImage;
    Stage dialog = new Stage();
    Stage selectDialog;
    Pane rootPane = new Pane();
    ImageView imageView = new ImageView();

    static String NEURAL_HOME_PATH = "C:\\Users\\ina\\Desktop\\fast-style-transfer-master\\fast-style-transfer-master\\";
    static String EXAMPLE_PATH = NEURAL_HOME_PATH + "results";
    static String IN_PATH = NEURAL_HOME_PATH + "in";
    static String OUT_PATH = NEURAL_HOME_PATH + "out";
    ArrayList<File> exampleFiles = new ArrayList<>();
    ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    public StyleTransfer(LayerManager parentLayerManager) {
        this.parentLayerManager = parentLayerManager;
        dialog.initStyle(StageStyle.DECORATED);
        dialog.setTitle("Style Transfer");
    }

    public void showThis() {
        VBox container = new VBox();
        fillLayerContainer(container);
        Scene scene = new Scene(container, 600, 700);

        dialog.setScene(scene);
        dialog.show();
    }

    private void fillLayerContainer(VBox container) {
        setImageView(imageView);

        HBox buttons = new HBox();
        fillButtons(buttons);

        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(rootPane, buttons);
    }

    private void setImageView(ImageView imageView) {
        rootPane.prefHeightProperty().bind(imageView.fitHeightProperty());
        rootPane.prefWidthProperty().bind(imageView.fitWidthProperty());
        rootPane.getChildren().add(imageView);
        rootPane.setBorder((new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED,
                CornerRadii.EMPTY, new BorderWidths(2), Insets.EMPTY))));

        handlingImage = parentLayerManager.getFirstSelectedImage();

        imageView.setFitWidth(handlingImage.getWidth());
        imageView.setFitHeight(handlingImage.getHeight());
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
                showSelectDialog();
                startButton.setOnAction(null);
            }
        });
    }

    private void showSelectDialog() {
        selectDialog = new Stage();
        VBox topContainer = new VBox();
        topContainer.setPrefWidth(700);
        topContainer.setPrefHeight(250);
        ScrollPane selectContainer = new ScrollPane();
        HBox selectHBox = new HBox();
        selectContainer.setContent(selectHBox);
        selectContainer.setPrefWidth(700);
        selectContainer.setPrefHeight(200);
        selectHBox.setPrefWidth(1000);
        selectHBox.setPrefHeight(200);
        selectDialog.initStyle(StageStyle.DECORATED);
        selectDialog.setTitle("Style Transfer");
        fillGridPane(selectHBox);
        topContainer.getChildren().add(selectContainer);
        addButton(topContainer);
        Scene scene = new Scene(topContainer, 700, 250);
        selectDialog.setScene(scene);
        selectDialog.show();
    }

    private void addButton(VBox topContainer) {
        Button transferButton = new Button("Transfer");
        topContainer.getChildren().add(transferButton);
        transferButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int i = 0 ; i < checkBoxes.size() ; i++) {
                    if (checkBoxes.get(i).isSelected()) {

                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(IN_PATH + "\\temp.jpg");
                            ImageIO.write(handlingImage, "JPG", fileOutputStream);

                            long startTime = System.currentTimeMillis();
                            String modelPath = getModelPath(exampleFiles.get(i));
                            String line;
                            String command = "cmd /c cd C:\\Users\\ina\\Desktop\\fast-style-transfer-master\\fast-style-transfer-master &&" +
                                    " C:\\Users\\ina\\Anaconda3\\envs\\tensorflow\\python.exe evaluate.py " +
                                    " --checkpoint " + modelPath +
                                    " --in-path in " +
                                    " --out-path out " +
                                    " --allow-different-dimensions";
                            System.out.println("Command: " + command);
                            Process p = Runtime.getRuntime().exec(command);
                            BufferedReader bri = new BufferedReader
                                    (new InputStreamReader(p.getInputStream()));
                            BufferedReader bre = new BufferedReader
                                    (new InputStreamReader(p.getErrorStream()));
                            while ((line = bri.readLine()) != null) {
                                System.out.println(line);
                            }
                            bri.close();
                            while ((line = bre.readLine()) != null) {
                                System.out.println(line);
                            }
                            bre.close();
                            p.waitFor();
                            System.out.println("Done: " + String.valueOf((System.currentTimeMillis() - startTime) / 1000));




                            BufferedImage loadedimage = ImageIO.read(new File(OUT_PATH + "\\temp.jpg"));
                            newImage = new BufferedImage(loadedimage.getWidth(), loadedimage.getHeight(), BufferedImage.TYPE_INT_ARGB);
                            newImage.getGraphics().drawImage(loadedimage, 0, 0, null);
                            parentLayerManager.changeFirstSelectedImage(newImage);
                            imageView.setImage(ImageUtils.BufferedImageToFxImage(newImage));
                            selectDialog.close();
                        }
                        catch (Exception err) {
                            err.printStackTrace();
                        }
                    }
                }
            }
        });

    }

    private String getModelPath(File exampleFile) {
        String modelPath = NEURAL_HOME_PATH + "models\\" + exampleFile.getName();
        String direcpath = modelPath.replaceAll(".jpg", "checkpoint");
        if ((new File(direcpath)).isDirectory()) {
            return direcpath;
        } else {
            return modelPath.replaceAll(".jpg", ".ckpt");
        }
    }

    private void fillGridPane(HBox selectHBox) {
        File exampleDir = new File(EXAMPLE_PATH);
        File[] exampleFileList = exampleDir.listFiles();
        selectHBox.setPrefWidth(exampleFileList.length * 180);
        for (File exampleFile: exampleFileList) {
            if (exampleFile.isFile()) {
                try {
                    BufferedImage loadedimage = ImageIO.read(exampleFile);
                    VBox grid = new VBox();
                    grid.setPadding(new Insets(10));
                    CheckBox checkBox = new CheckBox();
                    ImageView imageView = new ImageView();
                    imageView.setImage(ImageUtils.BufferedImageToFxImage(loadedimage));
                    imageView.setFitWidth(250);
                    imageView.setFitHeight(160);

                    grid.getChildren().addAll(imageView, checkBox);
                    selectHBox.getChildren().add(grid);
                    exampleFiles.add(exampleFile);
                    checkBoxes.add(checkBox);

                } catch (IOException e) {
                    System.out.println("File read failed");
                }
            }
        }
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
