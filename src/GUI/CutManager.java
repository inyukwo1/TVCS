package GUI;

import TVCS.Toon.Cut;
import TVCS.WorkSpace;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;

/**
 * Created by ina on 2017-06-28.
 */
public class CutManager {
    static int BORDER_WIDTH = 1;
    EpisodeManager parentEpisodeManager;
    Cut cut;

    HBox cutContainer = new HBox();
    ImageView imageView = new ImageView();

    double locX, locY, width, height;

    // resize related variables
    boolean resizing = false;
    EventHandler<MouseEvent> mouseMoveHandler;
    EventHandler<MouseEvent> mouseExitHandler;
    EventHandler<MouseEvent> mousePressHandler;
    EventHandler<MouseEvent> resizeDragHandler;
    EventHandler<MouseEvent> resizeReleaseHandler;
    // move related variables
    double mouseX, mouseY;
    EventHandler<MouseEvent> moveDragHandler;
    EventHandler<MouseEvent> moveReleaseHandler;

    public CutManager(Cut cut, EpisodeManager parentEpisodeManager) {
        this.parentEpisodeManager = parentEpisodeManager;
        parentEpisodeManager.workPane.getChildren().add(cutContainer);
        this.cut = cut;
        cutContainer.setLayoutX(cut.cutRectangle().leftPos());
        cutContainer.setLayoutY(cut.cutRectangle().topPos());
        locX = cut.cutRectangle().leftPos();
        locY = cut.cutRectangle().topPos();
        cutContainer.getChildren().add(imageView);
        imageView.setPreserveRatio(cut.preserveRatio());
        setDefaultBorder();
        setHandler();
    }

    public void moveEndPoint(double endX, double endY) {
        cut.cutRectangle().set(locX, locY, endX, endY);
        width = endX - locX;
        height = endY - locY;
        syncContainerLoc();
        resetImage();
    }

    public void moveSize(double deltaWidth, double deltaHeight) {
        width += deltaWidth;
        height += deltaHeight;
        cut.cutRectangle().setWithSize(locX, locY, width, height);
        syncContainerLoc();
        resetImage();
    }

    public void move(double x, double y) {
        locX += x;
        locY += y;
        cutContainer.setLayoutX(locX);
        cutContainer.setLayoutY(locY);
    }

    public void setUp() {
        syncContainerLoc();
        resetImage();
        setFileDragDropListener();
        setSelectCutListener(parentEpisodeManager);
    }

    private void syncContainerLoc() {
        cutContainer.setLayoutX(cut.cutRectangle().leftPos());
        cutContainer.setLayoutY(cut.cutRectangle().topPos());
        cutContainer.setMinSize(cut.cutRectangle().width, cut.cutRectangle().height);
        cutContainer.setMaxSize(cut.cutRectangle().width, cut.cutRectangle().height);
    }

    public void unselect() {
        setDefaultBorder();
        cutContainer.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMoveHandler);
        cutContainer.removeEventHandler(MouseEvent.MOUSE_EXITED, mouseExitHandler);
        cutContainer.removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressHandler);
    }

    public void select() {
        cutContainer.addEventHandler(MouseEvent.MOUSE_MOVED, mouseMoveHandler);
        cutContainer.addEventHandler(MouseEvent.MOUSE_EXITED,mouseExitHandler);
        cutContainer.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressHandler);
    }

    public Button makePreserveRatioButton() {
        Button fitContainerButton = new Button("Preserve Ratio");
        if (cut.preserveRatio()) {
            fitContainerButton.setText("Not Preserve Ratio");
        }
        fitContainerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cut.changePreserveRatio();
                if (cut.preserveRatio()) {
                    fitContainerButton.setText("Not Preserve Ratio");
                    imageView.setPreserveRatio(true);
                } else {
                    fitContainerButton.setText("Preserve Ratio");
                    imageView.setPreserveRatio(false);
                }
                cut.updated(); //  TODO 두번 눌리면 updated 해제
            }
        });
        return fitContainerButton;
    }

    private void setSelectCutListener(EpisodeManager parentEpisodeManager) {
        CutManager thisCutManager = this;
        cutContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (resizing) {
                    event.consume();
                    return;
                }
                parentEpisodeManager.unselect();
                parentEpisodeManager.selectCut(thisCutManager);
                setSelectedBorder();
                event.consume();
            }
        });
    }

    private void setFileDragDropListener() {
        cutContainer.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                parentEpisodeManager.unselect();
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
                CornerRadii.EMPTY, new BorderWidths(BORDER_WIDTH), Insets.EMPTY)));
    }

    private void setSelectedBorder() {
        cutContainer.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.DASHED,
                CornerRadii.EMPTY, new BorderWidths(BORDER_WIDTH), Insets.EMPTY)));
    }

    private void resetImage() {
        if (!cut.hasImage()) {
            return;
        }
        imageView.setImage(cut.currentImage());
        imageView.setFitHeight(cut.cutRectangle().height - 2 * BORDER_WIDTH);
        imageView.setFitWidth(cut.cutRectangle().width - 2 * BORDER_WIDTH);
    }

    private void setHandler() {
        CutManager thisCutManager = this;
        mouseMoveHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (cut.cutRectangle().width - event.getX() < 10 && cut.cutRectangle().height - event.getY() < 10) {
                    WorkSpace.primaryStage.getScene().setCursor(Cursor.SE_RESIZE);
                    resizing = true;
                } else {
                    WorkSpace.primaryStage.getScene().setCursor(Cursor.DEFAULT);
                    resizing = false;
                }
            }
        };
        mouseExitHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                WorkSpace.primaryStage.getScene().setCursor(Cursor.DEFAULT);
                resizing = false;
            }
        };
        mousePressHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseX = event.getScreenX();
                mouseY = event.getScreenY();
                if (resizing) {
                    cutContainer.addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeDragHandler);
                    cutContainer.addEventHandler(MouseEvent.MOUSE_RELEASED, resizeReleaseHandler);
                } else {
                    cutContainer.addEventHandler(MouseEvent.MOUSE_DRAGGED, moveDragHandler);
                    cutContainer.addEventHandler(MouseEvent.MOUSE_RELEASED, moveReleaseHandler);
                }
            }
        };
        resizeDragHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                thisCutManager.moveSize(event.getScreenX() - mouseX, event.getScreenY() - mouseY);
                mouseX = event.getScreenX();
                mouseY = event.getScreenY();
            }
        };
        resizeReleaseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cutContainer.removeEventHandler(MouseEvent.MOUSE_DRAGGED, resizeDragHandler);
                cutContainer.removeEventHandler(MouseEvent.MOUSE_RELEASED, resizeReleaseHandler);
                cut.updated();
            }
        };
        moveDragHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                thisCutManager.move(event.getScreenX() - mouseX, event.getScreenY() - mouseY);
                mouseX = event.getScreenX();
                mouseY = event.getScreenY();
            }
        };
        moveReleaseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                cutContainer.removeEventHandler(MouseEvent.MOUSE_DRAGGED, moveDragHandler);
                cutContainer.removeEventHandler(MouseEvent.MOUSE_RELEASED, moveReleaseHandler);
                cut.updated();
            }
        };
    }
}
