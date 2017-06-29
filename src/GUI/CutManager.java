package GUI;

import TVCS.Toon.Cut;
import TVCS.Utils.Rectangle;
import TVCS.Utils.ToonPoint;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;

/**
 * Created by ina on 2017-06-28.
 */
public class CutManager {
    Cut cut;

    Pane workingPane;
    HBox cutContainer = new HBox();
    ImageView imageView = new ImageView();
    ToonPoint startPoint = new ToonPoint(0, 0);
    Rectangle dragRectangle;

    public CutManager(Cut cut, Pane workingPane) {
        this.cut = cut;
        this.workingPane = workingPane;
        workingPane.getChildren().add(cutContainer);
        startPoint.x = cut.cutPoint().x;
        startPoint.y = cut.cutPoint().y;
        dragRectangle = new Rectangle(startPoint.x, startPoint.y, 0, 0);
        cutContainer.setLayoutX(dragRectangle.leftPos());
        cutContainer.setLayoutY(dragRectangle.topPos());
        cutContainer.getChildren().add(imageView);
        imageView.setPreserveRatio(true);
        setDefaultBorder();
    }

    public void moveEndPoint(double endX, double endY) {
        dragRectangle.set(startPoint.x, startPoint.y, endX, endY);
        cutContainer.setLayoutX(dragRectangle.leftPos());
        cutContainer.setLayoutY(dragRectangle.topPos());
        cutContainer.setMinSize(dragRectangle.width, dragRectangle.height);
        cutContainer.setMaxSize(dragRectangle.width, dragRectangle.height);
    }

    public void setUp() {
        cut.moveRectangle(dragRectangle);
        setFileDragDropListener();
    }

    private void setFileDragDropListener() {
        cutContainer.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                if(dragboard.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                    setSelectedBorder();
                } else {
                    event.consume();
                }
            }
        });
        cutContainer.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                setDefaultBorder();
                event.consume();
            }
        });
        cutContainer.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard dragboard = event.getDragboard();
                boolean success = false;
                if (dragboard.hasFiles()) {
                    success = true;
                    for (File file : dragboard.getFiles()) {
                        cut.AddImage(file.getPath());
                    }
                    resetImage();
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }

    private void setDefaultBorder() {
        cutContainer.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED,
                CornerRadii.EMPTY, BorderWidths.DEFAULT, Insets.EMPTY)));
    }

    private void setSelectedBorder() {
        cutContainer.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.DASHED,
                CornerRadii.EMPTY, new BorderWidths(2), Insets.EMPTY)));
    }

    private void resetImage() {
        imageView.setImage(cut.currentImage());
        imageView.setFitHeight(cutContainer.getHeight());
        imageView.setFitWidth(cutContainer.getWidth());
    }
}
