package tn.fxgraph.cells;

import tn.fxgraph.graph.Cell;
import javafx.scene.image.ImageView;

// узел тепловой сети
// *тепловой колодец
public class NodeCell extends Cell {

    public NodeCell(String id) {
        super(id);

        ImageView view = new ImageView("resources/images/tngraph/tk1.png");
        view.setAccessibleText("azamat");
        view.setFitWidth(25);
        view.setFitHeight(25);

        setView(view);

    }

}