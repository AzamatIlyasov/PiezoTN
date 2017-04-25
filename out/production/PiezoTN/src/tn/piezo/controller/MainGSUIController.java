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
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tn.piezo.Main;
import tn.piezo.model.FileParser;
import tn.piezo.model.HydraC;
import tn.piezo.model.PiezoC;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    @FXML
    private StackPane stackPaneGraph;

    // для ГР - источник данных
    private String sourceFileName = "";

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
        // инициализация combobox
        listSourceTN.getItems().addAll(FileParser.listSourse);
        listTN.getItems().addAll(FileParser.listTN);
        listBranchingOfTN.getItems().add("Без ответвления");
        listBranchingOfTN.getItems().addAll(FileParser.listBranchTN);

    }

    /**
     * строим ПГ.
     *
     * @param piezoData
     */
    public void setPiezoData(List piezoData) {
        LayeredXyChartsSample LPchart = new LayeredXyChartsSample();
        stackPaneGraph.getChildren().addAll(LPchart.startLayerCharts(piezoData));

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

    }

    /**
     * вызывается при нажатии на combobox
     */
    @FXML
    private void mouseClickComboBox() {
        //очистка от старых данных
        /*
        if (listSourceTN.isFocused()) {
            listSourceTN.getItems().clear();
            // Заносим новые данные
            // инициализация combobox - выбор источника
            listSourceTN.getItems().addAll(FileParser.listSourse);
        }
        if (listTN.isFocused()) {
            listTN.getItems().clear();
            // Заносим новые данные
            // инициализация combobox - выбор тепловой сети
            listTN.getItems().addAll(FileParser.listTN);
        }
        if (listBranchingOfTN.isFocused()) {
            listBranchingOfTN.getItems().clear();
            // Заносим новые данные
            // инициализация combobox - выбор ответвления тепловой сети
            listBranchingOfTN.getItems().addAll(FileParser.listBranchTN);
        }
        */
    }

    // к.Вычислить - запуск гидравлического расчета для выбранного участка
    @FXML
    private void runGidRas() {
        //выбор данных для расчета
        sourceFileName = "resources/ExcelDataBase/test files/";
        sourceFileName += "input";
        sourceFileName += "-";

        try {
            // источник
            if (listSourceTN.getValue().toString().equals("Кот. №1"))
                sourceFileName += "K1";
            else if (listSourceTN.getValue().toString().equals("Кот. №3"))
                sourceFileName += "K3";
            else if (listSourceTN.getValue().toString().equals("Кот. №5"))
                sourceFileName += "K5";
            else
                sourceFileName += listSourceTN.getValue().toString();
            sourceFileName += "-";

            //тепловая сеть
            if (listTN.getValue().toString().equals("M1"))
                sourceFileName += "M1";
            else if (listTN.getValue().toString().equals("M2"))
                sourceFileName += "M2";
            else if (listTN.getValue().toString().equals("M3"))
                sourceFileName += "M3";
            else if (listTN.getValue().toString().equals("M4"))
                sourceFileName += "M4";
            else if (listTN.getValue().toString().equals("M700"))
                sourceFileName += "M700";
            else if (listTN.getValue().toString().equals("M500"))
                sourceFileName += "M500";
            else if (listTN.getValue().toString().equals("M600"))
                sourceFileName += "M600";
            else
                sourceFileName += listTN.getValue().toString();

            //ответвление
            if (listBranchingOfTN.getValue().toString().equals("Без ответления"))
                sourceFileName += "";
            else if (listBranchingOfTN.getValue().toString().equals("M88"))
                sourceFileName += "-M88";
            else if (listBranchingOfTN.getValue().toString().equals("M11"))
                sourceFileName += "-M11";

        } catch (Exception exception) {
            sourceFileName = exception.getLocalizedMessage();
        }

        sourceFileName += ".xls";
        // запуск расчета для выбранного участка
        main.runGRMain(listSourceTN.getValue().toString(),listTN.getValue().toString(),listBranchingOfTN.getValue().toString());
        //excel
        //main.runGRMain(sourceFileName);
        //SQL DBPiezo

        main.runGRSolver();
        stackPaneGraph.getChildren().clear();
        LayeredXyChartsSample LPchart = new LayeredXyChartsSample();
        stackPaneGraph.requestLayout();
        stackPaneGraph.getChildren().addAll(LPchart.startLayerCharts(main.getPiezoData()));
    }

    /**
     * сохранить график в файл-изображения
     */
    @FXML private void savePiezoPlot() throws IOException {
        WritableImage snapShot = stackPaneGraph.snapshot(null,null);

        ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(snapShot, null), "png",
                new File("resources/test.png"));
    }

}
