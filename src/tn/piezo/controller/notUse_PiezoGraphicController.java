package tn.piezo.controller;

/**
 * Created by djaza on 19.02.2017.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;
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
public class notUse_PiezoGraphicController {

    private Stage dialogStage;
    private PiezoC piezoC;
    private boolean okClicked = false;
    private Main main;

    //defining the axes
    @FXML
    private CategoryAxis xAxis = new CategoryAxis();
    @FXML
    private NumberAxis yAxis = new NumberAxis();
    @FXML
    public LineChart<String,Number> numberLineChart = new LineChart<String,Number>(xAxis,yAxis);
    @FXML
    public BarChart<String,Number> barChart = new BarChart<String, Number>(xAxis,yAxis);

    // определяем графики
    private XYChart.Series seriesGeodezia = new XYChart.Series();
    private XYChart.Series seriesStatic = new XYChart.Series();
    private XYChart.Series seriesPodacha = new XYChart.Series();
    private XYChart.Series seriesObratka = new XYChart.Series();
    private XYChart.Series seriesStroenie = new XYChart.Series();

    /**
     * Инициализирует класс-контроллер.
     *
     */
    @FXML
    private void initialize() {
        setDefaultChartProperties(barChart);
        setDefaultChartProperties(numberLineChart);
        configureOverlayChart(barChart);
        configureOverlayChart(numberLineChart);
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
        double[] HStatic = new double[piezoData.size()];

        seriesGeodezia.setName("Местность");
        seriesStroenie.setName("Строения");
        seriesPodacha.setName("Подача");
        seriesObratka.setName("Обратка");
        seriesStatic.setName("Статический напор");

        PiezoC tempObjPiezoDCS;
        // считаем необходимы данные для графиков и сохраняем в массивах
        ObservableList<XYChart.Data> datasGeodezia = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datasStroinie = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datasPodacha = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datasObratka = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datasStaticH = FXCollections.observableArrayList();
        // для ранжирования оси ОY
        double min =  100000;
        double max = -100000;
        for (int i = 0; i < piezoData.size(); i++) {
            // сохраняем с учетом геодезических отметок
            tempObjPiezoDCS = (PiezoC)piezoData.get(i);
            Geodezia[i] = tempObjPiezoDCS.getGeo();
            Stroinie[i] = tempObjPiezoDCS.getZdanieEtaj() * 3 + Geodezia[i];
            HStatic[i] = Stroinie[i]+5;
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
            //ищем минимум и максимум точки
            if (min > Geodezia[i]) min = Geodezia[i];
            if (max < Hpodacha[i]) max = Hpodacha[i];
        }
        //определям статическое давления
        double maxHstatic = HStatic[0];
        for (double var: HStatic) {
            if (maxHstatic < var) maxHstatic = var;
        }
        for (int i = 0; i < piezoData.size(); i++) {
            HStatic[i] = maxHstatic;
            datasStaticH.add(new XYChart.Data<>(String.valueOf(LengthPart[i]), HStatic[i]));
        }

        seriesGeodezia.setData(datasGeodezia);
        seriesStroenie.setData(datasStroinie);
        seriesPodacha.setData(datasPodacha);
        seriesObratka.setData(datasObratka);
        seriesStatic.setData(datasStaticH);

        numberLineChart.getData().add(seriesGeodezia);
        barChart.getData().add(seriesStroenie);
        numberLineChart.getData().add(seriesPodacha);
        numberLineChart.getData().add(seriesObratka);
        numberLineChart.getData().add(seriesStatic);

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(min-10);
        yAxis.setUpperBound(max+10);
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

    private void setDefaultChartProperties(final XYChart<String, Number> chart) {
        chart.setLegendVisible(false);
        chart.setAnimated(false);
    }

    private void configureOverlayChart(final XYChart<String, Number> chart) {
        chart.setAlternativeRowFillVisible(false);
        chart.setAlternativeColumnFillVisible(false);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
        chart.getXAxis().setVisible(false);
        chart.getYAxis().setVisible(false);

        chart.getStylesheets().addAll(getClass().getResource("resources/overlay-chart.css").toExternalForm());
    }

}
