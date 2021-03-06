package TVCS.Utils;

import java.io.Serializable;

/**
 * Created by ina on 2017-06-05.
 */
public class Rectangle implements Serializable {
    public TVCSPoint coord;
    public double width;
    public double height;

    public Rectangle(double x, double y, double width, double height) {
        this.coord = new TVCSPoint(x, y);
        this.width = width;
        this.height = height;
    }

    public double leftPos() {
        return coord.x;
    }

    public double topPos() {
        return coord.y;
    }

    public TVCSPoint LeftTopCoord() {
        TVCSPoint left_top_coord = new TVCSPoint(coord.x, coord.y);
        return left_top_coord;
    }

    public TVCSPoint LeftBottomCoord() {
        TVCSPoint left_bottom_coord = new TVCSPoint(coord.x, coord.y + height -1);
        return left_bottom_coord;
    }

    public TVCSPoint RightTopCoord() {
        TVCSPoint right_top_coord = new TVCSPoint(coord.x + width -1, coord.y);
        return right_top_coord;
    }

    public TVCSPoint RightBottomCoord() {
        TVCSPoint right_bottom_coord = new TVCSPoint(coord.x + width -1, coord.y + height -1);
        return right_bottom_coord;
    }

    public boolean ContainsPoint(TVCSPoint TVCSPoint) {
        if(coord.x <= TVCSPoint.x && TVCSPoint.x < coord.x + width
                && coord.y <= TVCSPoint.y && TVCSPoint.y <= coord.y + height) {
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

    public void set(double startX, double startY, double endX, double endY) {
        if (startX >= endX) {
            coord.x = endX;
            width = startX - endX;
        } else {
            coord.x = startX;
            width = endX - startX;
        }
        if (startY >= endY) {
            coord.y = endY;
            height = startY - endY;
        } else {
            coord.y = startY;
            height = endY - startY;
        }
    }

    public void setEndPoint(double endX, double endY) {
        if (endX >= coord.x && endY >= coord.y) {
            width = endX - coord.x;
            height = endY - coord.y;
        } else if (endX >= coord.x && endY < coord.y) {
            width = endX - coord.x;
            height = coord.y - endY;
            coord.y = endY;
        } else if (endX < coord.x && endY >= coord.y) {
            width = coord.x - endX;
            height = endY - coord.y;
            coord.x = endX;
        } else {
            width = coord.x - endX;
            height = coord.y - endY;
            coord.x = endX;
            coord.y = endY;
        }
    }

    public void setWithSize(double startX, double startY, double width, double height) {
        if (width >= 0 && height >= 0) {
            this.width = width;
            this.height = height;
            this.coord.x = startX;
            this.coord.y = startY;
        } else if (width < 0 && height >= 0) {
            this.width = -width;
            this.height = height;
            this.coord.x = startX + width;
            this.coord.y = startY;
        } else if (width >= 0 && height < 0) {
            this.width = width;
            this.height = -height;
            this.coord.x = startX;
            this.coord.y = startY + height;
        } else {
            this.width = -width;
            this.height = -height;
            this.coord.x = startX + width;
            this.coord.y = startY + height;
        }
    }
}
