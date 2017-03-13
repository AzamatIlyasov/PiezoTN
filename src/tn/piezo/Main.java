package tn.piezo;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

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
    private ArrayList hydraDataArrayList;
    private ArrayList piezoDataArrayList;

    /**
     * Конструктор для главного метода приложения
     */
    public Main() {
        // выполним гидрарасчет - участок по умолчанию
        runGRMain("resources/ExcelDataBase/test files/input-K3-M2-88.xls");
    }

    //
    public void runGRMain(String fileName) {
        //считывание из БД (из файла excel) + проводим гидрарасчет
        hydraDataArrayList = ExcelParser.parseHydraT(fileName);//input-M700-M11 input-K3-M2-88 input-M700
        piezoDataArrayList = ExcelParser.parsePiezoPlot(hydraDataArrayList);

    }

    /**
     * сохранения данных в таблицу
     */
    public void saveDataTable(ArrayList DataHydra, String fileName) {
        //тестовая запись в файл Excel
        ExcelParser.writeExcelHydra(DataHydra, fileName);
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
            // Загружаем сведения об участках.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MainGSUI.fxml"));
            AnchorPane gsMainOverview = (AnchorPane)loader.load();
            //запуск расчетов
            runGRSolver();
            // Помещаем сведения об участках в центр корневого макета.
            rootLayout.setCenter(gsMainOverview);

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
            // Помещаем сведения об участках в центр корневого макета.
            rootLayout.setCenter(gsOverview);
            // Передаём участок в контроллер.
            GSOverviewController gsOverviewController = loader.getController();
            //
            gsOverviewController.setMain(this);

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
         /*   // Загружает fxml-файл и создаёт новую сцену для всплывающего окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/PiezoUI.fxml.fxml"));
            AnchorPane pgView = (AnchorPane) loader.load();
            // Помещаем сведения об участках в центр корневого макета.
            rootLayout.setCenter(pgView);
            // Передаём участок в контроллер.
            PiezoGraphicController pgController = loader.getController();
            pgController.setPiezoData(piezoData);
            //
            pgController.setPiezoData(piezoData);
            pgController.setMain(this);
*/

            // Загружает fxml-файл и создаёт новую сцену для всплывающего окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/PiezoUI.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("PiezoGraphic");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаёт адресатов в контроллер.
            PiezoGraphicController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPiezoData(piezoData);

            dialogStage.show();



        } catch (IOException e) {
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

}
