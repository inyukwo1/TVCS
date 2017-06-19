package TVCS.Toon;

import java.io.Serializable;

/**
 * Created by ina on 2017-06-04.
 */
public class BranchEdge implements Serializable{
    transient Toon parent_toon;

    public BranchVertex source;
    public BranchVertex target;


    public BranchEdge(Toon parent_toon, BranchVertex source, BranchVertex target) {
        this.parent_toon = parent_toon;
        this.source = source;
        this.target = target;
    }
    public void LoadtransientRecursively(Toon parent_toon) {
        if(this.parent_toon == parent_toon) {
            return;
        }
        this.parent_toon = parent_toon;
        target.LoadtransientRecursively(parent_toon);
    }
}
