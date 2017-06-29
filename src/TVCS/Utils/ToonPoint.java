package TVCS.Utils;

import java.io.Serializable;

/**
 * Created by ina on 2017-06-05.
 */
public class ToonPoint implements Serializable {
    public double x, y;
    public ToonPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
