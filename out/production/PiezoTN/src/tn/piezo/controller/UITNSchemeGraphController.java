package tn.piezo.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import tn.piezo.model.HydraC;
import tn.fxgraph.graph.Graph;

import java.util.ArrayList;

/**
 * Created by djaza on 16.02.2017.
 * контроллер для работы с окном графа
 */
public class UITNSchemeGraphController {

    @FXML
    private TableView<HydraC> HydraTable;
    @FXML
    private TableColumn<HydraC, String> NameTNPart_column;
    // переменные - исходные данные для расчета
    @FXML
    private TableColumn<HydraC, Double> D_column;
    @FXML
    private TableColumn<HydraC, Double> L_column;
    @FXML
    private TableColumn<HydraC, Double> G_column;
    @FXML
    private TableColumn<HydraC, Double> Hrasp_ist_column;
    @FXML
    private TableColumn<HydraC, Double> dH_fist_column; //падение напора от источника
    @FXML
    private TableColumn<HydraC, Double> Hrasp_endP_column; // падение напора в конце участка

    private Stage dialogStage;

    Graph graph;

    /**
     * Конструктор.
     * Конструктор вызывается раньше метода initialize().
     */
    public UITNSchemeGraphController() {
    }

    /**
     * Инициализация класса-контроллера. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        // Инициализация таблицы
        NameTNPart_column.setCellValueFactory(cellData -> cellData.getValue().NameTNPartProperty());
        D_column.setCellValueFactory(cellData -> cellData.getValue().DProperty().asObject());
        L_column.setCellValueFactory(cellData -> cellData.getValue().LProperty().asObject());
        G_column.setCellValueFactory(cellData -> cellData.getValue().GProperty().asObject());
        Hrasp_ist_column.setCellValueFactory(cellData -> cellData.getValue().Hrasp_istProperty().asObject());
        dH_fist_column.setCellValueFactory(cellData -> cellData.getValue().dH_fistProperty().asObject());
        Hrasp_endP_column.setCellValueFactory(cellData -> cellData.getValue().Hrasp_endPProperty().asObject());
    }

    public void setData(ArrayList hydraDataArrayList, BorderPane pane) {
        graph = new Graph(hydraDataArrayList, pane);
        HydraTable.setItems(graph.getHydraData());
    }

    /**
     * Устанавливает сцену для этого окна.
     * @param dialogStage - диалоговое окно
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        // Устанавливаем иконку приложения.
        this.dialogStage.getIcons().add(new Image("file:resources/images/Edit1.png"));
    }

    public Scene getScene() {
        return graph.getScene();
    }


}
