package TVCS.Toon;

import TVCS.Utils.FileManager;
import TVCS.Utils.Rectangle;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by ina on 2017-06-02.
 */
public class Cut implements Serializable{
    transient Toon parentToon;
    transient Episode parentEpisode;

    CutInfo cutInfo;

    public ArrayList<CutImage> images;

    public Cut(Toon parentToon, Episode parentEpisode, Rectangle rectangle) {
        this.parentToon = parentToon;
        this.parentEpisode = parentEpisode;
        this.cutInfo = new CutInfo(parentToon.generateID(), rectangle, false);
        this.images = new ArrayList<>();
        updated();
    }

    public void moveRectangle(Rectangle rectangle) {
        cutInfo.rectangle = rectangle;
    }

    public Rectangle cutRectangle() {
        return cutInfo.rectangle;
    }

    public boolean AddImage(String image_path) {
        CutImage new_image = new CutImage(parentToon, parentEpisode, this);
        if(!new_image.LoadImage(image_path)) {
            return false;
        }
        images.add(new_image);
        updated();
        return true;
    }

    public void DrawLatestImage(Graphics2D scene_graphics) {
        if(images.size() == 0) {
            return;
        }
        CutImage latest_image = images.get(images.size()-1);
        scene_graphics.drawImage(latest_image.image, GetAffineTransform(latest_image), null);
    }
    public AffineTransform GetAffineTransform(CutImage image) {
        double m00 = (double) cutInfo.rectangle.width / (double)image.Width();
        double m10 = (double) 0;
        double m01 = (double) 0;
        double m11 = (double) cutInfo.rectangle.height / (double)image.Height();
        double m02 = (double) cutInfo.rectangle.LeftTopCoord().x;
        double m12 = (double) cutInfo.rectangle.LeftTopCoord().y;

        return new AffineTransform(m00, m10, m01, m11, m02, m12);
    }

    public BigInteger id() {
        return cutInfo.id;
    }

    public int imageNum() {
        return images.size();
    }

    public void Save() {
        FileManager.MakeDirectory(cutDirPath());
        for(CutImage image : images) {
            image.Save();
        }
    }

    public void Loadtransient(Toon parentToon, Episode parentScene) {
        this.parentToon = parentToon;
        this.parentEpisode = parentScene;
        for(CutImage image: images) {
            image.Loadtransient(parentToon, parentScene, this);
        }
    }

    public String cutDirPath() {
        return parentEpisode.sceneDirPath() + File.separator + cutInfo.id;
    }

    public boolean hasImage() {
        return images.size() > 0;
    }

    public Image currentImage() {
        return images.get(images.size() - 1).fxImage();
    }

    public void updated() {
        cutInfo.updated = true;
        parentEpisode.updated();
    }

    public boolean preserveRatio() {
        return cutInfo.preserveRatio;
    }

    public void changePreserveRatio() {
        cutInfo.preserveRatio = !cutInfo.preserveRatio;
    }
}
