package TVCS.Toon;

import TVCS.Utils.FileManager;

import java.io.*;

/**
 * Created by ina on 2017-06-07.
 */
public class ObjectMetaInfo implements Serializable {
    public transient String path;

    protected ObjectMetaInfo(String path){
        this.path = path;
    }

    public void Save() {
        FileManager.SaveSerializableObject(this, path);
    }

    public void Loadtransient(String path) {
        this.path = path;
    }
}
