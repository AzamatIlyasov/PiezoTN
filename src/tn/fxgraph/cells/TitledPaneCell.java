package tn.fxgraph.cells;

import javafx.scene.control.TitledPane;
import tn.fxgraph.graph.Cell;

public class TitledPaneCell extends Cell {

    public TitledPaneCell(String id) {
        super(id);

        TitledPane view = new TitledPane();
        view.setPrefSize(100, 80);

        setView(view);

    }

}