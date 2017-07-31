package tn.fxgraph.cells;

import javafx.scene.image.ImageView;
import tn.fxgraph.graph.Cell;

// задвижка
public class ValveCell extends Cell {

    public ValveCell(String id) {
        super(id);

        ImageView view = new ImageView("resources/images/tngraph/valve1.png");
        view.setFitWidth(10);
        view.setFitHeight(10);

        setView(view);

    }

}