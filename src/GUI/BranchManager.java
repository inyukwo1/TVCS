package GUI;

import TVCS.Toon.Branch;
import TVCS.WorkSpace;
import javafx.geometry.Pos;
import javafx.scene.control.Button;

/**
 * Created by ina on 2017-06-21.
 */
public class BranchManager {
    Branch branch;
    Button showButton;

    public BranchManager(Branch branch) {
        this.branch = branch;
    }

    public Button makeShowButton() {
        showButton = new Button("Show Branch");

        return showButton;
    }
}
