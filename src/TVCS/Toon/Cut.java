package TVCS.Toon;

import TVCS.Utils.FileManager;
import TVCS.Utils.ImageUtils;
import TVCS.Utils.IntPair;
import TVCS.Utils.Rectangle;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by ina on 2017-06-02.
 */
public class Cut implements Serializable{
    public static int GRID_FIT_MIN_OFFSET = 0;


    transient Toon parentToon;
    transient Episode parentEpisode;

    CutInfo cutInfo;

    public ArrayList<CutImage> images;
    transient public BufferedImage showingImage;

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
        makeShowingImage();
        updated();
        return true;
    }

    public void DrawLatestImage(Graphics2D scene_graphics) {
        if(images.size() == 0) {
            return;
        }
        scene_graphics.drawImage(showingImage, GetAffineTransform(showingImage), null);
    }

    private AffineTransform GetAffineTransform(BufferedImage image) {
        double m00, m10, m01, m11, m02, m12;
        if (!cutInfo.preserveRatio) {
            m00 = (double) cutInfo.rectangle.width / (double) image.getWidth();
            m10 = (double) 0;
            m01 = (double) 0;
            m11 = (double) cutInfo.rectangle.height / (double) image.getHeight();
            m02 = (double) cutInfo.rectangle.LeftTopCoord().x;
            m12 = (double) cutInfo.rectangle.LeftTopCoord().y;
        } else {
            double widthRatio = cutInfo.rectangle.width / (double) image.getWidth();
            double heightRatio = cutInfo.rectangle.height / (double) image.getHeight();
            double ratio = widthRatio > heightRatio ? heightRatio : widthRatio;
            m00 = (double) ratio;
            m10 = (double) 0;
            m01 = (double) 0;
            m11 = (double) ratio;
            m02 = (double) cutInfo.rectangle.LeftTopCoord().x;
            m12 = (double) cutInfo.rectangle.LeftTopCoord().y;
        }

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
        makeShowingImage();
    }

    public String cutDirPath() {
        return parentEpisode.sceneDirPath() + File.separator + cutInfo.id;
    }

    public boolean hasImage() {
        return images.size() > 0;
    }

    public Image currentImage() {
        makeShowingImage();
        return ImageUtils.BufferedImageToFxImage(showingImage);
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

    public int parentGridSize() {
        return parentEpisode.getGridSize();
    }

    public boolean getFitToGrid() {
        return cutInfo.fitToGrid;
    }

    public int getGridOffset() {
        return cutInfo.gridOffset;
    }

    public void fitToGrid(IntPair leftTopGrid, IntPair rightBottomGrid) {
        cutInfo.fitToGrid = true;
        cutInfo.leftTopGrid = leftTopGrid;
        cutInfo.rightBottomGrid = rightBottomGrid;
        syncRectangle();
    }

    public void unfitToGrid() {
        cutInfo.fitToGrid = false;
    }

    public void setGridOffset(int gridOffset) {
        cutInfo.gridOffset = gridOffset;
        syncRectangle();
    }

    public void parentGridSizeChanged() {
        syncRectangle();
    }

    private void syncRectangle() {
        int myOffset = cutInfo.gridOffset;
        if (cutInfo.gridOffset > parentGridSize()) {
            myOffset = parentGridSize();
        }
        cutInfo.rectangle.coord.x = parentEpisode.getGridSize() * cutInfo.leftTopGrid.first + myOffset;
        cutInfo.rectangle.coord.y = parentEpisode.getGridSize() * cutInfo.leftTopGrid.second + myOffset;
        cutInfo.rectangle.width =
                parentEpisode.getGridSize() * (cutInfo.rightBottomGrid.first - cutInfo.leftTopGrid.first + 1) - 2 * myOffset;
        cutInfo.rectangle.height =
                parentEpisode.getGridSize() * (cutInfo.rightBottomGrid.second - cutInfo.leftTopGrid.second + 1) - 2 * myOffset;
    }

    private void makeShowingImage() {
        ArrayList<BufferedImage> imagesToMerge = new ArrayList<>();
        for(CutImage cutImage: images) {
            if (cutImage.show()) {
                BufferedImage imagesToPush = ImageUtils.ResizeBufferedImage(cutImage.image, (int) cutInfo.rectangle.width, (int) cutInfo.rectangle.height, preserveRatio());
                imagesToMerge.add(imagesToPush);
            }
        }
        showingImage = ImageUtils.MergeBufferedImages((int) cutInfo.rectangle.width, (int) cutInfo.rectangle.height, imagesToMerge);
    }
}
