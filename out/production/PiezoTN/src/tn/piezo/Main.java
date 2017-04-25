package tn.piezo;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.piezo.controller.*;
import tn.piezo.model.*;
import javafx.collections.*;
import tn.piezo.controller.MainGSUIController;


public class Main extends Application {

    /**
     * для инициализации окна приложения
     */
    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * Данные, в виде наблюдаемого списка ГР и ПГ.
     * а также в виде arraylist - для сохранения
     */
    private ObservableList<HydraC> hydraData = FXCollections.observableArrayList();
    private ObservableList<PiezoC> piezoData = FXCollections.observableArrayList();
    private ArrayList hydraDataArrayList = new ArrayList();
    private ArrayList piezoDataArrayList = new ArrayList();

    /**
     * Конструктор для главного метода приложения
     */
    public Main() {

    }

    //БД SQL DBPiezo
    public void runGRMain(String conditionBoiler, String conditionTNMain, String conditionTNBranch) {
        //считывание из БД + проводим гидрарасчет
        hydraDataArrayList.clear();
        piezoDataArrayList.clear();
        //из SQL DBPiezo
        hydraDataArrayList = DBParser.parseHydraT(conditionBoiler, conditionTNMain, conditionTNBranch);
        piezoDataArrayList = DBParser.parsePiezoPlot(hydraDataArrayList);
    }

    // БД Excel
    public void runGRMain(String fileName) {
        //считывание из БД + проводим гидрарасчет
        hydraDataArrayList.clear();
        piezoDataArrayList.clear();
        //excel
        hydraDataArrayList = ExcelParser.parseHydraT(fileName);
        piezoDataArrayList = ExcelParser.parsePiezoPlot(hydraDataArrayList);
    }

    // сохранения данных в базу DBPiezo
    public void saveDataTable(ArrayList DataHydra) {
        //тестовая запись в бд
        //sql DBPiezo
        DBParser.writeTableHydra(DataHydra);
        //excel
        //ExcelParser.writeTableHydra(DataHydra, fileName);
    }
    /**
     * сохранения данных в таблицу
     */
    public void saveDataTable(ArrayList DataHydra, String fileName) {
        //тестовая запись в бд
        //excel
        ExcelParser.writeTableHydra(DataHydra, fileName);
    }
    /**
     * ГР решатель
     */
    public void runGRSolver() {
        //очищаем старые данные
        hydraData.clear();
        piezoData.clear();
        //создаем объект для считывания ГР
        HydraDataClassStruct objHydraDCS;
        //запоминаем данные
        for (int i = 0; i < hydraDataArrayList.size(); i++) {
            // каждый участок (строка) сохраняем как новый объект
            objHydraDCS = (HydraDataClassStruct) hydraDataArrayList.get(i);
            hydraData.add(new HydraC(objHydraDCS.NamePartTN, objHydraDCS.NamePartTNpred, objHydraDCS.D,
                    objHydraDCS.L, objHydraDCS.G, objHydraDCS.Kekv, objHydraDCS.Geo, objHydraDCS.ZdanieEtaj,
                    objHydraDCS.Hrasp_ist, objHydraDCS.W, objHydraDCS.Rud,
                    objHydraDCS.b, objHydraDCS.Rrash, objHydraDCS.Hl, objHydraDCS.Hm, objHydraDCS.H1x, objHydraDCS.H2x,
                    objHydraDCS.dH_fist, objHydraDCS.Hrasp_endP, i));
        }
        //создаем объект для считывания ПГ
        PiezoDataClassStructure objPiezoDCS;
        //запоминаем данные
        for (int i = 0; i < piezoDataArrayList.size(); i++) {
            // каждый участок (строка) сохраняем как новый объект
            objPiezoDCS = (PiezoDataClassStructure) piezoDataArrayList.get(i);
            piezoData.add(new PiezoC(i, objPiezoDCS.NamePartTN,
                    objPiezoDCS.L, objPiezoDCS.Geo, objPiezoDCS.ZdanieEtaj, objPiezoDCS.HraspPod, objPiezoDCS.HraspObrat));
        }

    }

    /**
     * Возвращает данные в виде наблюдаемого списка участков.
     * @return
     */
    public ObservableList<HydraC> getHydraData() {
        return hydraData;
    }

    /**
     * Задаем новые данные для расчета.
     */
    public void setHydraData(ArrayList hydra) {
        hydraData.clear();
        //создаем объект для считывания ГР
        HydraDataClassStruct objHydraDCS;
        //запоминаем данные
        for (int i = 0; i < hydra.size(); i++) {
            // каждый участок (строка) сохраняем как новый объект
            objHydraDCS = (HydraDataClassStruct) hydra.get(i);
            hydraData.add(new HydraC(objHydraDCS.NamePartTN, objHydraDCS.NamePartTNpred, objHydraDCS.D,
                    objHydraDCS.L, objHydraDCS.G, objHydraDCS.Kekv, objHydraDCS.Geo, objHydraDCS.ZdanieEtaj,
                    objHydraDCS.Hrasp_ist, objHydraDCS.W, objHydraDCS.Rud,
                    objHydraDCS.b, objHydraDCS.Rrash, objHydraDCS.Hl, objHydraDCS.Hm, objHydraDCS.H1x, objHydraDCS.H2x,
                    objHydraDCS.dH_fist, objHydraDCS.Hrasp_endP, i));
        }

    }

    /**
     * Возвращает данные в виде наблюдаемого списка участков для ПГ.
     * @return
     */
    public ObservableList<PiezoC> getPiezoData() {
        return piezoData;
    }

    /**
     * Запуск макета приложения (окно)
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("PiezoApp");
        // Устанавливаем иконку приложения.
        this.primaryStage.getIcons().add(new Image("file:resources/images/TNSource1.jpg"));
        initRootLayout();
        //гидравлическая таблица и график
        showGSMainOverview();

    }

    /**
     * Инициализирует корневой макет.
     */
    public void initRootLayout() {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane)loader.load();


            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout, 800, 600);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.setScene(scene);
            //отображение
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Показывает в корневом макете гидравлическую таблицу и график.
     */
    public void showGSMainOverview() {
        try {
            // Загружаем данные для combobox-сов (ист,тс,ответвление)
            FileParser fileParser = new FileParser();
            fileParser.readTxtToCombobox();
            // Загружаем сведения об участках.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MainGSUI.fxml"));
            AnchorPane gsMainOverview = (AnchorPane)loader.load();
            // Помещаем сведения об участках в центр корневого макета.
            rootLayout.setCenter(gsMainOverview);
            // выполним гидрарасчет - участок по умолчанию
            // sql DBPiezo
            runGRMain("К.Баскуат","М700","");
            // excel
            //runGRMain("resources/ExcelDataBase/test files/input-main.xls");
            //запуск расчетов
            runGRSolver();
            // Даём контроллеру доступ к главному приложению.
            MainGSUIController controller = loader.getController();
            controller.setMain(this);
            controller.setPiezoData(piezoData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Показывает в корневом макете интрефейс для изменения данных.
     */
    public boolean showGSOverview() {
        try {
            // Загружаем сведения об участках.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/GSOverview.fxml"));
            AnchorPane gsOverview = (AnchorPane)loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("HydraEdit");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(gsOverview);
            dialogStage.setScene(scene);

            // Помещаем сведения об участках в центр корневого макета.
            //rootLayout.setCenter(gsOverview);
            // Передаём участок в контроллер.
            GSOverviewController gsOverviewController = loader.getController();
            gsOverviewController.setDialogStage(dialogStage);
            //
            gsOverviewController.setMain(this);
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();


            return gsOverviewController.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Открывает диалоговое окно для добавления нового участка.
     * Если пользователь кликнул OK, то изменения сохраняются в предоставленном
     * объекте адресата и возвращается значение true.
     *
     * @param hydra - объект участка, который надо изменить
     * @return true, если пользователь кликнул OK, в противном случае false.
     */
    public boolean showGSNewTableDialog(HydraC hydra) {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/GSNewTableDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Hydra table");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаём участок в контроллер.
            GSNewTableDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();
            hydraDataArrayList = controller.getNewHydraData();
            setHydraData(hydraDataArrayList);
            saveDataTable(hydraDataArrayList,controller.getFileName());

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Открывает диалоговое окно для изменения деталей указанного участка.
     * Если пользователь кликнул OK, то изменения сохраняются в предоставленном
     * объекте адресата и возвращается значение true.
     *
     * @param hydra - объект участка, который надо изменить
     * @return true, если пользователь кликнул OK, в противном случае false.
     */
    public boolean showTPartEditDialog(HydraC hydra) {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/GSEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Hydra table");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаём участок в контроллер.
            GSEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setHydra(hydra);

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Открывает диалоговое окно для вывода ПГ
     */
    public void showPiezoGraphic() {
        try {
            Stage dialogStage = new Stage();
            LayeredXyChartsSample LPchart = new LayeredXyChartsSample();
            LPchart.startLayeredXyChart(dialogStage, piezoData);
            dialogStage.setTitle("PiezoGraphic");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Возвращает главную сцену.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Запуск программы
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    //закрытие программы
    @Override
    public void stop(){
        FileParser fileParser = new FileParser();
        fileParser.writeTxtFromCombobox();
    }

}
