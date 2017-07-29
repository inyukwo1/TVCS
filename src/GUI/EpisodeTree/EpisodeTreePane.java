package GUI.EpisodeTree;

import TVCS.Toon.EpisodeTree.EpisodeSet;
import TVCS.Toon.EpisodeTree.EpisodeTree;
import TVCS.Toon.EpisodeTree.EpisodeVertex;
import TVCS.Toon.EpisodeTree.EpisodeVertexBase;
import TVCS.Utils.DiscreteLocation;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by ina on 2017-07-13.
 */
public class EpisodeTreePane {
    //TODO refactor re-aligning logic..
    public static int MARGIN = 10;

    int paneWidth = 1000;
    int paneHeight = 500;
    int paneLocationWidth;
    int paneLocationHeight;

    EpisodeTree episodeTree;

    Pane pane = new Pane();
    ArrayList<LinkedList<EpisodeTreeContent>> episodeTreeContents = new ArrayList<>();

    public EpisodeTreePane() {
        pane.setMaxHeight(paneHeight);
        pane.setMinHeight(paneHeight);
        pane.setMinWidth(paneWidth);
        pane.setMaxWidth(paneWidth);
        setPainLocation();
    }

    public void setEpisodeTree(EpisodeTree episodeTree) {
        this.episodeTree = episodeTree;
        constructEpisodeTreeContents();
    }

    public void showPane() {
        for (LinkedList<EpisodeTreeContent> episodeTreeContentLinkedList: episodeTreeContents) {
            for (EpisodeTreeContent episodeTreeContent : episodeTreeContentLinkedList) {
                pane.getChildren().add(episodeTreeContent.container);
            }
        }
    }

    public DiscreteLocation nextContentLocation() {
        // square time complexity. Need improvement?
        int yLocation = 0;
        for (yLocation = 0; yLocation < paneLocationHeight; yLocation++) {
            if (episodeTreeContents.get(yLocation).size() >= paneLocationWidth) {
                continue;
            } else {
                return new DiscreteLocation(episodeTreeContents.get(yLocation).size(), yLocation);
            }
        }
        // Couldn't find empty space
        return null;
    }

    public void makeAndAddEpisodeTreeContent(EpisodeVertexBase episodeVertexBase) {
        EpisodeTreeContent newEpisodeTreeContent = new EpisodeTreeContent(episodeVertexBase, this);
        addContent(newEpisodeTreeContent);
    }

    private void addContent(EpisodeTreeContent content) {
        if (episodeTreeContents.get(content.location().y).size() < content.location().x) {
            episodeTreeContents.get(content.location().y).add(content);
            content.location().x = episodeTreeContents.get(content.location().y).indexOf(content);
        } else {
            episodeTreeContents.get(content.location().y).add(content.location().x, content);
        }
        pane.getChildren().addAll(content.container);
    }

    public void moveToLocation(EpisodeTreeContent selectedContent, DiscreteLocation location) {
        episodeTreeContents.get(selectedContent.location().y).remove(selectedContent);
        if (episodeTreeContents.get(location.y).size() <= location.x) {
            episodeTreeContents.get(location.y).add(selectedContent);
        } else {
            episodeTreeContents.get(location.y).add(location.x, selectedContent);
        }
        selectedContent.location().x = episodeTreeContents.get(location.y).indexOf(selectedContent);
        selectedContent.location().y = location.y;
    }

    public void contentsSetLocation() {
        for (LinkedList<EpisodeTreeContent> episodeTreeContentLinkedList: episodeTreeContents) {
            int i = 0;
            for (EpisodeTreeContent episodeTreeContent : episodeTreeContentLinkedList) {
                episodeTreeContent.location().x = i;
                episodeTreeContent.setLocation();
                i++;
            }
        }
    }

    public void mergeTwoVertexContents(EpisodeTreeContent vertexContent1, EpisodeTreeContent vertexContent2) {
        assert (!vertexContent1.containsSet());
        assert (!vertexContent2.containsSet());
        EpisodeSet newSet = episodeTree.mergeTwoEpisodeVertex(
                (EpisodeVertex) vertexContent1.content, (EpisodeVertex) vertexContent2.content);
        EpisodeTreeContent setContent = new EpisodeTreeContent(newSet, this);
        removeContent(vertexContent1);
        removeContent(vertexContent2);
        setContent.addContent(vertexContent1);
        setContent.addContent(vertexContent2);
        addContent(setContent);
        contentsSetLocation();
    }

    public void moveIntoSetContent(EpisodeTreeContent setContent, EpisodeTreeContent vertexContent) {
        assert (setContent.containsSet());
        removeContent(vertexContent);
        setContent.addContent(vertexContent);
    }

    public void movePivotRight(int pivotX, int pivotY, int moveRight) {
        for (LinkedList<EpisodeTreeContent> episodeTreeContentLinkedList: episodeTreeContents) {
            for (EpisodeTreeContent episodeTreeContent : episodeTreeContentLinkedList) {
                if (episodeTreeContent.location().x > pivotX && episodeTreeContent.location().y <= pivotY) {
                    episodeTreeContent.setMoveToRight(moveRight);
                }
            }
        }
    }

    public void resetMovePivotRight() {
        for (LinkedList<EpisodeTreeContent> episodeTreeContentLinkedList: episodeTreeContents) {
            for (EpisodeTreeContent episodeTreeContent : episodeTreeContentLinkedList) {
                episodeTreeContent.resetMoveToRight();
            }
        }
    }

    private void removeContent(EpisodeTreeContent content) {
        episodeTreeContents.get(content.location().y).remove(content);
        pane.getChildren().remove(content.container);
    }

    private void constructEpisodeTreeContents() {
        for (EpisodeVertexBase episodeVertexBase: episodeTree.firstLevelVertices()) {
            episodeTreeContents.get(episodeVertexBase.getLocationInParent().y).
                    add(episodeVertexBase.getLocationInParent().x, new EpisodeTreeContent(episodeVertexBase, this));
        }
    }

    private void setPainLocation() {
        paneLocationWidth = paneWidth / (EpisodeTreeContent.WIDTH + 2 * MARGIN);
        paneLocationHeight = paneHeight / (EpisodeTreeContent.HEIGHT + 2 * MARGIN);
        for (int i = 0 ; i < paneLocationHeight; i++) {
            episodeTreeContents.add(new LinkedList<>());
        }
    }
}
