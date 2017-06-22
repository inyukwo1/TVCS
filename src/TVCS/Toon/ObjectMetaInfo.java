package TVCS.Toon;

import TVCS.Utils.FileManager;

import java.io.*;

/**
 * Created by ina on 2017-06-07.
 */
public class ObjectMetaInfo implements Serializable {

    public void Save(String path) {
        FileManager.SaveSerializableObject(this, path);
    }

}
