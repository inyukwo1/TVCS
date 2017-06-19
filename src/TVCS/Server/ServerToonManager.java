package TVCS.Server;

import TVCS.Utils.FileManager;

import java.io.File;

/**
 * Created by ina on 2017-06-12.
 */
public class ServerToonManager {
    public static String homePath = "C:\\Users\\ina\\TVCShome";

    public ServerToonManager() {
        File homeDirectory = new File(homePath);
        if(!homeDirectory.exists()) {
            homeDirectory.mkdirs();
        }
    }

    public int AllocateToonId() {
        int allocatedId = FileManager.NumFilesInDirectory(homePath) + 1;
        File newToonDirectory = new File(homePath + File.separator + allocatedId);
        newToonDirectory.mkdir();
        return allocatedId;
    }


}
