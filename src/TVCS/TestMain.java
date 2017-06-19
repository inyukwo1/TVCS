package TVCS;

import TVCS.Toon.BranchVertex;
import TVCS.Toon.Cut;
import TVCS.Toon.ToonScene;
import TVCS.Toon.Toon;

/**
 * Created by ina on 2017-06-04.
 */
public class TestMain {
    public static void ToonTest1() {
        Toon new_toon = new Toon("C:\\Users\\ina\\ToonTest", true);
        ToonScene new_Toon_scene = new_toon.AddNewScene("scene1", 400, 800);
        Cut new_cut = new_Toon_scene.AddNewCut(10, 10, 100, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(20, 220, 300, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(10, 500, 100, 100);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_Toon_scene.Export("C:\\Users\\ina\\ToonTest\\TestScene1.jpg");
    }
    public static void ToonTest2() {
        Toon new_toon = new Toon("C:\\Users\\ina\\ToonTest", true);
        ToonScene new_Toon_scene = new_toon.AddNewScene("scene1", 400, 800);
        Cut new_cut = new_Toon_scene.AddNewCut(10, 10, 100, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(20, 220, 300, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(10, 500, 100, 100);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_toon.SaveToon();
        Toon new_toon2 = new Toon ("C:\\Users\\ina\\ToonTest", false);
        new_toon2.LoadScene("scene1").Export("C:\\Users\\ina\\ToonTest\\TestScene1.jpg");
    }
    public static void ToonTest3() {
        Toon new_toon = new Toon("C:\\Users\\ina\\ToonTest", true);
        ToonScene new_Toon_scene = new_toon.AddNewScene("scene1", 400, 800);
        Cut new_cut = new_Toon_scene.AddNewCut(10, 10, 100, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(20, 220, 300, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(10, 500, 100, 100);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_toon.SaveToon();
        Toon new_toon2 = new Toon ("C:\\Users\\ina\\ToonTest", false);
        ToonScene new_Toon_scene2 = new_toon2.LoadScene("scene1");
        new_cut = new_Toon_scene2.AddNewCut(10, 700, 100, 100);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_Toon_scene2.Export("C:\\Users\\ina\\ToonTest\\TestScene1.jpg");
    }
    public static void ToonTest4() {
        Toon new_toon = new Toon("C:\\Users\\ina\\ToonTest", true);
        ToonScene new_Toon_scene = new_toon.AddNewScene("scene1", 400, 800);
        Cut new_cut = new_Toon_scene.AddNewCut(10, 10, 100, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(20, 220, 300, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(10, 500, 100, 100);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");

        ToonScene new_Toon_scene2 =  new_toon.AddNewScene("scene2", 400, 800);
        new_cut = new_Toon_scene2.AddNewCut(10, 700, 100, 100);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_Toon_scene.Export("C:\\Users\\ina\\ToonTest\\TestScene1.jpg");
        new_Toon_scene2.Export("C:\\Users\\ina\\ToonTest\\TestScene2.jpg");
    }
    public static void ToonTest5() {
        Toon new_toon = new Toon("C:\\Users\\ina\\ToonTest", true);
        ToonScene new_Toon_scene = new_toon.AddNewScene("scene1", 400, 800);
        Cut new_cut = new_Toon_scene.AddNewCut(10, 10, 100, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(20, 220, 300, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(10, 500, 100, 100);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");

        ToonScene new_Toon_scene2 =  new_toon.AddNewScene("scene2", 400, 800);
        new_cut = new_Toon_scene2.AddNewCut(10, 700, 100, 100);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_toon.SaveToon();
        Toon new_toon2 = new Toon ("C:\\Users\\ina\\ToonTest", false);

        new_toon2.LoadScene("scene1").Export("C:\\Users\\ina\\ToonTest\\TestScene1.jpg");
        new_toon2.LoadScene("scene2").Export("C:\\Users\\ina\\ToonTest\\TestScene2.jpg");
    }
    public static void ToonTest6() {
        Toon new_toon = new Toon("C:\\Users\\ina\\ToonTest", true);
        ToonScene new_Toon_scene = new_toon.AddNewScene("scene1", 400, 800);
        Cut new_cut = new_Toon_scene.AddNewCut(10, 10, 100, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(20, 220, 300, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(10, 500, 100, 100);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");

        ToonScene new_Toon_scene2 =  new_toon.AddNewScene("scene2", 400, 800);
        new_cut = new_Toon_scene2.AddNewCut(10, 700, 100, 100);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_toon.SaveToon();
        Toon new_toon2 = new Toon ("C:\\Users\\ina\\ToonTest", false);

        BranchVertex scene1vertex= new_toon2.LoadScene("scene1").getBranchVertex();
        BranchVertex scene2vertex = new_toon2.LoadScene("scene2").getBranchVertex();

        new_toon2.getBranch().AddNewEdge(scene1vertex,scene2vertex);
    }
    public static void ToonTestAlloc() {
        Toon new_toon = new Toon("C:\\Users\\ina\\ToonTest", true);
        ToonScene new_Toon_scene = new_toon.AddNewScene("scene1", 400, 800);
        Cut new_cut = new_Toon_scene.AddNewCut(10, 10, 100, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(20, 220, 300, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(10, 500, 100, 100);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");

        ToonScene new_Toon_scene2 =  new_toon.AddNewScene("scene2", 400, 800);
        new_cut = new_Toon_scene2.AddNewCut(10, 700, 100, 100);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_toon.SaveToon();
        new_toon.PushAlloc("localhost", 3000);
    }
    public static void ToonTestPush() {
        Toon new_toon = new Toon("C:\\Users\\ina\\ToonTest", true);
        ToonScene new_Toon_scene = new_toon.AddNewScene("scene1", 400, 800);
        Cut new_cut = new_Toon_scene.AddNewCut(10, 10, 100, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(20, 220, 300, 200);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_cut = new_Toon_scene.AddNewCut(10, 500, 100, 100);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");

        ToonScene new_Toon_scene2 =  new_toon.AddNewScene("scene2", 400, 800);
        new_cut = new_Toon_scene2.AddNewCut(10, 700, 100, 100);
        new_cut.AddImage("C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
        new_toon.SaveToon();
        new_toon.PushAlloc("localhost", 3000);
        new_toon.PushScenes("localhost", 3000);
    }
    public static void main (String [] args) throws Exception {
        ToonTestPush();
    }
}
