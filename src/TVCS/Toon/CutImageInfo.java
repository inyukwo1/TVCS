package TVCS.Toon;

import java.math.BigInteger;

/**
 * Created by ina on 2017-06-07.
 */
public class CutImageInfo extends ObjectMetaInfo {
    public boolean show;
    public CutImageInfo(BigInteger id) {
        super(id);
        show = true;
    }
}
