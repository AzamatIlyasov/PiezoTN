package tn.fxgraph.layout.random;

import tn.fxgraph.layout.base.Layout;
import tn.fxgraph.graph.Cell;
import tn.fxgraph.graph.Graph;

import java.util.List;

public class SortLayout extends Layout {

    Graph graph;
    double h,w,x,y;

    //Random rnd = new Random();

    public SortLayout(Graph graph) {

        this.graph = graph;

        h = //graph.getScene().getHeight();
        graph.getCellLayer().getHeight();
        w = //graph.getScene().getWidth();
        graph.getCellLayer().getWidth();
        x = 10;
        y = 10;

    }

    public void execute() {

        List<Cell> cells = graph.getModel().getAllCells();

        for (Cell cell : cells) {
            x += 40;
            y += 10;
            cell.relocate(x, y);
        }

    }

}