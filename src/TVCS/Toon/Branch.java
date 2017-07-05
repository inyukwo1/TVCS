package TVCS.Toon;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by ina on 2017-06-02.
 */
public class Branch implements Serializable{
    transient Toon parent_toon;
    transient ArrayList <BranchVertex> branchVertices;

    BranchVertex root;

    public Branch(Toon parent_toon) {
        this.parent_toon = parent_toon;
        branchVertices = new ArrayList<>();
        MakeNewBranch();
    }

    public BranchVertex AddNewVertex(Episode new_Toon_scene) {
        BranchVertex new_vertex = new BranchVertex(parent_toon, new_Toon_scene);
        LoadToBranchVertices(new_vertex);
        root.AddEdgeTo(new_vertex);
        return new_vertex;
    }

    public void AddNewEdge(BranchVertex sourceVertex, BranchVertex targetVertex) {
        sourceVertex.AddEdgeTo(targetVertex);
        DeleteEdge(root, targetVertex);
    }

    public void DeleteEdge(BranchVertex sourceVertex, BranchVertex targetVertex) {
        sourceVertex.DeleteEdge(targetVertex);
        targetVertex.DeleteParent(sourceVertex);
    }

    public void MakeNewBranch() {
        root = new BranchVertex(parent_toon, null);
    }

    public void Save() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(branchPath());
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);

            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Loadtransient(Toon parent_toon) {
        this.parent_toon = parent_toon;
        this.branchVertices = new ArrayList<>();
        root.LoadtransientRecursively(this.parent_toon);
    }

    public void LoadToBranchVertices(BranchVertex branchVertex) {
        branchVertices.add(branchVertex);
    }

    public BranchVertex FindBranchVertex(long id) {
        for(BranchVertex branchVertex : branchVertices) {
            if(id == branchVertex.scene_id) {
                return branchVertex;
            }
        }
        return null;
    }

    public String branchPath() {
        return parent_toon.toonPath() + File.separator + "branch";
    }
}
