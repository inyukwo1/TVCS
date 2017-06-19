package TVCS.Toon;

import TVCS.Utils.Rectangle;

/**
 * Created by ina on 2017-06-07.
 */
public class CutInfo extends ObjectMetaInfo {
    long id;
    public Rectangle rectangle;
    boolean deleted;

    public CutInfo(String path, long id, Rectangle rectangle, boolean  deleted){
        super(path);
        this.id = id;
        this.rectangle = rectangle;
        this.deleted = deleted;
    }
}
