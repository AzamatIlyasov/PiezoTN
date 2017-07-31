package tn.piezo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tn.piezo.model.PiezoC;
import java.util.List;

/** original class name is: PiezoChartDrawer
 * This class copy from stackoveflow
 * Demonstrates how to draw layers of XYCharts.  
 * https://forums.oracle.com/forums/thread.jspa?threadID=2435995 "Using StackPane to layer more different type charts"
 */
public class PiezoChartDrawer {

    // для ранжирования оси ОY
    private double min =  100000;
    private double max = -100000;
    // определяем графики
    private XYChart.Series seriesGeodezia = new XYChart.Series();
    private XYChart.Series seriesStatic = new XYChart.Series();
    private XYChart.Series seriesPodacha = new XYChart.Series();
    private XYChart.Series seriesObratka = new XYChart.Series();
    private XYChart.Series seriesStroenie = new XYChart.Series();
    private PiezoC tempObjPiezoDCS;
    // считаем необходимы данные для графиков и сохраняем в массивах
    private ObservableList<XYChart.Data> datasGeodezia = FXCollections.observableArrayList();
    private ObservableList<XYChart.Data> datasStroinie = FXCollections.observableArrayList();
    private ObservableList<XYChart.Data> datasPodacha = FXCollections.observableArrayList();
    private ObservableList<XYChart.Data> datasObratka = FXCollections.observableArrayList();
    private ObservableList<XYChart.Data> datasStaticH = FXCollections.observableArrayList();

    public void startLayeredXyChart(Stage stage, List piezoData) {
        stage.setScene( new Scene( startLayerCharts(piezoData) ) );
        stage.show();
    }

    public StackPane startLayerCharts(List piezoData) {
        setPiezoData(piezoData);
        return layerCharts(
                createBarChart(),
                createStackedAreaChart(),
                createLineChart()
        );
    }

    private NumberAxis createYaxis() {
        final NumberAxis axis = new NumberAxis(min - 10, max + 10, 1);
        axis.setPrefWidth(35);
        axis.setMinorTickCount(10);
        axis.setAutoRanging(false);
 
        axis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(axis) {
            @Override public String toString(Number object) {
                return String.format("%7.2f", object.floatValue());
            }
        });
        
        return axis;
    }
    
    private BarChart<String, Number> createBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        final BarChart<String, Number> bc = new BarChart<>(xAxis, createYaxis());
        setDefaultChartProperties(bc);
        bc.getData().addAll( seriesStroenie );
        //меняем ширину зданий-столбов
        double maxBarWidth=10;
        double minCategoryGap=100;
        double barWidth=1;
        do{
            double catSpace = xAxis.getCategorySpacing();
            double avilableBarSpace = catSpace - (bc.getCategoryGap() + bc.getBarGap());
            barWidth = (avilableBarSpace / bc.getData().size()) - bc.getBarGap();
            if (barWidth >maxBarWidth) {
                avilableBarSpace=(maxBarWidth + bc.getBarGap())* bc.getData().size();
                bc.setCategoryGap(catSpace-avilableBarSpace-bc.getBarGap());
            }
        } while(barWidth>maxBarWidth);

        do{
            double catSpace = xAxis.getCategorySpacing();
            double avilableBarSpace = catSpace - (minCategoryGap + bc.getBarGap());
            barWidth = Math.min(maxBarWidth, (avilableBarSpace / bc.getData().size()) - bc.getBarGap());
            avilableBarSpace=(barWidth + bc.getBarGap())* bc.getData().size();
            bc.setCategoryGap(catSpace-avilableBarSpace-bc.getBarGap());
        } while(barWidth < maxBarWidth && bc.getCategoryGap()>minCategoryGap);

        return bc;

    }
    
    private LineChart<String, Number> createLineChart() {
        final LineChart<String, Number> chart = new LineChart<>(new CategoryAxis(), createYaxis());
        setDefaultChartProperties(chart);
        chart.setCreateSymbols(false);
        chart.getData().addAll(
                                seriesStatic,
                                seriesPodacha,
                                seriesObratka );
        Node lineStatic = seriesStatic.getNode().lookup(".chart-series-line");
        Node linePodacha = seriesPodacha.getNode().lookup(".chart-series-line");
        Node lineObratka = seriesObratka.getNode().lookup(".chart-series-line");
        Color color = Color.BLACK;
        String rgb = String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
        lineStatic.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0); -fx-stroke-width: 2px;");
        color=Color.RED;
        rgb = String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
        linePodacha.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0); -fx-stroke-width: 4px;");
        color=Color.BLUE;
        rgb = String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
        lineObratka.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0); -fx-stroke-width: 4px;");
        return chart;
    }

    private StackedAreaChart<String, Number> createStackedAreaChart() {
        final StackedAreaChart<String, Number> chart = new StackedAreaChart<>(new CategoryAxis(), createYaxis());
        setDefaultChartProperties(chart);
        chart.setCreateSymbols(false);
        chart.getData().addAll( seriesGeodezia );

        return chart;
    }

    private void setDefaultChartProperties(final XYChart<String, Number> chart) {
        chart.setLegendVisible(false);
        chart.setAnimated(false);
    }
    
    private StackPane layerCharts(final XYChart<String, Number> ... charts) {
        for (int i = 1; i < charts.length; i++) {
            configureOverlayChart(charts[i]);
        }

        StackPane stackpane = new StackPane();
        stackpane.getChildren().addAll(charts);

        return stackpane;
    }

    private void configureOverlayChart(final XYChart<String, Number> chart) {
        chart.setAlternativeRowFillVisible(false);
        chart.setAlternativeColumnFillVisible(false);
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
        chart.getXAxis().setVisible(false);
        chart.getYAxis().setVisible(false);
        chart.getStylesheets().addAll(getClass().getResource("overlay-chart.css").toExternalForm());
    }

    /**
     * Задаёт участки для построения ПГ.
     *
     * @param piezoData - данные для построения графика
     */
    public void setPiezoData(List piezoData) {
        double[] Hpodacha = new double[piezoData.size()];
        double[] Hobratka = new double[piezoData.size()];
        double[] Geodezia = new double[piezoData.size()];
        double[] Stroinie = new double[piezoData.size()];
        double[] LengthPart = new double[piezoData.size()];
        double[] HStatic = new double[piezoData.size()];

        seriesStroenie.setName("Строения");
        seriesPodacha.setName("Подача");
        seriesObratka.setName("Обратка");
        seriesStatic.setName("Статический напор");
        seriesGeodezia.setName("Местность");

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
            datasPodacha.add(new XYChart.Data<>(String.valueOf(LengthPart[i]), Hpodacha[i]));
            datasObratka.add(new XYChart.Data<>(String.valueOf(LengthPart[i]), Hobratka[i]));
            datasStroinie.add(new XYChart.Data<>(String.valueOf(LengthPart[i]), Stroinie[i]));
            datasGeodezia.add(new XYChart.Data<>(String.valueOf(LengthPart[i]), Geodezia[i]));
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

        seriesStroenie.setData(datasStroinie);
        seriesPodacha.setData(datasPodacha);
        seriesObratka.setData(datasObratka);
        seriesStatic.setData(datasStaticH);
        seriesGeodezia.setData(datasGeodezia);

    }
}