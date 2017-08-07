package tn.fxgraph.graph;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import tn.piezo.model.HydraC;
import tn.piezo.model.HydraDataClassStruct;

import java.util.ArrayList;
import java.util.List;

public class Cell extends Pane {

    String cellId;
    HydraDataClassStruct hydraDataClassStruct;

    List<Cell> children = new ArrayList<>();
    List<Cell> parents = new ArrayList<>();

    Node view;

    public Cell(String cellId) {
        this.cellId = cellId;
    }

    public void addCellChild(Cell cell) {
        children.add(cell);
    }

    public List<Cell> getCellChildren() {
        return children;
    }

    public void addCellParent(Cell cell) {
        parents.add(cell);
    }

    public List<Cell> getCellParents() {
        return parents;
    }

    public void removeCellChild(Cell cell) {
        children.remove(cell);
    }

    public void setView(Node view) {

        this.view = view;
        getChildren().add(view);

    }

    public Node getView() {
        return this.view;
    }

    public String getCellId() {
        return cellId;
    }

    public void setHydraDataClassStruct(HydraDataClassStruct hydraDataClassStruct) {
        this.hydraDataClassStruct = hydraDataClassStruct;
    }

    public ObservableList<HydraC> getHydraData() {
        ObservableList<HydraC> hydraData = FXCollections.observableArrayList();
            hydraData.add(new HydraC(hydraDataClassStruct.NamePartTN,
                    hydraDataClassStruct.D, hydraDataClassStruct.L, hydraDataClassStruct.G,
                    hydraDataClassStruct.Geo, hydraDataClassStruct.ZdanieEtaj,
                    hydraDataClassStruct.dH_fist, hydraDataClassStruct.Hrasp_endP));
        return hydraData;
    }
}