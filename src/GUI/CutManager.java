package GUI;

import TVCS.Toon.Cut;
import TVCS.Utils.Rectangle;
import TVCS.Utils.ToonPoint;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.concurrent.Semaphore;

/**
 * Created by ina on 2017-06-28.
 */
public class CutManager {
    Cut cut;

    Pane workingPane;
    HBox cutContainer = new HBox();
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
        cutContainer.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.DASHED,
                CornerRadii.EMPTY, BorderWidths.DEFAULT, Insets.EMPTY)));
    }

    public void moveEndPoint(int endX, int endY) {
        dragRectangle.set(startPoint.x, startPoint.y, endX, endY);
        cutContainer.setLayoutX(dragRectangle.leftPos());
        cutContainer.setLayoutY(dragRectangle.topPos());
        cutContainer.setMinSize(dragRectangle.width, dragRectangle.height);
        cutContainer.setMaxSize(dragRectangle.width, dragRectangle.height);
    }

    public void setUp() {
        cut.moveRectangle(dragRectangle);
    }
}
