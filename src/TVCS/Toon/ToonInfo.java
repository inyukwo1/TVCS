package TVCS.Toon;

import java.io.*;

/**
 * Created by ina on 2017-06-06.
 */
public class ToonInfo extends ObjectMetaInfo{

    int toonId; //if -1, it means that id wasn't allocated.
    IDGenerator id_generator;
    IDGenerator update_id_generator;

    public long GenerateID() {
        return id_generator.generate_id();
    }
    public long GenerateUpdateID() {
        return update_id_generator.generate_id();
    }
    public ToonInfo(String path) {
        super(path);
        toonId = -1;
        id_generator = new IDGenerator();
        update_id_generator = new IDGenerator();
    }

}
