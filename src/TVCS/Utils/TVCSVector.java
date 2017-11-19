package TVCS.Utils;

/**
 * Created by ina on 2017-11-18.
 */
public class TVCSVector {
    public double x, y;

    public TVCSVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double size() {
        return Math.sqrt(x * x + y * y);
    }

    public void unitize() {
        x /= size();
        y /= size();
    }

    public void makeSize(double size) {
        unitize();
        x *= size;
        y *= size;
    }

    public TVCSVector unitProportionalLeft() {
        return new TVCSVector(-y / size(), x / size());
    }

    public TVCSVector unitProportionalRight() {
        return new TVCSVector(y / size(), -x / size());
    }

}
