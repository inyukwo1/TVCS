package TVCS.Toon;

/**
 * Created by ina on 2017-06-07.
 */
public class CutImageInfo extends ObjectMetaInfo{
    public long id;
    public long update_id;
    public CutImageInfo(String path, long id, long update_id) {
        super(path);
        this.id = id;
        this.update_id = update_id;
    }
}
