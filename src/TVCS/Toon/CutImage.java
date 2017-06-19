package TVCS.Toon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by ina on 2017-06-02.
 */
public class CutImage implements Serializable{
    transient Toon parent_toon;
    public transient String path;
    transient public BufferedImage image;

    CutImageInfo cutImageInfo;


    public CutImage(Toon parent_toon, String path) {
        this.parent_toon = parent_toon;
        long id = parent_toon.GenerateID();
        this.path = path + File.separator + id +".jpg";
        cutImageInfo = new CutImageInfo(this.path + "info",
                id, parent_toon.GenerateUpdateID());
    }
    public boolean LoadImage(String image_path) {
        try {
            image = ImageIO.read(new File(image_path));
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Image Load failed");
            return false;
        }
        return true;
    }
    public int Width() {
        return image.getWidth();
    }
    public int Height() {
        return image.getHeight();
    }
    public void Save() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            ImageIO.write(image, "JPG", fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long id() {
        return cutImageInfo.id;
    }

    public void Loadtransient(Toon parent_toon, String path) {
        this.parent_toon = parent_toon;
        this.path = path + File.separator + cutImageInfo.id;
        LoadImage(this.path);
        cutImageInfo.Loadtransient(this.path + "info");
    }
}
