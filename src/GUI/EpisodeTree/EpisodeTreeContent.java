package GUI.EpisodeTree;

import GUI.ToonManager;
import TVCS.Toon.Episode;
import TVCS.Toon.EpisodeTree.EpisodeVertex;
import TVCS.Toon.EpisodeTree.EpisodeVertexBase;
import TVCS.Toon.Toon;
import TVCS.Utils.DiscreteLocation;
import TVCS.WorkSpace;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Created by ina on 2017-07-13.
 */
public class EpisodeTreeContent{
    public static int WIDTH = 100;
    public static int HEIGHT = 120;

    private static int TEXT_HEIGHT = 20;
    private static int THUMBNAIL_HEIGHT = 100;

    EpisodeTreePane parentTreePane;
    EpisodeVertexBase content;

    VBox container = new VBox();
    VBox thumbnailContainer = new VBox();
    VBox textContainer = new VBox();

    // Used in drag events
    private double nodeLocationX = 0;
    private double nodeLocationY = 0;
    private double mouseLocationX = 0;
    private double mouseLocationY = 0;

    boolean dragging = false;
    EpisodeTreeContent mergeContent;

    // When set selected
    int moveToRight = 0;

    // Used in set
    boolean setSelected = false;
    SetPane setPane = new SetPane();

    public EpisodeTreeContent(EpisodeVertexBase content, EpisodeTreePane parentTreePane) {
        this.content = content;
        this.parentTreePane = parentTreePane;
        container.setMaxHeight(HEIGHT);
        container.setMinHeight(HEIGHT);
        container.setMaxWidth(WIDTH);
        container.setMinWidth(WIDTH);
        container.setAlignment(Pos.CENTER);

        thumbnailContainer.setMaxHeight(THUMBNAIL_HEIGHT);
        thumbnailContainer.setMinHeight(THUMBNAIL_HEIGHT);
        thumbnailContainer.setMaxWidth(WIDTH);
        thumbnailContainer.setMinWidth(WIDTH);

        thumbnailContainer.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3), new Insets(5))));

        textContainer.setMaxHeight(TEXT_HEIGHT);
        textContainer.setMinHeight(TEXT_HEIGHT);
        textContainer.setMaxWidth(WIDTH);
        textContainer.setMinWidth(WIDTH);


        thumbnailContainer.setAlignment(Pos.CENTER);
        textContainer.setAlignment(Pos.CENTER);


        setEpisodeName();
        setThumbnail();
        attachThumbnailAndText();
        setLocation();
        setContainerEvents();
    }

    public DiscreteLocation location() {
        return content.getLocationInParent();
    }

    private void setThumbnail() {
        ImageView imageView = new ImageView();
        imageView.setImage(content.thumbnail());
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        thumbnailContainer.getChildren().add(imageView);
    }

    private void setEpisodeName() {
        Text episodeName = new Text(content.name());
        textContainer.getChildren().add(episodeName);
    }

    private void attachThumbnailAndText() {
        container.getChildren().addAll(thumbnailContainer, textContainer);
    }

    private void detachThumbnailAndText() {
        container.getChildren().removeAll(thumbnailContainer, textContainer);
    }

    public void setLocation() {
        container.setLayoutX(EpisodeTreePane.MARGIN + CONTENT_BLOCK_WIDTH() * (location().x + moveToRight));
        container.setLayoutY(EpisodeTreePane.MARGIN + CONTENT_BLOCK_HEIGHT() * location().y);
    }

    public static int CONTENT_BLOCK_WIDTH() {
        return EpisodeTreePane.MARGIN * 2 + WIDTH;
    }

    public static int CONTENT_BLOCK_HEIGHT() {
        return EpisodeTreePane.MARGIN * 2 + HEIGHT;
    }

    public boolean containsSet() {
        return content.isSet();
    }

    public void addContent(EpisodeTreeContent content) {
        assert (containsSet());
        setPane.addContent(content);
    }

    public void setMoveToRight(int moveToRight) {
        this.moveToRight = moveToRight;
    }

    public void resetMoveToRight() {
        this.moveToRight = 0;
    }

    private void setContainerEvents() {
        setContainerChangeMouseCursor();
        setContainerDragEvents();
    }

    private void setContainerChangeMouseCursor() {
        container.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                WorkSpace.primaryStage.getScene().setCursor(Cursor.HAND);
            }
        });
        container.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                WorkSpace.primaryStage.getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }

    private void setContainerDragEvents() {
        EpisodeTreeContent episodeTreeContentThis = this;
        container.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (dragging) {
                    dragging = false;
                    return;
                }
                if (containsSet()){
                    //open set
                    //TODO span되면서 parentPane 사이즈도 키우기
                    //TODO set 안에서 set 만들 때 NullpointerException
                    if (!setSelected) {
                        setSelected = true;
                        container.setMaxWidth(setPane.paneWidth() * CONTENT_BLOCK_WIDTH());
                        container.setMinWidth(setPane.paneWidth() * CONTENT_BLOCK_WIDTH());
                        container.setMaxHeight(setPane.paneHeight() * CONTENT_BLOCK_HEIGHT());
                        container.setMinHeight(setPane.paneHeight() * CONTENT_BLOCK_HEIGHT());
                        container.setBorder(new Border(new BorderStroke(Color.BLACK,
                                BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3), new Insets(5))));
                        setPane.setPaneSize();
                        detachThumbnailAndText();
                        container.getChildren().add(setPane.pane);
                        parentTreePane.movePivotRight(location().x, location().y, setPane.paneWidth() - 1);
                        parentTreePane.contentsSetLocation();
                    } else {
                        setSelected = false;
                        container.getChildren().remove(setPane.pane);
                        container.setMaxHeight(HEIGHT);
                        container.setMinHeight(HEIGHT);
                        container.setMaxWidth(WIDTH);
                        container.setMinWidth(WIDTH);
                        container.setBorder(Border.EMPTY);
                        attachThumbnailAndText();
                        parentTreePane.resetMovePivotRight();
                        parentTreePane.contentsSetLocation();
                    }
                } else {
                    // Open Episode
                    ToonManager currentToonManager = WorkSpace.mainApp.toonManager;
                    Toon currentToon = currentToonManager.toon;
                    for (Episode episode: currentToon.loadedEplisodes) {
                        if (matchesEpisode(episode)) {
                            // Episode already loaded
                            currentToonManager.selectTab(episode);
                            afterClicked();
                            event.consume();
                            return;
                        }
                    }
                    // Episode didn't loaded already
                    if (currentToonManager.loadEpisode((EpisodeVertex) content)) {
                        // loaded well
                        afterClicked();
                        event.consume();
                        return;
                    }
                    // TODO if there is not episode in disk, find it in server
                }
                afterClicked();
                event.consume();
            }

            private void afterClicked() {
                dragging = false;
                mergeContent = null;
            }
        });
        container.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (setSelected) {
                    return;
                }
                if (event.getButton() == MouseButton.PRIMARY) {
                    mouseLocationX = event.getSceneX();
                    mouseLocationY = event.getSceneY();

                    nodeLocationX = container.getLayoutX();
                    nodeLocationY = container.getLayoutY();
                }
            }
        });
        container.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dragging = true;
                if (setSelected) {
                    return;
                }
                if (event.getButton() == MouseButton.PRIMARY) {
                    double deltaX = event.getSceneX() - mouseLocationX;
                    double deltaY = event.getSceneY() - mouseLocationY;
                    nodeLocationX += deltaX;
                    nodeLocationY += deltaY;
                    container.setLayoutX(nodeLocationX);
                    container.setLayoutY(nodeLocationY);
                    mouseLocationX = event.getSceneX();
                    mouseLocationY = event.getSceneY();
                    EpisodeTreeContent tempMergeContent = getMergeContent(
                            (int) nodeLocationX + WIDTH / 2, (int) nodeLocationY + HEIGHT / 2,
                            parentTreePane);
                    if (tempMergeContent != mergeContent) {
                        if (mergeContent != null) {
                            //TODO animation
                        } else {
                            //TODO animation
                        }
                    }
                    mergeContent = tempMergeContent;
                }
            }
        });
        container.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (setSelected) {
                    return;
                }
                if (mergeContent != null && mergeContent != episodeTreeContentThis) {
                    // Merge Content
                    if (mergeContent.containsSet()) {
                        // join into the set
                        parentTreePane.moveIntoSetContent(mergeContent, episodeTreeContentThis);
                    } else {
                        // make a set and merge both
                        parentTreePane.mergeTwoVertexContents(mergeContent, episodeTreeContentThis);
                    }
                } else if (mergeContent != episodeTreeContentThis) {
                    DiscreteLocation toLocation = getDiscreteLocationFromPixelLocation(
                            (int) nodeLocationX + WIDTH / 2, (int) nodeLocationY + HEIGHT / 2);
                    parentTreePane.moveToLocation(episodeTreeContentThis, toLocation);
                }
                parentTreePane.contentsSetLocation();
                mergeContent = null;
                event.consume();
            }
        });
    }

    private EpisodeTreeContent getMergeContent(int locX, int locY, EpisodeTreePane episodeTreePane) {
        if (locX < 0 || locY < 0) {
            return null;
        }
        int discreteX = locX / CONTENT_BLOCK_WIDTH();
        int discreteY = locY / CONTENT_BLOCK_HEIGHT();
        if (episodeTreePane.episodeTreeContents.size() <= discreteY) {
            return null;
        }
        if (locX % CONTENT_BLOCK_WIDTH() <= EpisodeTreePane.MARGIN * 2 ||
                locX % CONTENT_BLOCK_WIDTH() > CONTENT_BLOCK_WIDTH() - EpisodeTreePane.MARGIN * 2) {
            return null;
        }
        if (episodeTreePane.episodeTreeContents.get(discreteY).size() <= discreteX) {
            return null;
        }
        return episodeTreePane.episodeTreeContents.get(discreteY).get(discreteX);
    }

    private DiscreteLocation getDiscreteLocationFromPixelLocation(int locX, int locY) {
        int discreteX = (locX -  CONTENT_BLOCK_WIDTH() / 2) / CONTENT_BLOCK_WIDTH() ;
        int discreteY = locY / CONTENT_BLOCK_HEIGHT();

        discreteX = discreteX <= 0 ? 0 : discreteX;
        discreteY = discreteY <= 0 ? 0 : discreteY;
        return new DiscreteLocation(discreteX, discreteY);
    }

    private boolean matchesEpisode(Episode episode) {
        // EpisodeVertex and corresponding Episode has the same episodeInfo
        if (((EpisodeVertex) content).episodeInfo == episode.episodeInfo) {
            return true;
        }
        return false;
    }


}
