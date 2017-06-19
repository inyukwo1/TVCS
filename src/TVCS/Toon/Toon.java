package TVCS.Toon;

import TVCS.Client.ClientAllocToon;
import TVCS.Client.ClientPushScene;
import TVCS.Utils.FileManager;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by ina on 2017-06-02.
 */
public class Toon {
    String toon_path;

    public ToonInfo toon_info;
    Branch branch;
    ArrayList<ToonScene> loaded_Toon_scenes;

    public Toon(String path, boolean is_new) {
        toon_path = path;
        loaded_Toon_scenes = new ArrayList<ToonScene>();
        if(is_new) {
            MakeNewToon();
        } else {
            LoadToon();
        }
    }

    public long GenerateID() {
        return toon_info.GenerateID();
    }

    public long GenerateUpdateID() {
        return toon_info.GenerateUpdateID();
    }

    public boolean MakeNewToon(){
        toon_info = new ToonInfo(toon_path + File.separator + "tooninfo");
        File toon_directory = new File(toon_path);
        if(toon_directory.exists()) {
            FileManager.DeleteDirectory(toon_directory);
        }
        if(!toon_directory.mkdir()){
            System.out.println("Making Failed");
            return false;
        }
        return MakeToonStructure();
    }
    private boolean MakeToonStructure() {
        branch = new Branch(this);
        return true;
    }

    public String ToonPath() {
        return toon_path;
    }



    public boolean LoadToon() {
        LoadToonInfo();
        LoadBranch();
        return true;
    }

    private void LoadToonInfo() {
        String toon_info_path = toon_path + File.separator + "tooninfo";
        try {
            FileInputStream fileInputStream = new FileInputStream(toon_info_path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            toon_info = (ToonInfo) objectInputStream.readObject();
            fileInputStream.close();
        } catch (IOException e) {
            System.out.println("File couldn't be read");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        toon_info.Loadtransient(toon_info_path);
    }
    private void LoadBranch() {
        try {
            FileInputStream fileInputStream = new FileInputStream(toon_path + File.separator + "branch");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            branch = (Branch) objectInputStream.readObject();
            fileInputStream.close();
        } catch (IOException e) {
            System.out.println("File couldn't be read");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        branch.Loadtransient(this);
    }

    public Branch getBranch() {
        return branch;
    }

    public boolean SaveToon() {
        toon_info.Save();
        branch.Save();
        SaveScenes();
        return true;
    }

    private void SaveScenes() {
        for (ToonScene toonScene : loaded_Toon_scenes) {
            toonScene.Save();
        }
    }

    public ToonScene AddNewScene(String name, int width, int height) {
        ToonScene newToonScene = new ToonScene(this, name, width, height);
        if(!newToonScene.MakeNewScene()){
            System.out.println("Making new scene failed");
            return null;
        }
        loaded_Toon_scenes.add(newToonScene);
        BranchVertex newVertex = branch.AddNewVertex(newToonScene);
        newToonScene.LinkBranchVertex(newVertex);
        return newToonScene;
    }

    public ToonScene LoadScene(String scene_name) {
        ToonScene toonScene = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(toon_path + File.separator + scene_name + File.separator + scene_name);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            toonScene = (ToonScene) objectInputStream.readObject();
            loaded_Toon_scenes.add(toonScene);
            fileInputStream.close();
        } catch (IOException e) {
            System.out.println("File couldn't be read");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        toonScene.Loadtransient(this);
        toonScene.LinkBranchVertex();
        return toonScene;
    }

    public void LoadToBranchVertices(BranchVertex branchVertex) {
        branch.LoadToBranchVertices(branchVertex);
    }

    public BranchVertex FindBranchVertex(long id) {
        return branch.FindBranchVertex(id);
    }

    public int PushAlloc(String ip, int port) {
        ClientAllocToon clientAllocToon = new ClientAllocToon(ip, port, this);
        clientAllocToon.ClientStart();
        return toon_info.toonId = clientAllocToon.getId();
    }

    public void PushScenes(String ip, int port) {
        for(ToonScene scene : loaded_Toon_scenes) {
            ClientPushScene clientPushScene = new ClientPushScene(ip, port, toon_info.toonId, scene);
            clientPushScene.ClientStart();
        }
    }
}
