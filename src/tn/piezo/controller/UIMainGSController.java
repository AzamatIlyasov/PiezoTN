package tn.piezo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import tn.piezo.Main;
import tn.piezo.model.DerbyDBParser;
import tn.piezo.model.HydraC;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by djaza on 16.02.2017.
 * контроллер для работы с главные окном
 */
public class UIMainGSController {

    @FXML
    private TableView<HydraC> HydraTable;
    @FXML
    private TableColumn<HydraC, Integer> Number_column;
    @FXML
    private TableColumn<HydraC, String> NameTNPart_column;
    @FXML
    private TableColumn<HydraC, String> NameTNPartPred_column;
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
    private ComboBox listTNBoiler;
    @FXML
    private ComboBox listTNMain;
    @FXML
    private ComboBox listTNBranch;
    @FXML
    public StackPane stackPaneGraph;
    @FXML
    private ToggleButton toggleBtnDBconnect;
    @FXML
    private ToggleButton toggleBtnDBdisconnect;
    @FXML
    private Button btnSolver;
    @FXML
    private TextField txtHist;

    // Ссылка на главное приложение.
    private Main main;

    /**
     * Конструктор.
     * Конструктор вызывается раньше метода initialize().
     */
    public UIMainGSController() {
    }

    /**
     * Инициализация класса-контроллера. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        // Инициализация таблицы
        Number_column.setCellValueFactory(cellData -> cellData.getValue().NumProperty().asObject());
        NameTNPart_column.setCellValueFactory(cellData -> cellData.getValue().NameTNPartProperty());
        NameTNPartPred_column.setCellValueFactory(cellData -> cellData.getValue().NameTNPartPredProperty());
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
        listTNBoiler.getItems().addAll(DerbyDBParser.listTNBoiler);
        //отключаем комбобоксы магистрали и ответвления
        listTNMain.setDisable(true);
        listTNBranch.setDisable(true);
        connectDB_btn();
        btnSolver.setDisable(true);
        txtHist.getText();
    }

    /**
     * строим ПГ.
     * @param piezoData - данные для построения
     */
    public void setPiezoData(List piezoData) {
        PiezoChartDrawer LPchart = new PiezoChartDrawer();
        stackPaneGraph.getChildren().addAll(LPchart.startLayerCharts(piezoData));
    }

    /**
     * Вызывается главным приложением, которое даёт на себя ссылку.
     *
     * @param main - главное приложение
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
    private void handleEditDataBaseBtn() {
        //изменение данных
        HydraC selectedHydra = HydraTable.getSelectionModel().getSelectedItem();
        main.showEditBtnDialog(selectedHydra);
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
     * Вызывается, когда пользователь кликает по кнопке добаить участок
     * Открывает отображения ПГ.
     */
    @FXML
    private void handleAddBtn() {
        //добавления данных
        main.showAddBtnDialog();
    }

    /**
     * Вызывается, когда пользователь кликает по кнопке удалить
     * Открывает отображения ПГ.
     */
    @FXML
    private void handleDeleteBtn() {
        //удаление данных
        main.showDeleteBtnDialog();
    }

    /**
     * Вызывается, когда пользователь кликает по кнопке Датчики
     * Открывает окно для запроса данных из датчиков
     */
    @FXML
    private void GetDataSensorBtn() {
        //просмотр данных из датчиков
        main.showDataSensorDialog();
    }

    /**
     * Вызывается, когда пользователь кликает по кнопке Расчетная схема
     * Открывает окно-изображение расчетной схемы
     */
    @FXML
    private void CurSchemeBtn() {

    }

    /**
     * вызывается при нажатии на combobox
     */
    @FXML
    private void mouseClickComboBox() {
        // очистка от старых данных и заносим новые данные

        if (listTNBoiler.isFocused()) {
            listTNBoiler.getItems().clear();
            DerbyDBParser.dbReadForComboboxBoiler();
            listTNBoiler.getItems().addAll(DerbyDBParser.listTNBoiler);
            listTNMain.setDisable(false);
        }
        if (listTNMain.isFocused()) {
            listTNMain.getItems().clear();
            DerbyDBParser.dbReadForComboboxTNMain(listTNBoiler.getValue().toString());
            listTNMain.getItems().addAll(DerbyDBParser.listTNMain);
            listTNBranch.setDisable(false);
        }
        if (listTNBranch.isFocused()) {
            listTNBranch.getItems().clear();
            DerbyDBParser.dbReadForComboboxTNBranch(listTNBoiler.getValue().toString(), listTNMain.getValue().toString());
            listTNBranch.getItems().addAll(DerbyDBParser.listTNBranch);
            btnSolver.setDisable(false);
        }
    }

    // к.Вычислить - запуск гидравлического расчета для выбранного участка
    @FXML
    private void runGidRas() {
        // запуск расчета для выбранного участка

        main.runGRMain(listTNBoiler.getValue().toString(), listTNMain.getValue().toString(),
                listTNBranch.getValue().toString(),Double.parseDouble(txtHist.getText()));

        main.runGRSolver();
        stackPaneGraph.getChildren().clear();
        PiezoChartDrawer LPchart = new PiezoChartDrawer();
        stackPaneGraph.requestLayout();
        stackPaneGraph.getChildren().addAll(LPchart.startLayerCharts(main.getPiezoData()));
    }

    /**
     * сохранить график в файл-изображения
     */
    @FXML
    private void savePiezoPlot() throws IOException {
        WritableImage snapShot = stackPaneGraph.snapshot(null,null);

        ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(snapShot, null), "png",
                new File("resources/test.png"));
    }

    /**
     * подключиться к БД
     */
    @FXML
    private void connectDB_btn() {
        try {
            if (DerbyDBParser.con.isClosed()) {
                DerbyDBParser.connectDataBase();
            }
            // подключен
            toggleBtnDBconnect.setSelected(true);
            Paint paint = Color.GREEN;
            toggleBtnDBconnect.setTextFill(paint);
            // отключить
            toggleBtnDBdisconnect.setSelected(false);
            paint = Color.BLACK;
            toggleBtnDBdisconnect.setTextFill(paint);

        }
        catch (Exception e) {
            // логируем исключения
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    /**
     * закрыть подключение к БД
     */
    @FXML
    private void closeConDB_btn() {
        try {
            if (!DerbyDBParser.con.isClosed()) {
                DerbyDBParser.closeConnectDB();
                toggleBtnDBconnect.setSelected(false);
                Paint paint = Color.BLACK;
                toggleBtnDBconnect.setTextFill(paint);
                toggleBtnDBdisconnect.setSelected(true);
                paint = Color.RED;
                toggleBtnDBdisconnect.setTextFill(paint);
            }
        }
        catch (Exception e) {
            //логируем исключения
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, e);
        }

    }

}
