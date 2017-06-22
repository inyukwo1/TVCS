package TVCS.Toon;

import TVCS.Utils.FileManager;
import TVCS.Utils.Rectangle;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ina on 2017-06-02.
 */
public class Cut implements Serializable{
    transient Toon parentToon;
    transient ToonScene parentScene;

    CutInfo cutInfo;

    public ArrayList<CutImage> images;

    public Cut(Toon parentToon, ToonScene parentScene, Rectangle rectangle) {
        this.parentToon = parentToon;
        this.parentScene = parentScene;
        long id = parentToon.GenerateID();
        this.cutInfo = new CutInfo(id , rectangle, false);
        this.images = new ArrayList<>();
    }

    public boolean AddImage(String image_path) {
        CutImage new_image = new CutImage(parentToon, parentScene, this);
        if(!new_image.LoadImage(image_path)) {
            return false;
        }
        images.add(new_image);
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

    public long id() {
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
    public void Loadtransient(Toon parentToon, ToonScene parentScene) {
        this.parentToon = parentToon;
        this.parentScene = parentScene;
        for(CutImage image: images) {
            image.Loadtransient(parentToon, parentScene, this);
        }
    }

    public String cutDirPath() {
        return parentScene.sceneDirPath() + File.separator + cutInfo.id;
    }

}
