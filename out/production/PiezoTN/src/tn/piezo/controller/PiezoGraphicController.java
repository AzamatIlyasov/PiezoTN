package tn.piezo.controller;

/**
 * Created by djaza on 19.02.2017.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import tn.piezo.Main;
import tn.piezo.model.HydraDataClassStruct;
import tn.piezo.model.PiezoC;
import tn.piezo.model.PiezoDataClassStructure;

import java.util.ArrayList;
import java.util.List;


/**
 * Контроллер для представления пьезометрического графика сети
 */
public class PiezoGraphicController {

    private Stage dialogStage;
    private PiezoC piezoC;
    private boolean okClicked = false;
    private Main main;

    //defining the axes
    @FXML
    private CategoryAxis xAxis = new CategoryAxis();
    //final NumberAxis xAxis = new NumberAxis();
    @FXML
    private NumberAxis yAxis = new NumberAxis();
    @FXML
    private LineChart<String,Number> numberLineChart = new LineChart<String,Number>(xAxis,yAxis);

    // список данных для построения графика
    ObservableList<XYChart.Data> dataPodacha = FXCollections.observableArrayList();
    ObservableList<XYChart.Data> dataObratka = FXCollections.observableArrayList();
    ObservableList<XYChart.Data> dataGeodezia = FXCollections.observableArrayList();
    ObservableList<XYChart.Data> dataStroenie = FXCollections.observableArrayList();
    // определяем графики
    private XYChart.Series seriesGeodezia = new XYChart.Series();
    private XYChart.Series seriesStroenie = new XYChart.Series();
    private XYChart.Series seriesPodacha = new XYChart.Series();
    private XYChart.Series seriesObratka = new XYChart.Series();

    /**
     * Инициализирует класс-контроллер.
     *
     */
    @FXML
    private void initialize() {
        numberLineChart.setTitle("Пример пьезометрического графика");
        xAxis.setLabel("Участки");
        yAxis.setLabel("Напор (с учетом геодезии), м");

    }

    /**
     * Задаёт участки для построения ПГ.
     *
     * @param piezoData
     */
    public void setPiezoData(List piezoData) {
        double[] Hpodacha = new double[piezoData.size()];
        double[] Hobratka = new double[piezoData.size()];
        double[] Geodezia = new double[piezoData.size()];
        double[] Stroinie = new double[piezoData.size()];
        double[] LengthPart = new double[piezoData.size()];

        seriesGeodezia.setName("Местность");
        seriesStroenie.setName("Строения");
        seriesPodacha.setName("Подача");
        seriesObratka.setName("Обратка");

        PiezoC tempObjPiezoDCS;

        // считаем необходимы данные для графиков и сохраняем в массивах
        ObservableList<XYChart.Data> datasGeodezia = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datasStroinie = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datasPodacha = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datasObratka = FXCollections.observableArrayList();
        // для ранжирования оси ОY
        double min = Geodezia[0];
        double max = Geodezia[0];
        for (int i = 0; i < piezoData.size(); i++)
        {
            // сохраняем с учетом геодезических отметок
            tempObjPiezoDCS = (PiezoC)piezoData.get(i);
            Geodezia[i] = tempObjPiezoDCS.getGeo();
            Stroinie[i] = tempObjPiezoDCS.getZdanieEtaj() * 3 + Geodezia[i];
            Hpodacha[i] = tempObjPiezoDCS.getHraspPod() + Geodezia[i];
            Hobratka[i] = tempObjPiezoDCS.getHraspObrat() + Geodezia[i];
            if (i==0) LengthPart[i] = tempObjPiezoDCS.getL();
            else LengthPart[i] = LengthPart[i-1] + tempObjPiezoDCS.getL();
            //// Создаём объект XYChart.Data для каждого участка.
            // Добавляем его в серии.
            datasGeodezia.add(new XYChart.Data<>(String.valueOf(LengthPart[i]), Geodezia[i]));
            datasStroinie.add(new XYChart.Data<>(String.valueOf(LengthPart[i]), Stroinie[i]));
            datasPodacha.add(new XYChart.Data<>(String.valueOf(LengthPart[i]), Hpodacha[i]));
            datasObratka.add(new XYChart.Data<>(String.valueOf(LengthPart[i]), Hobratka[i]));
        }
        seriesGeodezia.setData(datasGeodezia);
        seriesStroenie.setData(datasStroinie);
        seriesPodacha.setData(datasPodacha);
        seriesObratka.setData(datasObratka);

        numberLineChart.getData().add(seriesGeodezia);
        numberLineChart.getData().add(seriesStroenie);
        numberLineChart.getData().add(seriesPodacha);
        numberLineChart.getData().add(seriesObratka);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(590);
        yAxis.setUpperBound(750);
    }

    /**
     * Устанавливает сцену для этого окна.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Вызывается главным приложением, которое даёт на себя ссылку.
     *
     * @param main
     */
    public void setMain(Main main) {
        this.main = main;
    }

}
