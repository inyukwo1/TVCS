package TVCS.Toon;

import TVCS.Utils.FileManager;

import java.io.*;
import java.math.BigInteger;

/**
 * Created by ina on 2017-06-07.
 */
public class ObjectMetaInfo implements Serializable {
    public BigInteger id;
    public boolean updated;

    public ObjectMetaInfo(BigInteger id) {
        this.id = id;
        this.updated = false;
    }

    public void Save(String path) {
        FileManager.SaveSerializableObject(this, path);
    }
}
