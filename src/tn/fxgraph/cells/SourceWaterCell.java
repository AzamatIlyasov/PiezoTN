package tn.fxgraph.cells;

import javafx.scene.image.ImageView;
import tn.fxgraph.graph.Cell;

// источник воды - резервуар
public class SourceWaterCell extends Cell {

    public SourceWaterCell(String id) {
        super(id);

        ImageView view = new ImageView("resources/images/tngraph/sourcewater1.png");
        view.setFitWidth(10);
        view.setFitHeight(10);

        setView(view);

    }

}