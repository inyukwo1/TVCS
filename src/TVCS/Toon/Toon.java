package TVCS.Toon;

import GUI.EpisodeTree.EpisodeTreeContent;
import GUI.EpisodeTree.EpisodeTreePane;
import TVCS.Toon.Branch.Branch;
import TVCS.Toon.EpisodeTree.EpisodeTree;
import TVCS.Toon.EpisodeTree.EpisodeVertex;
import TVCS.Toon.EpisodeTree.EpisodeVertexBase;
import TVCS.Utils.DiscreteLocation;
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
    public EpisodeTree episodeTree;
    public ArrayList<Episode> loadedEplisodes;

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
        episodeTree = new EpisodeTree();
        return true;
    }

    public String toonPath() {
        return toon_path;
    }

    public String name() {
        return toon_info.name;
    }

    public long toonId() {
        return toon_info.toonId;
    }

    public void setToonId(long toonId) {
        toon_info.toonId = toonId;
    }

    public EpisodeTree getEpisodeTree() {
        return episodeTree;
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
        episodeTree.save(toonPath());
        SaveEpisodes();
        return true;
    }

    public boolean LoadToon(String path) {
        this.toon_path = path;
        LoadToonInfo();
        episodeTree = EpisodeTree.load(toonPath());
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

    public boolean hasPath() {
        if (toon_path.equals("")) {
            return false;
        }
        return true;
    }

    public Episode AddNewEpisode(String name, DiscreteLocation nextContentLocation, EpisodeTreePane episodeTreePane) {
        Episode newEpisode = new Episode(this, name);
        if(!newEpisode.MakeNewEpisode()){
            System.out.println("Making new episode failed");
            return null;
        }
        loadedEplisodes.add(newEpisode);
        EpisodeVertexBase newEpisodeVertex = episodeTree.addNewVertex(newEpisode, nextContentLocation);
        episodeTreePane.makeAndAddEpisodeTreeContent(newEpisodeVertex);
        return newEpisode;
    }

    public Episode LoadEpisode(EpisodeVertex episodeVertex) {
        Episode episode = Episode.load(this, episodeVertex.episodeInfo);
        loadedEplisodes.add(episode);
        return episode;
    }

    public boolean hasToSave() {
        //TODO
        return true;
    }

    private void SaveEpisodes() {
        for (Episode episode : loadedEplisodes) {
            episode.Save();
        }
    }
}
