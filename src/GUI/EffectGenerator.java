package GUI;


import TVCS.Utils.ImageUtils;
import TVCS.Utils.TVCSPoint;
import TVCS.Utils.TVCSVector;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by ina on 2017-11-17.
 */
public class EffectGenerator {
    static int POINT_RADIUS = 3;
    LayerManager parentLayerManager;
    BufferedImage handlingImage;
    BufferedImage newImage;
    Stage dialog = new Stage();
    Pane rootPane = new Pane();
    ImageView imageView = new ImageView();
    Random randomGenerator = new Random();

    double verticalScale = 0, horizontalScale = 0, shapeRad = 20;
    long timeGap = 250;
    long timeElapsed = 0;
    TVCSPoint pivot, first, second, third, fourth;
    TVCSPoint newFirst, newSecond, newThird, newFourth;

    public EffectGenerator(LayerManager parentLayerManager) {
        this.parentLayerManager = parentLayerManager;
        dialog.initStyle(StageStyle.DECORATED);
        dialog.setTitle("Effect Generator");
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
                takeFirstPivot();
                startButton.setOnAction(null);
            }
        });
    }

    private void takeFirstPivot() {
        rootPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                first = new TVCSPoint(event.getX(), event.getY());
                imageView.setImage(ImageUtils.BufferedImageToFxImage(newImage));
                drawPoint(first);
                takeSecondPivot();
            }
        });
    }

    private void takeSecondPivot() {
        rootPane.setOnMouseClicked(null);
        rootPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                second = new TVCSPoint(event.getX(), event.getY());
                drawPoint(second);
                drawLine(first, second);
                takeThirdPivot();
            }
        });
    }

    private void takeThirdPivot() {
        rootPane.setOnMouseClicked(null);
        rootPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                third = new TVCSPoint(event.getX(), event.getY());
                drawPoint(third);
                drawDottedLine(first, third);
                drawDottedLine(second, third);
                takeFourthPivot();
            }
        });
    }

    private void takeFourthPivot() {
        rootPane.setOnMouseClicked(null);
        rootPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                fourth = new TVCSPoint(event.getX(), event.getY());
                drawPoint(fourth);
                drawDottedLine(first, fourth);
                drawDottedLine(second, fourth);
                drawLine(third, fourth);
                generateImages();
            }
        });
    }

    private void generateImages() {
        rootPane.setOnMouseClicked(null);
        rootPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (pivot == null) {
                    timeElapsed = System.currentTimeMillis();
                    pivot = new TVCSPoint(event.getX(), event.getY());
                    return;
                }

                if (System.currentTimeMillis() - timeElapsed >= timeGap) {
                    if (pivot.x == event.getX() && pivot.y == event.getY())
                        return;
                    TVCSVector moveVector = new TVCSVector(event.getX() - pivot.x, event.getY() - pivot.y);
                    //System.out.println("moveec: "+ moveVector.x + "/" + moveVector.y);
                    TVCSVector firstVector = new TVCSVector(first.x - (first.x + second.x) / 2, first.y - (first.y + second.y) / 2);
                    TVCSVector secondVector = new TVCSVector(second.x - (first.x + second.x) / 2, second.y - (first.y + second.y) / 2);
                    TVCSVector thirdVector = new TVCSVector(third.x - (first.x + second.x) / 2, third.y - (first.y + second.y) / 2);
                    TVCSVector fourthVector = new TVCSVector(fourth.x - (first.x + second.x) / 2, fourth.y - (first.y + second.y) / 2);

                    TVCSVector unitFirstVector = new TVCSVector(firstVector.x, firstVector.y);
                    unitFirstVector.unitize();
                    TVCSVector newFirstVector = moveVector.unitProportionalRight();

                    double sinTheta = (unitFirstVector.x * newFirstVector.y - unitFirstVector.y * newFirstVector.x);
                    double cosTheta = (unitFirstVector.x * newFirstVector.x + unitFirstVector.y * newFirstVector.y);

                    TVCSVector newThirdVector = new TVCSVector(thirdVector.x * cosTheta - thirdVector.y * sinTheta,
                            thirdVector.y * cosTheta + thirdVector.x * sinTheta);
                    TVCSVector newFourthVector = new TVCSVector(fourthVector.x * cosTheta - fourthVector.y * sinTheta,
                            fourthVector.y * cosTheta + fourthVector.x * sinTheta);

                    newFirstVector.makeSize(firstVector.size());
                    TVCSVector newSecondVector = moveVector.unitProportionalLeft();
                    newSecondVector.makeSize(secondVector.size());


                   /* System.out.println("firstvec: "+ newFirstVector.x + "/" + newFirstVector.y);
                    System.out.println("secondvec: "+ newSecondVector.x + "/" + newSecondVector.y);
                    System.out.println("originthird: " + thirdVector.x + "/" + thirdVector.y);
                    System.out.println("thirdvec: "+ newThirdVector.x + "/" + newThirdVector.y);
                    System.out.println("cossin: " + cosTheta + "/" + sinTheta);

                    System.out.println("fourthvec: "+ newFourthVector.x + "/" + newFourthVector.y);*/

                    newFirst = new TVCSPoint(event.getX(), event.getY());
                    newFirst.plusVector(newFirstVector, newFirstVector.size() * (1 - verticalScale + (2 * verticalScale) * randomGenerator.nextDouble()));
                    newSecond = new TVCSPoint(event.getX(), event.getY());
                    newSecond.plusVector(newSecondVector, newSecondVector.size() * (1 - verticalScale + (2 * verticalScale) * randomGenerator.nextDouble()));
                    newThird = new TVCSPoint(event.getX(), event.getY());
                    newThird.plusVector(newThirdVector, newThirdVector.size() * (1 - horizontalScale + (2 * horizontalScale) * randomGenerator.nextDouble()));
                    newFourth = new TVCSPoint(event.getX(), event.getY());
                    newFourth.plusVector(newFourthVector, newFourthVector.size() * (1 - horizontalScale + (2 * horizontalScale) * randomGenerator.nextDouble()));

                    newFirst.x += shapeRad * (-1 + 2 * randomGenerator.nextDouble());
                    newFirst.y += shapeRad * (-1 + 2 * randomGenerator.nextDouble());
                    newSecond.x += shapeRad * (-1 + 2 * randomGenerator.nextDouble());
                    newSecond.y += shapeRad * (-1 + 2 * randomGenerator.nextDouble());
                    newThird.x += shapeRad * (-1 + 2 * randomGenerator.nextDouble());
                    newThird.y += shapeRad * (-1 + 2 * randomGenerator.nextDouble());
                    newFourth.x += shapeRad * (-1 + 2 * randomGenerator.nextDouble());
                    newFourth.y += shapeRad * (-1 + 2 * randomGenerator.nextDouble());

                    copyImage();
                    drawLine(newFirst, newFourth);
                    drawLine(newFourth, newSecond);
                    drawLine(newSecond, newThird);
                    drawLine(newThird, newFirst);


                    pivot.x = event.getX();
                    pivot.y = event.getY();
                    timeElapsed = System.currentTimeMillis();
                }

            }
        });
    }

    private void copyImage() {
        //upper side
        double upA=0, upB=0, upC=0, upD=0, upE=0, upF=0;
        double upperMat[][] = new double[6][6];
        upperMat[0][0] = newFirst.x;
        upperMat[0][1] = newFirst.y;
        upperMat[0][2] = 0;
        upperMat[0][3] = 0;
        upperMat[0][4] = 1;
        upperMat[0][5] = 0;

        upperMat[1][0] = 0;
        upperMat[1][1] = 0;
        upperMat[1][2] = newFirst.x;
        upperMat[1][3] = newFirst.y;
        upperMat[1][4] = 0;
        upperMat[1][5] = 1;

        upperMat[2][0] = newThird.x;
        upperMat[2][1] = newThird.y;
        upperMat[2][2] = 0;
        upperMat[2][3] = 0;
        upperMat[2][4] = 1;
        upperMat[2][5] = 0;

        upperMat[3][0] = 0;
        upperMat[3][1] = 0;
        upperMat[3][2] = newThird.x;
        upperMat[3][3] = newThird.y;
        upperMat[3][4] = 0;
        upperMat[3][5] = 1;

        upperMat[4][0] = newFourth.x;
        upperMat[4][1] = newFourth.y;
        upperMat[4][2] = 0;
        upperMat[4][3] = 0;
        upperMat[4][4] = 1;
        upperMat[4][5] = 0;

        upperMat[5][0] = 0;
        upperMat[5][1] = 0;
        upperMat[5][2] = newFourth.x;
        upperMat[5][3] = newFourth.y;
        upperMat[5][4] = 0;
        upperMat[5][5] = 1;

        double upperVec[] = new double[6];

        upperVec[0] = first.x;
        upperVec[1] = first.y;
        upperVec[2] = third.x;
        upperVec[3] = third.y;
        upperVec[4] = fourth.x;
        upperVec[5] = fourth.y;

        double upperInverted[][] = invert(upperMat);
        for (int i = 0 ; i < 6; i++)
            upA += upperInverted[0][i] * upperVec[i];
        for (int i = 0 ; i < 6; i++)
            upB += upperInverted[1][i]* upperVec[i];
        for (int i = 0 ; i < 6; i++)
            upC += upperInverted[2][i]* upperVec[i];
        for (int i = 0 ; i < 6; i++)
            upD += upperInverted[3][i]* upperVec[i];
        for (int i = 0 ; i < 6; i++)
            upE += upperInverted[4][i]* upperVec[i];
        for (int i = 0 ; i < 6; i++)
            upF += upperInverted[5][i]* upperVec[i];
        //lower side
        double lowerA=0, lowerB=0, lowerC=0, lowerD=0, lowerE=0, lowerF=0;
        double lowerMat[][] = new double[6][6];
        lowerMat[0][0] = newSecond.x;
        lowerMat[0][1] = newSecond.y;
        lowerMat[0][2] = 0;
        lowerMat[0][3] = 0;
        lowerMat[0][4] = 1;
        lowerMat[0][5] = 0;

        lowerMat[1][0] = 0;
        lowerMat[1][1] = 0;
        lowerMat[1][2] = newSecond.x;
        lowerMat[1][3] = newSecond.y;
        lowerMat[1][4] = 0;
        lowerMat[1][5] = 1;

        lowerMat[2][0] = newThird.x;
        lowerMat[2][1] = newThird.y;
        lowerMat[2][2] = 0;
        lowerMat[2][3] = 0;
        lowerMat[2][4] = 1;
        lowerMat[2][5] = 0;

        lowerMat[3][0] = 0;
        lowerMat[3][1] = 0;
        lowerMat[3][2] = newThird.x;
        lowerMat[3][3] = newThird.y;
        lowerMat[3][4] = 0;
        lowerMat[3][5] = 1;

        lowerMat[4][0] = newFourth.x;
        lowerMat[4][1] = newFourth.y;
        lowerMat[4][2] = 0;
        lowerMat[4][3] = 0;
        lowerMat[4][4] = 1;
        lowerMat[4][5] = 0;

        lowerMat[5][0] = 0;
        lowerMat[5][1] = 0;
        lowerMat[5][2] = newFourth.x;
        lowerMat[5][3] = newFourth.y;
        lowerMat[5][4] = 0;
        lowerMat[5][5] = 1;

        double lowerVec[] = new double[6];

        lowerVec[0] = second.x;
        lowerVec[1] = second.y;
        lowerVec[2] = third.x;
        lowerVec[3] = third.y;
        lowerVec[4] = fourth.x;
        lowerVec[5] = fourth.y;

        double lowerInverted[][] = invert(lowerMat);
        for (int i = 0 ; i < 6; i++)
            lowerA += lowerInverted[0][i]* lowerVec[i];
        for (int i = 0 ; i < 6; i++)
            lowerB += lowerInverted[1][i]* lowerVec[i];
        for (int i = 0 ; i < 6; i++)
            lowerC += lowerInverted[2][i]* lowerVec[i];
        for (int i = 0 ; i < 6; i++)
            lowerD += lowerInverted[3][i]* lowerVec[i];
        for (int i = 0 ; i < 6; i++)
            lowerE += lowerInverted[4][i]* lowerVec[i];
        for (int i = 0 ; i < 6; i++)
            lowerF += lowerInverted[5][i]* lowerVec[i];

        //calculate
        int startX = (int) Math.min(Math.min(newFirst.x, newSecond.x),
                Math.min(newThird.x, newFourth.x));
        int startY = (int) Math.min(Math.min(newFirst.y, newSecond.y),
                Math.min(newThird.y, newFourth.y));
        int endX = (int) Math.max(Math.max(newFirst.x, newSecond.x),
                Math.max(newThird.x, newFourth.x));
        int endY = (int) Math.max(Math.max(newFirst.y, newSecond.y),
                Math.max(newThird.y, newFourth.y));
        double lineA = (newFourth.y - newThird.y) / (newFourth.x - newThird.x);
        double lineB = newFourth.y - lineA * newFourth.x;

        for (int j = startY; j < endY; j++) {
            for (int i = startX; i < endX; i++) {
                if (i < 0 || i >= newImage.getWidth() || j < 0 || j >= newImage.getHeight())
                    continue;
                if (lineA * i + lineB > j) { // upper
                    int originX = (int) (upA * i + upB * j + upE);
                    int originY = (int) (upC * i + upD * j + upF);
                    if (originX < 0 || originX >= handlingImage.getWidth() ||
                            originY < 0 || originY >= handlingImage.getHeight())
                        continue;
                    int newRGB = handlingImage.getRGB(originX, originY);
                    if ((newRGB >> 24) != 0)
                        newImage.setRGB(i, j, newRGB);
                } else { //lower
                    int originX = (int) (lowerA * i + lowerB * j + lowerE);
                    int originY = (int) (lowerC * i + lowerD * j + lowerF);
                    if (originX < 0 || originX >= handlingImage.getWidth() ||
                            originY < 0 || originY >= handlingImage.getHeight())
                        continue;
                    int newRGB = handlingImage.getRGB(originX, originY);
                    if ((newRGB >> 24) != 0)
                        newImage.setRGB(i, j, newRGB);
                }
            }
        }
        imageView.setImage(ImageUtils.BufferedImageToFxImage(newImage));
    }

    private double[][] invert(double a[][])
    {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i=0; i<n; ++i)
            b[i][i] = 1;

        // Transform the matrix into an upper triangle
        gaussian(a, index);

        // Update the matrix b[i][j] with the ratios stored
        for (int i=0; i<n-1; ++i)
            for (int j=i+1; j<n; ++j)
                for (int k=0; k<n; ++k)
                    b[index[j]][k]
                            -= a[index[j]][i]*b[index[i]][k];

        // Perform backward substitutions
        for (int i=0; i<n; ++i)
        {
            x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
            for (int j=n-2; j>=0; --j)
            {
                x[j][i] = b[index[j]][i];
                for (int k=j+1; k<n; ++k)
                {
                    x[j][i] -= a[index[j]][k]*x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }

// Method to carry out the partial-pivoting Gaussian
// elimination.  Here index[] stores pivoting order.

    private void gaussian(double a[][], int index[])
    {
        int n = index.length;
        double c[] = new double[n];

        // Initialize the index
        for (int i=0; i<n; ++i)
            index[i] = i;

        // Find the rescaling factors, one from each row
        for (int i=0; i<n; ++i)
        {
            double c1 = 0;
            for (int j=0; j<n; ++j)
            {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }

        // Search the pivoting element from each column
        int k = 0;
        for (int j=0; j<n-1; ++j)
        {
            double pi1 = 0;
            for (int i=j; i<n; ++i)
            {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1)
                {
                    pi1 = pi0;
                    k = i;
                }
            }

            // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i=j+1; i<n; ++i)
            {
                double pj = a[index[i]][j]/a[index[j]][j];

                // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;

                // Modify other elements accordingly
                for (int l=j+1; l<n; ++l)
                    a[index[i]][l] -= pj*a[index[j]][l];
            }
        }
    }

    private void drawPoint(TVCSPoint point) {
        Circle circle = new Circle();
        circle.setCenterX(point.x);
        circle.setCenterY(point.y);
        circle.setRadius(POINT_RADIUS);
        rootPane.getChildren().add(circle);
    }

    private void drawLine(TVCSPoint start, TVCSPoint end) {
        Line line = new Line();
        line.setStartX(start.x);
        line.setStartY(start.y);
        line.setEndX(end.x);
        line.setEndY(end.y);
        rootPane.getChildren().add(line);
    }

    private void drawDottedLine(TVCSPoint start, TVCSPoint end) {
        Line line = new Line();
        line.setStartX(start.x);
        line.setStartY(start.y);
        line.setEndX(end.x);
        line.setEndY(end.y);
        line.getStrokeDashArray().addAll(2d, 3d);
        rootPane.getChildren().add(line);
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
