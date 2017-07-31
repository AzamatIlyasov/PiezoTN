package tn.fxgraph.graph;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import tn.fxgraph.layout.base.Layout;
import tn.fxgraph.layout.random.RandomLayout;
import tn.fxgraph.layout.random.SortLayout;
import tn.piezo.model.HydraC;
import tn.piezo.model.HydraDataClassStruct;
import java.util.ArrayList;

public class Graph {

    private Model model;

    private Group canvas;

    private ZoomableScrollPane scrollPane;

    MouseGestures mouseGestures;

    private Scene scene;

    /**
     * the pane wrapper is necessary or else the scrollpane would always align
     * the top-most and left-most child to the top and left eg when you drag the
     * top child down, the entire scrollpane would move down
     */
    CellLayer cellLayer;

    ArrayList hydraDataArrayList;
    private ObservableList<HydraC> hydraData = FXCollections.observableArrayList();

    public Graph() {

        this.model = new Model();

        canvas = new Group();
        cellLayer = new CellLayer();

        canvas.getChildren().add(cellLayer);

        mouseGestures = new MouseGestures(this);

        scrollPane = new ZoomableScrollPane(canvas);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        BorderPane root = new BorderPane();
        root.setCenter(this.getScrollPane());
        scene = new Scene(root, 1024, 600);

        // демонстрационный режим
        // заполняем рандомными элементами
        simGraph();

    }

    public Graph(ArrayList hydraDataArrayList, BorderPane pane) {

        this.hydraDataArrayList = hydraDataArrayList;

        this.model = new Model();

        canvas = new Group();
        cellLayer = new CellLayer();

        canvas.getChildren().add(cellLayer);

        mouseGestures = new MouseGestures(this);

        scrollPane = new ZoomableScrollPane(canvas);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        //BorderPane root = new BorderPane();
        pane.setCenter(this.getScrollPane());
        scene = new Scene(pane, 1024, 600);

        // демонстрационный режим
        // заполняем данными
        simTNGraphM700();

    }

    public ScrollPane getScrollPane() {
        return this.scrollPane;
    }

    public Pane getCellLayer() {
        return this.cellLayer;
    }

    public Model getModel() {
        return model;
    }

    public void beginUpdate() {

    }

    public void endUpdate() {

        // add components to graph pane
        getCellLayer().getChildren().addAll(model.getAddedEdges());
        getCellLayer().getChildren().addAll(model.getAddedCells());

        // remove components from graph pane
        getCellLayer().getChildren().removeAll(model.getRemovedCells());
        getCellLayer().getChildren().removeAll(model.getRemovedEdges());

        // enable dragging of cells
        for (Cell cell : model.getAddedCells()) {
            mouseGestures.makeDraggable(cell);
        }

        // every cell must have a parent, if it doesn't, then the graphParent is
        // the parent
        getModel().attachOrphansToGraphParent(model.getAddedCells());

        // remove reference to graphParent
        getModel().disconnectFromGraphParent(model.getRemovedCells());

        // merge added & removed cells with all cells
        getModel().merge();

    }

    public double getScale() {
        return this.scrollPane.getScaleValue();
    }

    public Scene getScene() {
        return this.scene;
    }

    private void simGraph() {
        // addGraphComponents();
        // -->
        {
            Model model = getModel();

            this.beginUpdate();

            model.addCell("Cell A", CellType.NODE);
            model.addCell("Cell B", CellType.SOURCEWATER);
            model.addCell("Cell C", CellType.TANK);
            model.addCell("Cell D", CellType.PUMP);
            model.addCell("Cell E", CellType.VALVE);
            model.addCell("Cell F", CellType.LABEL);
            model.addCell("Cell G", CellType.PIPE);

            model.addEdge("Cell A", "Cell B");
            model.addEdge("Cell A", "Cell C");
            model.addEdge("Cell B", "Cell C");
            model.addEdge("Cell C", "Cell D");
            model.addEdge("Cell B", "Cell E");
            model.addEdge("Cell D", "Cell F");
            model.addEdge("Cell D", "Cell G");

            this.endUpdate();
        } // <--

        Layout layout = new RandomLayout(this);
        layout.execute();
    }

    private void simTNGraphM700() {
        // addGraphComponents();
        // -->
        {
            Model model = getModel();

            this.beginUpdate();

            //model.addCell("Cell A", CellType.NODE);
            //создаем объект для считывания ГР
            HydraDataClassStruct objHydraDCS;
            // добавляем узлы - колодцы
            objHydraDCS = (HydraDataClassStruct) hydraDataArrayList.get(0);
            model.addCell(objHydraDCS.NamePartTNpred, CellType.NODE, objHydraDCS);
            for (int i = 0; i < hydraDataArrayList.size(); i++) {
                objHydraDCS = (HydraDataClassStruct) hydraDataArrayList.get(i);
                model.addCell(objHydraDCS.NamePartTN, CellType.NODE, objHydraDCS);
            }
            //соединяем их линиями
            for (int i = 0; i < hydraDataArrayList.size(); i++) {
                objHydraDCS = (HydraDataClassStruct) hydraDataArrayList.get(i);
                model.addEdge(objHydraDCS.NamePartTNpred, objHydraDCS.NamePartTN);
            }

            this.endUpdate();
        } // <--

        Layout layout = new SortLayout(this);
        layout.execute();
    }

    public void setHydraData(ObservableList<HydraC> hydraData) {
        this.hydraData = hydraData;
    }

    /**
     * Возвращает данные в виде наблюдаемого списка участков.
     * @return hydraData
     */
    public ObservableList<HydraC> getHydraData() {
        return hydraData;
    }

}