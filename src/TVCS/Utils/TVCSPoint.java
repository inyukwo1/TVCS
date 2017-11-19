package TVCS.Utils;

import java.io.Serializable;

/**
 * Created by ina on 2017-06-05.
 */
public class TVCSPoint implements Serializable {
    public double x, y;

    public TVCSPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void plusVector(TVCSVector vector) {
        x += vector.x;
        y += vector.y;
    }

    public void plusVector(TVCSVector unitvector, double size) {
        TVCSVector tempVector = new TVCSVector(unitvector.x, unitvector.y);
        tempVector.makeSize(size);
        plusVector(tempVector);
    }
}
