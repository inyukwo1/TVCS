package TVCS.Toon;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by ina on 2017-06-02.
 */
public class CutImage implements Serializable {
    transient Toon parentToon;
    transient Episode parentScene;
    transient Cut parentCut;

    transient public BufferedImage image;

    CutImageInfo cutImageInfo;


    public CutImage(Toon parentToon, Episode parentScene, Cut parentCut) {
        this.parentToon = parentToon;
        this.parentScene = parentScene;
        this.parentCut = parentCut;


        long id = parentToon.GenerateID();
        cutImageInfo = new CutImageInfo(id, parentToon.GenerateUpdateID());
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
            FileOutputStream fileOutputStream = new FileOutputStream(cutImagePath());
            ImageIO.write(image, "PNG", fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long id() {
        return cutImageInfo.id;
    }

    public void Loadtransient(Toon parentToon, Episode parentScene, Cut parentCut) {
        this.parentToon = parentToon;
        this.parentScene = parentScene;
        this.parentCut = parentCut;
        LoadImage(cutImagePath());
    }

    public String cutImagePath() {
        return parentCut.cutDirPath() + File.separator + cutImageInfo.id + ".png";
    }

    public String cutImageInfoPath() {
        return cutImagePath() + "info";
    }

    public Image fxImage() {
        return SwingFXUtils.toFXImage(image, null);
    }
}
