package TVCS.Utils;

import java.io.Serializable;

/**
 * Created by ina on 2017-06-05.
 */
public class ToonPoint implements Serializable {
    public int x, y;
    public ToonPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
