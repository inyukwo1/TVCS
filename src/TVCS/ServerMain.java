package TVCS;

import GUI.PushManager;
import Server.ServerOpen;
import Server.ServerResourceManager;

/**
 * Created by ina on 2017-05-31.
 */
public class ServerMain {
    public static void main (String [] args) throws Exception {
        new ServerOpen(ServerResourceManager.SERVER_PORT);
    }
}
