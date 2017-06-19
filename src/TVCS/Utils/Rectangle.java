package TVCS.Utils;

import java.awt.geom.AffineTransform;
import java.io.Serializable;

/**
 * Created by ina on 2017-06-05.
 */
public class Rectangle implements Serializable {
    public Point coord;
    public int width, height;

    public Rectangle(int x, int y, int width, int height) {
        this.coord = new Point(x, y);
        this.width = width;
        this.height = height;
    }
    public Point LeftTopCoord() {
        Point left_top_coord = new Point(coord.x, coord.y);
        return left_top_coord;
    }
    public Point LeftBottomCoord() {
        Point left_bottom_coord = new Point(coord.x, coord.y + height -1);
        return left_bottom_coord;
    }
    public Point RightTopCoord() {
        Point right_top_coord = new Point(coord.x + width -1, coord.y);
        return right_top_coord;
    }
    public Point RightBottomCoord() {
        Point right_bottom_coord = new Point(coord.x + width -1, coord.y + height -1);
        return right_bottom_coord;
    }
    public boolean ContainsPoint(Point point) {
        if(coord.x <= point.x && point.x < coord.x + width
                && coord.y <= point.y && point.y <= coord.y + height) {
            return true;
        }
        return false;
    }
    public boolean IsOverlappedWith(Rectangle rect) {
        if(ContainsPoint(rect.LeftTopCoord()) || ContainsPoint(rect.LeftBottomCoord())
                || ContainsPoint(rect.RightTopCoord()) || ContainsPoint(rect.RightBottomCoord())) {
            return true;
        }
        return false;
    }
}
