package TVCS.Toon;

/**
 * Created by ina on 2017-06-07.
 */
public class CutImageInfo extends ObjectMetaInfo{
    public long id;
    public long update_id;
    public CutImageInfo(long id, long update_id) {
        this.id = id;
        this.update_id = update_id;
    }
}
