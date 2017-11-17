package TVCS.Toon;

import TVCS.Utils.ImageUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;

import static org.opencv.core.CvType.CV_8UC;

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

        cutImageInfo = new CutImageInfo(parentToon.generateID());
        updated();
    }

    public boolean LoadImage(String image_path) {
        try {
            BufferedImage loadedimage = ImageIO.read(new File(image_path));
            image = new BufferedImage(loadedimage.getWidth(), loadedimage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            image.getGraphics().drawImage(loadedimage, 0, 0, null);
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Image Load failed");
            return false;
        }
        return true;
    }

    public boolean show() {
        return cutImageInfo.show;
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

    public BigInteger id() {
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

    /*public Image testttt() {
        //BufferedImage image = new BufferedImage(this.image.getWidth(), this.image.getHeight(), BufferedImage.TYPE_INT_ARGB);
       // image.getGraphics().drawImage(this.image, 0, 0, null);

        Mat mat = ImageUtils.BufferedImageToMat(image);
        BufferedImage image2 = ImageUtils.MatToBufferedImage(mat);
        return SwingFXUtils.toFXImage(image2, null);
    }*/

    public Image fxImage() {
        return SwingFXUtils.toFXImage(image, null);
    }

    public Image alphaRepresentingImage() {
        BufferedImage alphaRepresentingImage = ImageUtils.AlphaRepresentingImage(image);
        return SwingFXUtils.toFXImage(alphaRepresentingImage, null);
    }

    public void updated() {
        cutImageInfo.updated = true;
        parentCut.updated();
    }
}
