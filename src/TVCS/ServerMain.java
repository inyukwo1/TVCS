package TVCS;

import TVCS.Server.ServerOpen;

/**
 * Created by ina on 2017-05-31.
 */
public class ServerMain {
    public static void main (String [] args) throws Exception {
        new ServerOpen(3000);
    }
}
