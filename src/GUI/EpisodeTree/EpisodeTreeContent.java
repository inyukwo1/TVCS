package GUI.EpisodeTree;

import TVCS.Toon.EpisodeTree.EpisodeTree;
import TVCS.Toon.EpisodeTree.EpisodeVertexBase;
import TVCS.Utils.DiscreteLocation;
import TVCS.WorkSpace;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
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


    EpisodeVertexBase content;
    SetPane setPane = new SetPane();

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

    public EpisodeTreeContent(EpisodeVertexBase content, EpisodeTreePane episodeTreePane) {
        this.content = content;
        container.setMaxHeight(HEIGHT);
        container.setMinHeight(HEIGHT);
        container.setMaxWidth(WIDTH);
        container.setMinWidth(WIDTH);

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

        container.setAlignment(Pos.CENTER);
        thumbnailContainer.setAlignment(Pos.CENTER);
        textContainer.setAlignment(Pos.CENTER);
        container.getChildren().addAll(thumbnailContainer, textContainer);
        setEpisodeName();
        setThumbnail();
        setLocation();
        setContainerEvents(episodeTreePane);
    }

    public DiscreteLocation location() {
        return content.getLocationInParent();
    }

    private void setEpisodeName() {
        Text episodeName = new Text(content.name());
        textContainer.getChildren().add(episodeName);
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

    public void setLocation() {
        container.setLayoutX(EpisodeTreePane.MARGIN + CONTENT_BLOCK_WIDTH() * location().x);
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

    private void setContainerEvents(EpisodeTreePane episodeTreePane) {
        setContainerChangeMouseCursor();
        setContainerDragEvents(episodeTreePane);
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

    private void setContainerDragEvents(EpisodeTreePane episodeTreePane) {
        EpisodeTreeContent episodeTreeContentThis = this;
        container.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    dragging = true;
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
                            episodeTreePane);
                    if (tempMergeContent != mergeContent) {
                        if (mergeContent != null) {
                            //TODO
                        } else {
                            //TODO
                        }
                    }
                    mergeContent = tempMergeContent;
                }
            }
        });
        container.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dragging = false;
                if (mergeContent != null) {
                    // Merge Content
                    if (mergeContent.containsSet()) {
                        // join into the set
                        episodeTreePane.moveIntoSetContent(mergeContent, episodeTreeContentThis);
                    } else {
                        // make a set and merge both
                        episodeTreePane.mergeTwoVertexContents(mergeContent, episodeTreeContentThis);
                    }
                } else {
                    DiscreteLocation toLocation = getDiscreteLocationFromPixelLocation(
                            (int) nodeLocationX + WIDTH / 2, (int) nodeLocationY + HEIGHT / 2);
                    episodeTreePane.moveToLocation(episodeTreeContentThis, toLocation);
                }
                episodeTreePane.contentsSetLocation();
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
}
