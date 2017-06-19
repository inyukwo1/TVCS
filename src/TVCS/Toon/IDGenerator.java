package TVCS.Toon;

import java.io.Serializable;

/**
 * Created by ina on 2017-06-04.
 */
public class IDGenerator implements Serializable{
    long current_id;
    long generate_id() {
        current_id++;
        return current_id;
    }
    public IDGenerator() {
        current_id = 0;
    }
    public IDGenerator(long id) {
        current_id = id;
    }
}
