package tn.fxgraph.cells;

import javafx.scene.image.ImageView;
import tn.fxgraph.graph.Cell;

public class ImageCell extends Cell {

    public ImageCell(String id) {
        super(id);

        ImageView view = new ImageView("http://upload.wikimedia.org/wikipedia/commons/thumb/4/41/Siberischer_tiger_de_edit02.jpg/800px-Siberischer_tiger_de_edit02.jpg");
        view.setFitWidth(10);
        view.setFitHeight(10);

        setView(view);

    }

}