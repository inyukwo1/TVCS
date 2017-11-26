package GUI;

import TVCS.Utils.ImageUtils;
import TVCS.Utils.TVCSPoint;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

    ChoiceBox radiusChoice = new ChoiceBox(FXCollections.observableArrayList("5px", "10px", "15px", "20px"));
    int[] radius = new int[]{5, 10, 15, 20};
    int[] regionCheck; // Can get value from 0 to 255.
    BufferedImage showigImage;
    boolean checkRegioned = false;

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
        if (handlingImage == null) {
            //TODO do something
        }
        regionCheck = new int[handlingImage.getHeight() * handlingImage.getWidth()];
        for (int i = 0 ; i < regionCheck.length; i++) {
            regionCheck[i] = 0;
        }

        imageView.setFitWidth(handlingImage.getWidth());
        imageView.setFitHeight(handlingImage.getHeight());
        newImage = ImageUtils.CopyBufferedImage(handlingImage);
        showigImage = ImageUtils.CopyBufferedImage(handlingImage);
        imageView.setImage(ImageUtils.BufferedImageToFxImage(showigImage));
    }

    private void fillButtons(HBox buttons) {
        Button startButton = new Button("Start");
        setStartButton(startButton);
        Button checkRegion = new Button("Check region");
        setCheckRegionButton(checkRegion);
        Button saveButton = new Button("Save");
        setSaveButton(saveButton);
        Button okButton = new Button("Ok");
        setOkButton(okButton);

        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(startButton, checkRegion, saveButton, okButton, radiusChoice);
    }

    private void setCheckRegionButton(Button checkRegionButton) {
        checkRegionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                checkRegioned = true;
                rootPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        String radius = radiusChoice.getValue().toString();
                        int intrad = 5;
                        switch (radius) {
                            case  "5px":
                                intrad = 5;
                                break;
                            case "10px":
                                intrad = 10;
                                break;
                            case "15px":
                                intrad = 15;
                                break;
                            case "20px":
                                intrad = 20;
                                break;
                        }
                        for (int yindex = (int) event.getY() - intrad; yindex < (int) event.getY() + intrad ; yindex++) {
                            if (yindex < 0 || yindex >= (int) handlingImage.getHeight())
                                continue;
                            for (int xindex = (int) event.getX() - intrad; xindex < (int) event.getX() + intrad; xindex++) {
                                if (xindex < 0 || xindex >= (int) handlingImage.getWidth())
                                    continue;
                                int relativeX = xindex - (int)event.getX();
                                int relativeY = yindex - (int)event.getY();
                                double relativeRad = Math.sqrt(relativeX * relativeX + relativeY * relativeY);
                                if ((int) relativeRad > intrad)
                                    continue;
                                if ((int) relativeRad >= intrad / 2) {
                                    regionCheck[yindex * handlingImage.getWidth() + xindex] =
                                            Math.max(regionCheck[yindex * handlingImage.getWidth() + xindex], (int)(255 * ((intrad - relativeRad) / (intrad / 2))));
                                    int addRGB = 0x0000FF00 + ((regionCheck[yindex * handlingImage.getWidth() + xindex] / 2) << 24);
                                    int originRGB = showigImage.getRGB(xindex, yindex);
                                    showigImage.setRGB(xindex, yindex, ImageUtils.MixARGB(originRGB, addRGB));
                                } else {
                                    regionCheck[yindex * handlingImage.getWidth() + xindex] = 255;
                                    int addRGB = 0xFF00FF00;
                                    int originRGB = showigImage.getRGB(xindex, yindex);
                                    showigImage.setRGB(xindex, yindex, ImageUtils.MixARGB(originRGB, addRGB));
                                }
                            }
                        }
                        imageView.setImage(ImageUtils.BufferedImageToFxImage(showigImage));
                    }
                });
            }
        });
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
                            if (checkRegioned)
                                mixNewImage();
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

    private void mixNewImage() {
        for (int y = 0 ; y < newImage.getHeight() ; y++) {
            for (int x = 0 ; x < newImage.getWidth(); x++) {
                int newargb = newImage.getRGB(x, y);
                newargb &= 0x00FFFFFF;
                newargb += (regionCheck[y * newImage.getWidth() + x] << 24);
                int originargb = handlingImage.getRGB(x, y);
                newImage.setRGB(x, y, ImageUtils.MixARGB(originargb, newargb));
            }
        }
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
