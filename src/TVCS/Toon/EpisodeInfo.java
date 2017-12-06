package TVCS.Toon;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.math.BigInteger;

/**
 * Created by ina on 2017-06-07.
 */
public class EpisodeInfo extends ObjectMetaInfo {
    public String name;
    public int width, height;
    public int gridSize;
    transient public Color backgroundColor;
    private double backgroundRed, backgroundGreen, backgroundBlue, backgroundOpacity;

    public Image thumbnail; //TODO 저장, 로드, push, pull 등
    public Pair<Integer, Integer> episodeTreePaneLocation;

    public EpisodeInfo(String name, BigInteger id, int width, int height) {
        super(id);
        this.name = name;
        this.width = width;
        this.height = height;
        this.gridSize = Episode.DEFAULT_GRID;
        this.backgroundColor = Color.WHITE;
        this.backgroundRed = this.backgroundColor.getRed();
        this.backgroundGreen = this.backgroundColor.getGreen();
        this.backgroundBlue = this.backgroundColor.getBlue();
        this.backgroundOpacity = this.backgroundColor.getOpacity();
    }

    public void setBackgroundColor() {
        this.backgroundColor = new Color(this.backgroundRed, this.backgroundGreen, this.backgroundBlue, this.backgroundOpacity);
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        this.backgroundRed = this.backgroundColor.getRed();
        this.backgroundGreen = this.backgroundColor.getGreen();
        this.backgroundBlue = this.backgroundColor.getBlue();
        this.backgroundOpacity = this.backgroundColor.getOpacity();
    }
}
