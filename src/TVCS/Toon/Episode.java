package TVCS.Toon;

import TVCS.Utils.FileManager;
import TVCS.Utils.Rectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by ina on 2017-06-02.
 */
public class Episode implements Serializable{
    transient Toon parent_toon;

    public EpisodeInfo episodeInfo;

    public ArrayList<Cut> cuts;


    public Episode(Toon parent_toon, String name, int width, int height) {
        this.parent_toon = parent_toon;
        this.episodeInfo = new EpisodeInfo(name, parent_toon.GenerateID(), width, height);
        this.cuts = new ArrayList<Cut>();
    }

    public boolean MakeNewEpisode() {
        //TODO 이미 있는 scene인지 검사
        return true;
    }

    public Cut AddNewCut(int x, int y, int width, int height) {
        Rectangle new_cut_rect = new Rectangle(x, y, width, height);
        if(!ConfirmAddRect(new_cut_rect)){
            return null;
        }
        Cut new_cut = new Cut(parent_toon, this, new_cut_rect);
        cuts.add(new_cut);
        return new_cut;
    }

    private boolean ConfirmAddRect(Rectangle rectangle){
        for(Cut cut : cuts) {
            if(rectangle.IsOverlappedWith(cut.cutInfo.rectangle)) {
                return false;
            }
        }
        return true;
    }

    public void Export(String export_path) {
        try {
            BufferedImage merged_scene = MergeScene();
            ImageIO.write(merged_scene, "jpg", new File(export_path));
        } catch (IOException e) {
            System.out.println("File Making Failed");
        }
    }

    public long Id(){
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

    public void Loadtransient(Toon parent_toon) {
        this.parent_toon = parent_toon;
        for(Cut cut : cuts) {
            cut.Loadtransient(parent_toon, this);
        }
    }

    public void LinkBranchVertex() {
        episodeInfo.branchVertex = parent_toon.FindBranchVertex(episodeInfo.id);
    }

    public void LinkBranchVertex(BranchVertex branchVertex) {
        episodeInfo.branchVertex = branchVertex;
    }

    public BranchVertex getBranchVertex() {
        return episodeInfo.branchVertex;
    }

    public String sceneDirPath() {
        return parent_toon.toonPath() + File.separator + episodeInfo.name;
    }

    public String sceneInfoPath() {
        return sceneDirPath() + File.separator + episodeInfo.name;
    }

    private BufferedImage MergeScene(){
        BufferedImage merged_scene = new BufferedImage(episodeInfo.width, episodeInfo.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D scene_graphics = (Graphics2D) merged_scene.getGraphics();
        scene_graphics.setBackground(Color.WHITE); //TODO
        for(Cut cut : cuts) {
            cut.DrawLatestImage(scene_graphics);
        }
        return merged_scene;
    }
}
