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
public class ToonScene implements Serializable{
    transient Toon parent_toon;

    public SceneInfo sceneInfo;

    public ArrayList<Cut> cuts;


    public ToonScene(Toon parent_toon, String name, int width, int height) {
        this.parent_toon = parent_toon;
        this.sceneInfo = new SceneInfo(name, parent_toon.GenerateID(), width, height);
        this.cuts = new ArrayList<Cut>();
    }

    public boolean MakeNewScene() {
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
        return sceneInfo.id;
    }

    public String name() {
        return sceneInfo.name;
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
        sceneInfo.branchVertex = parent_toon.FindBranchVertex(sceneInfo.id);
    }

    public void LinkBranchVertex(BranchVertex branchVertex) {
        sceneInfo.branchVertex = branchVertex;
    }

    public BranchVertex getBranchVertex() {
        return sceneInfo.branchVertex;
    }

    public String sceneDirPath() {
        return parent_toon.toonPath() + File.separator + sceneInfo.name;
    }

    public String sceneInfoPath() {
        return sceneDirPath() + File.separator + sceneInfo.name;
    }

    private BufferedImage MergeScene(){
        BufferedImage merged_scene = new BufferedImage(sceneInfo.width, sceneInfo.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D scene_graphics = (Graphics2D) merged_scene.getGraphics();
        scene_graphics.setBackground(Color.WHITE); //TODO
        for(Cut cut : cuts) {
            cut.DrawLatestImage(scene_graphics);
        }
        return merged_scene;
    }
}
