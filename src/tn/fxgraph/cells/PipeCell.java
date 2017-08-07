package tn.fxgraph.cells;

import javafx.scene.image.ImageView;
import tn.fxgraph.graph.Cell;

// трубопровод
public class PipeCell extends Cell {

    public PipeCell(String id) {
        super(id);

        ImageView view = new ImageView("resources/images/tngraph/pipe1.jpg");
        view.setFitWidth(10);
        view.setFitHeight(10);

        setView(view);

    }

}