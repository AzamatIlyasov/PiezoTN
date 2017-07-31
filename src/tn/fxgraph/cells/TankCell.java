package tn.fxgraph.cells;

import javafx.scene.image.ImageView;
import tn.fxgraph.graph.Cell;

// накопитель воды
public class TankCell extends Cell {

    public TankCell(String id) {
        super(id);

        ImageView view = new ImageView("resources/images/tngraph/tank1.png");
        view.setFitWidth(10);
        view.setFitHeight(10);

        setView(view);

    }

}