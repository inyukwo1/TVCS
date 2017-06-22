package TVCS.Toon;

import TVCS.Utils.Rectangle;

/**
 * Created by ina on 2017-06-07.
 */
public class CutInfo extends ObjectMetaInfo {
    long id;
    public Rectangle rectangle;
    boolean deleted;

    public CutInfo(long id, Rectangle rectangle, boolean  deleted){
        this.id = id;
        this.rectangle = rectangle;
        this.deleted = deleted;
    }
}
