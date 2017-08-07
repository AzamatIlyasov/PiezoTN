package tn.fxgraph.cells;

import javafx.scene.image.ImageView;
import tn.fxgraph.graph.Cell;

// трубопровод
public class PumpCell extends Cell {

    public PumpCell(String id) {
        super(id);

        ImageView view = new ImageView("resources/images/tngraph/pump1.png");
        view.setFitWidth(10);
        view.setFitHeight(10);

        setView(view);

    }

}