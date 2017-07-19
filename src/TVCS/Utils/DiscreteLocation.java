package TVCS.Utils;

import javafx.scene.input.DataFormat;

import java.io.Serializable;

/**
 * Created by ina on 2017-07-16.
 */
public class DiscreteLocation implements Serializable {
    public static DataFormat DISCRETE_LOCATION_DATAFORMAT
            = new DataFormat(DiscreteLocation.class.getCanonicalName());
    public int x, y;
    public DiscreteLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
