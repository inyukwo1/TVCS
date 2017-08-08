package TVCS.Toon;

import java.math.BigInteger;

/**
 * Created by ina on 2017-06-06.
 */
public class ToonInfo extends ObjectMetaInfo {

    long toonId; //if -1, it means that id wasn't allocated.
    String name;
    IDGenerator idGenerator;

    public BigInteger generateID() {
        return idGenerator.generateId();
    }

    public ToonInfo() {
        super(BigInteger.ZERO);
        toonId = -1;
        idGenerator = new IDGenerator();
    }
}
