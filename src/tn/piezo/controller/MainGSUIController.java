package tn.piezo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import tn.piezo.Main;
import tn.piezo.model.HydraC;
import tn.piezo.model.PiezoC;

import java.util.List;

/**
 * Created by djaza on 16.02.2017.
 */
public class MainGSUIController {

    @FXML
    private TableView<HydraC> HydraTable;
    @FXML
    private TableColumn<HydraC, Integer> Number_column;
    @FXML
    private TableColumn<HydraC, String> NamePartTN_column;
    // переменные - исходные данные для расчета
    @FXML
    private TableColumn<HydraC, Double> D_column;
    @FXML
    private TableColumn<HydraC, Double> L_column;
    @FXML
    private TableColumn<HydraC, Double> G_column;
    @FXML
    private TableColumn<HydraC, Double> Kekv_column;
    @FXML
    private TableColumn<HydraC, Double> Hrasp_ist_column;
    @FXML
    private Label n; // нумерация участков
    // переменные - результаты расчета
    @FXML
    private TableColumn<HydraC, Double> W_column;
    @FXML
    private TableColumn<HydraC, Double> Rud_column;
    @FXML
    private TableColumn<HydraC, Double> b_column;
    @FXML
    private TableColumn<HydraC, Double> Rrash_column;
    @FXML
    private TableColumn<HydraC, Double> Hl_column;
    @FXML
    private TableColumn<HydraC, Double> Hm_column;
    @FXML
    private TableColumn<HydraC, Double> H1x_column;
    @FXML
    private TableColumn<HydraC, Double> H2x_column;
    @FXML
    private TableColumn<HydraC, Double> dH_fist_column; //падение напора от источника
    @FXML
    private TableColumn<HydraC, Double> Hrasp_endP_column; // падение напора в конце участка
    @FXML
    private ComboBox listSourceTN;
    @FXML
    private ComboBox listTN;
    @FXML
    private ComboBox listBranchingOfTN;

    // для ГР - источник данных
    String sourceFileName = "";
    //defining the axes
    @FXML
    private CategoryAxis xAxis = new CategoryAxis();
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

    // Ссылка на главное приложение.
    private Main main;

    /**
     * Конструктор.
     * Конструктор вызывается раньше метода initialize().
     */
    public MainGSUIController() {
    }

    /**
     * Инициализация класса-контроллера. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        // Инициализация таблицы
        Number_column.setCellValueFactory(cellData -> cellData.getValue().NumProperty().asObject());
        NamePartTN_column.setCellValueFactory(cellData -> cellData.getValue().NamePartTNProperty());
        //NamePartTNpred_column.setCellValueFactory(cellData -> cellData.getValue().NamePartTNProperty());
        D_column.setCellValueFactory(cellData -> cellData.getValue().DProperty().asObject());
        L_column.setCellValueFactory(cellData -> cellData.getValue().LProperty().asObject());
        G_column.setCellValueFactory(cellData -> cellData.getValue().GProperty().asObject());
        Kekv_column.setCellValueFactory(cellData -> cellData.getValue().KekvProperty().asObject());
        Hrasp_ist_column.setCellValueFactory(cellData -> cellData.getValue().Hrasp_istProperty().asObject());
        W_column.setCellValueFactory(cellData -> cellData.getValue().WProperty().asObject());
        Rud_column.setCellValueFactory(cellData -> cellData.getValue().RudProperty().asObject());
        b_column.setCellValueFactory(cellData -> cellData.getValue().bProperty().asObject());
        Rrash_column.setCellValueFactory(cellData -> cellData.getValue().RrashProperty().asObject());
        Hl_column.setCellValueFactory(cellData -> cellData.getValue().HlProperty().asObject());
        Hm_column.setCellValueFactory(cellData -> cellData.getValue().HmProperty().asObject());
        H1x_column.setCellValueFactory(cellData -> cellData.getValue().H1xProperty().asObject());
        H2x_column.setCellValueFactory(cellData -> cellData.getValue().H2xProperty().asObject());
        dH_fist_column.setCellValueFactory(cellData -> cellData.getValue().dH_fistProperty().asObject());
        Hrasp_endP_column.setCellValueFactory(cellData -> cellData.getValue().Hrasp_endPProperty().asObject());
        //инициализация графика
        numberLineChart.setTitle("Пример пьезометрического графика");
        xAxis.setLabel("Участки, м");
        yAxis.setLabel("Напор (с учетом геодезии), м");
        // инициализация combobox - выбор источника
        listSourceTN.getItems().addAll("К.Баскуат",
                "Кот. №1",
                "Кот. №3");
        // инициализация combobox - выбор тепловой сети
        listTN.getItems().addAll("М700",
                "М500",
                "М600");
        // инициализация combobox - выбор ответвления тепловой сети
        listBranchingOfTN.getItems().addAll("___",
                "М11",
                "");
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
     * Вызывается главным приложением, которое даёт на себя ссылку.
     *
     * @param main
     */
    public void setMain(Main main) {
        this.main = main;

        // Добавление в таблицу данных из наблюдаемого списка
        HydraTable.setItems(main.getHydraData());
    }

    /**
     * Вызывается, когда пользователь кликает по кнопке Внести изменения в БД
     * Открывает  для изменения.
     */
    @FXML
    private void handleEditDataBase() {
        //изменение данных
        main.showGSOverview();
    }

    /**
     * Вызывается, когда пользователь кликает по кнопке Пьезометрический график
     * Открывает отображения ПГ.
     */
    @FXML
    private void handlePiezoGraphic() {
        //изменение данных
        main.showPiezoGraphic();
    }

    /**
     * Вызывается, когда пользователь выбирает определенный участок в тепловой сети для определенной котельной
     * открывает соответствующую таблицу с исходными данными.
     */
    @FXML
    private void handleTermalNet() {
        sourceFileName = "resources/ExcelDataBase/test files/input-M700.xls";
        //sourceFileName = "resources/ExcelDataBase/test files/input-M700-M11.xls";
        //sourceFileName = "resources/ExcelDataBase/test files/input-K3-M2-88.xls";
    }

    // к.Вычислить - запуск гидравлического расчета для выбранного участка
    @FXML
    private void runGidRas() {
        //main.runGRMain(sourceFileName);
        main.runGRSolver();
    }

}
