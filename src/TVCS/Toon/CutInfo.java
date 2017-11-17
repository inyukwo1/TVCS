package TVCS.Toon;

import TVCS.Utils.IntPair;
import TVCS.Utils.Rectangle;

import java.math.BigInteger;

/**
 * Created by ina on 2017-06-07.
 */
public class CutInfo extends ObjectMetaInfo {
    public Rectangle rectangle;
    boolean deleted;
    boolean preserveRatio;
    boolean fitToGrid;
    //below is only used when fitToGrid is true
    IntPair leftTopGrid;
    IntPair rightBottomGrid;
    int gridOffset;

    public CutInfo(BigInteger id, Rectangle rectangle, boolean  deleted){
        super(id);
        this.rectangle = rectangle;
        this.deleted = deleted;
        this.preserveRatio = true;
        this.fitToGrid = false;
        this.gridOffset = 0;
    }
}
