package Server;

import TVCS.Utils.FileManager;

import java.io.File;
import java.util.concurrent.Semaphore;

/**
 * Created by ina on 2017-07-05.
 * This class handles server's resource which have to be used atomic.
 * TODO if there are a lot of concurrency access to this class, this class have to be refactored.
 */
public class ServerResourceManager {
    public static String HOME_PATH = "C:\\Users\\ina\\TVCShome";
    public static String SERVER_IP = "localhost";
    public static int SERVER_PORT = 3000;

    private static Semaphore mutex = new Semaphore(1);
    private static long nextToonId;

    static {
        File homeDirectory = new File(ServerResourceManager.HOME_PATH);
        if(!homeDirectory.exists()) {
            homeDirectory.mkdirs();
        }
        //TODO need to improve
        nextToonId = FileManager.NumFilesInDirectory(HOME_PATH) + 1;
    }

    public static long ALLOCATE_TOON_ID() {
        acquireMutex();

        long allocatedId = nextToonId++;

        mutex.release();

        return allocatedId;
    }

    static void acquireMutex() {
        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
