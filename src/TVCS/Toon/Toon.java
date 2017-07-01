package TVCS.Toon;

import TVCS.Client.ClientAllocToon;
import TVCS.Client.ClientPushEpisode;
import TVCS.Utils.FileManager;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by ina on 2017-06-02.
 */
public class Toon {
    //TODO Push 하기 전에는 무조건 저장하고 Push가능.
    String toon_path;

    public ToonInfo toon_info;
    Branch branch;
    ArrayList<Episode> loadedEplisodes;

    //used when create new toon
    public Toon(String name) {
        this.toon_path = "";
        loadedEplisodes = new ArrayList<Episode>();
        MakeNewToon();
        this.toon_info.name = name;
    }

    //used when loading
    public Toon() {
        loadedEplisodes = new ArrayList<Episode>();
    }

    public long GenerateID() {
        return toon_info.GenerateID();
    }

    public long GenerateUpdateID() {
        return toon_info.GenerateUpdateID();
    }

    public boolean MakeNewToon(){
        toon_info = new ToonInfo();
        return MakeToonStructure();
    }

    private boolean MakeToonStructure() {
        branch = new Branch(this);
        return true;
    }

    public String toonPath() {
        return toon_path;
    }

    public String name() {
        return toon_info.name;
    }

    public boolean LoadToon(String path) {
        this.toon_path = path;
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

    public boolean SaveToon(String path) {
        this.toon_path = path;
        return SaveToon();
    }

    public boolean SaveToon() {
        if (this.toon_path.equals("")) {
            return false;
        }
        FileManager.MakeDirectory(toon_path);
        toon_info.Save(toon_path + File.separator + "tooninfo");
        branch.Save();
        SaveEpisodes();
        return true;
    }

    public boolean hasPath() {
        if (toon_path.equals("")) {
            return false;
        }
        return true;
    }

    public Episode AddNewEpisode(String name, int width, int height) {
        Episode newEpisode = new Episode(this, name, width, height);
        if(!newEpisode.MakeNewEpisode()){
            System.out.println("Making new episode failed");
            return null;
        }
        loadedEplisodes.add(newEpisode);
        BranchVertex newVertex = branch.AddNewVertex(newEpisode);
        newEpisode.LinkBranchVertex(newVertex);
        return newEpisode;
    }

    public Episode LoadEpisode(String episodeName) {
        Episode episode = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(toon_path + File.separator + episodeName + File.separator + episodeName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            episode = (Episode) objectInputStream.readObject();
            loadedEplisodes.add(episode);
            fileInputStream.close();
        } catch (IOException e) {
            System.out.println("File couldn't be read");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        episode.Loadtransient(this);
        episode.LinkBranchVertex();
        return episode;
    }

    public void LoadToBranchVertices(BranchVertex branchVertex) {
        branch.LoadToBranchVertices(branchVertex);
    }

    public BranchVertex FindBranchVertex(long id) {
        return branch.FindBranchVertex(id);
    }

    public void pushAll(String ip, int port) {
        if(pushedBefore()) {
            PushAlloc(ip, port);
        }
        PushEpisodes(ip, port);
    }

    public int PushAlloc(String ip, int port) {
        ClientAllocToon clientAllocToon = new ClientAllocToon(ip, port, this);
        clientAllocToon.ClientStart();
        toon_info.toonId = clientAllocToon.getId();
        toon_info.Save(toon_path + File.separator + "tooninfo");
        return toon_info.toonId;
    }

    public void PushEpisodes(String ip, int port) {
        for(Episode episode : loadedEplisodes) {
            pushEpisode(ip, port, episode);
        }
    }

    public void pushEpisode(String ip, int port, Episode episode) {
        ClientPushEpisode clientPushEpisode = new ClientPushEpisode(ip, port, toon_info.toonId, episode);
        clientPushEpisode.ClientStart();
    }

    public boolean pushedBefore() {
        return toon_info.toonId != -1;
    }

    public boolean hasToSave() {
        //TODO
        return false;
    }

    private void SaveEpisodes() {
        for (Episode episode : loadedEplisodes) {
            episode.Save();
        }
    }
}
