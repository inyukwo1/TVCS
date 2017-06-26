package TVCS.Toon;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ina on 2017-06-04.
 */
public class BranchVertex implements Serializable{
    transient Toon parent_toon;

    boolean is_root;
    long scene_id;

    ArrayList<BranchEdge> edges;
    ArrayList<BranchVertex> parents;

    public BranchVertex (Toon parent_toon, Episode episode) {
        this.parent_toon = parent_toon;
        edges = new ArrayList<BranchEdge>();
        parents = new ArrayList<BranchVertex>();
        if(episode == null) {
            this.is_root = true;
            MakeNewRoot();
        } else {
            this.is_root = false;
            MakeNewVertex(episode);
        }
    }

    public void MakeNewRoot() {
        is_root = true;
    }

    public void MakeNewVertex(Episode episode) {
        scene_id = episode.Id();
    }

    public void AddParent(BranchVertex parent) {
        parents.add(parent);
    }

    public void AddEdgeTo(BranchVertex target_vertex) {
        BranchEdge new_edge = new BranchEdge(parent_toon, this, target_vertex);
        target_vertex.AddParent(this);
        edges.add(new_edge);
    }

    public void DeleteEdge(BranchVertex targetVertex) {
        //TODO can cause performance issue
        for(BranchEdge edge : edges) {
            if(edge.target == targetVertex) {
                edges.remove(edge);
                return;
            }
        }
    }

    public void DeleteParent(BranchVertex sourceVertex) {
        //TODO can cause performance issue
        for(BranchVertex branchVertex : parents) {
            if(branchVertex == sourceVertex) {
                parents.remove(branchVertex);
                return;
            }
        }
    }

    public void LoadtransientRecursively(Toon parent_toon) {
        if(this.parent_toon == parent_toon) {
            return;
        }
        this.parent_toon = parent_toon;
        this.parent_toon.LoadToBranchVertices(this);
        for(BranchEdge edge : edges) {
            edge.LoadtransientRecursively(parent_toon);
        }
    }
}
