package TVCS.Toon;

import TVCS.Utils.FileManager;
import TVCS.Utils.Rectangle;
import javafx.scene.paint.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by ina on 2017-06-02.
 */
public class Episode implements Serializable {
    //TODO 최근 Push / Pull 후 변동사항이 있는가?
    //TODO 최근 Save 이후로 변동사항이 있는가?
    public static int DEFAULT_WIDTH = 500;
    public static int MIN_WIDTH = 100;
    public static int MAX_WIDTH = 1000;
    public static int DEFAULT_HEIGHT = 3000;
    public static int MIN_HEIGHT = 500;
    public static int MAX_HEIGHT = 10000;
    public static int DEFAULT_GRID = 50;
    public static int MIN_GRID = 10;
    public static int MAX_GRID = 500;

    transient Toon parentToon;

    // This is saved with EpisodeTree
    public transient EpisodeInfo episodeInfo;

    public ArrayList<Cut> cuts;

    public Episode(Toon parentToon, String name) {
        this.parentToon = parentToon;
        this.episodeInfo = new EpisodeInfo(name, parentToon.generateID(), DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.cuts = new ArrayList<Cut>();
        updated();
    }

    public boolean MakeNewEpisode() {
        //TODO 이미 있는 episode인지 검사
        return true;
    }

    public void resizeWidth(int width) {
        episodeInfo.width = width;
    }

    public void resizeHeight(int height) {
        episodeInfo.height = height;
    }

    public Cut AddNewCut(double x, double y, int width, int height) {
        Rectangle newCutRect = new Rectangle(x, y, width, height);
        if(!ConfirmAddRect(newCutRect)){
            return null;
        }
        Cut newCut = new Cut(parentToon, this, newCutRect);
        cuts.add(newCut);
        return newCut;
    }

    private boolean ConfirmAddRect(Rectangle rectangle){
        for(Cut cut : cuts) {
            if(rectangle.IsOverlappedWith(cut.cutInfo.rectangle)) {
                return false;
            }
        }
        return true;
    }

    public void extract(String export_path, String formatName) {
        try {
            BufferedImage merged_scene = MergeScene();
            ImageIO.write(merged_scene, formatName, new File(export_path));
        } catch (IOException e) {
            System.out.println("File Making Failed");
        }
    }

    public BigInteger Id(){
        return episodeInfo.id;
    }

    public String name() {
        return episodeInfo.name;
    }

    public int getWidth() {
        return episodeInfo.width;
    }

    public int getHeight() {
        return episodeInfo.height;
    }

    public int getGridSize() {
        return episodeInfo.gridSize;
    }

    public void setGridSize(int gridSize) {
        episodeInfo.gridSize = gridSize;
    }

    public javafx.scene.paint.Color getBackgroundColor() {
        return episodeInfo.backgroundColor;
    }

    public int numCuts() {
        return cuts.size();
    }

    public void Save() {
        FileManager.MakeDirectory(sceneDirPath());
        FileManager.SaveSerializableObject(this, sceneInfoPath());
        for(Cut cut : cuts) {
            cut.Save();
        }
    }

    static public Episode load(Toon toon, EpisodeInfo episodeInfo) {
        String name = episodeInfo.name;
        Episode episode = (Episode) FileManager.LoadSerializableObject
                (toon.toonPath() + File.separator + name + File.separator + name);
        if (episode == null) {
            // Episode does not exist in disk
            return null;
        }
        episode.episodeInfo = episodeInfo;
        episode.Loadtransient(toon);
        return episode;
    }

    private void Loadtransient(Toon parent_toon) {
        this.parentToon = parent_toon;
        for(Cut cut : cuts) {
            cut.Loadtransient(parent_toon, this);
        }
    }

    public String sceneDirPath() {
        return parentToon.toonPath() + File.separator + episodeInfo.name;
    }

    public String sceneInfoPath() {
        return sceneDirPath() + File.separator + episodeInfo.name;
    }

    public boolean hasToSave() {
        //TODO
        return false;
    }

    public void setBackgroundColor(javafx.scene.paint.Color color) {
        episodeInfo.backgroundColor = color;
    }

    private BufferedImage MergeScene() {
        BufferedImage mergedScene = new BufferedImage(episodeInfo.width, episodeInfo.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D sceneGraphics = mergedScene.createGraphics();
        sceneGraphics.setPaint(getAwtBackgroundColor());
        sceneGraphics.fillRect(0, 0, episodeInfo.width, episodeInfo.height);
        for(Cut cut : cuts) {
            cut.DrawLatestImage(sceneGraphics);
        }
        return mergedScene;
    }

    private Color getAwtBackgroundColor() {
        Color awtColor = new java.awt.Color((float) episodeInfo.backgroundColor.getRed(),
                (float) episodeInfo.backgroundColor.getGreen(),
                (float) episodeInfo.backgroundColor.getBlue(),
                (float) episodeInfo.backgroundColor.getOpacity());
        return awtColor;
    }

    public void updated() {
        episodeInfo.updated = true;
        parentToon.updated();
    }
}
