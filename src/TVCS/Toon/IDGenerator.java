package TVCS.Toon;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by ina on 2017-06-04.
 */
public class IDGenerator implements Serializable {
    public static BigInteger BORROWING_SIZE = new BigInteger("10000000000000");
    public static BigInteger TO_BORROW_SIZE = new BigInteger("5000000000000");

    BigInteger currentId = new BigInteger("0");
    BigInteger toId = new BigInteger("99999999999999999999");

    BigInteger generateId() {
        BigInteger newId = currentId.add(BigInteger.valueOf(1));
        currentId = currentId.add(BigInteger.valueOf(1));
        return newId;
    }

    public boolean hasToUpdate() {
        if (currentId.equals(BigInteger.ZERO)) {
            return true;
        }

        if (toId.subtract(currentId).max(TO_BORROW_SIZE).equals(TO_BORROW_SIZE)) {
            return true;
        }
        return false;
    }

    public void borrow(BigInteger borrowedNum) {
        currentId = borrowedNum;
        toId = currentId.add(BORROWING_SIZE);
    }
}
