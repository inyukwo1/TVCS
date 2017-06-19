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
    transient Toon parent_toon;
    transient String path;

    CutInfo cutInfo;

    public ArrayList<CutImage> images;

    public Cut(Toon parent_toon, Rectangle rectangle, String path) {
        this.parent_toon = parent_toon;
        long id = parent_toon.GenerateID();
        this.path = path + File.separator + id;
        this.cutInfo = new CutInfo(this.path + File.separator + "cutinfo",
                id , rectangle, false);
        this.images = new ArrayList<CutImage>();
    }

    public boolean AddImage(String image_path) {
        CutImage new_image = new CutImage(parent_toon, path);
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
        File cut_directory = new File(path);
        if(cut_directory.exists()) {
            FileManager.DeleteDirectory(cut_directory);
        }
        cut_directory.mkdir();
        for(CutImage image : images) {
            image.Save();
        }
    }
    public void Loadtransient(Toon parent_toon, String scene_path) {
        this.parent_toon = parent_toon;
        this.path = scene_path + File.separator + cutInfo.id;
        cutInfo.Loadtransient(this.path + File.separator + "cutinfo");
        for(CutImage image: images) {
            image.Loadtransient(parent_toon, this.path);
        }
    }

}
