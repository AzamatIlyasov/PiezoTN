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
import tn.piezo.controller.TNBoiler.UIAddTNBoilerController;
import tn.piezo.controller.TNBoiler.UIDeleteTNBoilerController;
import tn.piezo.controller.TNBoiler.UIEditTNBoilerController;
import tn.piezo.controller.TNBranch.UIAddTNBranchController;
import tn.piezo.controller.TNBranch.UIDeleteTNBranchController;
import tn.piezo.controller.TNBranch.UIEditTNBranchController;
import tn.piezo.controller.TNConsumer.UIAddTNConsumerController;
import tn.piezo.controller.TNConsumer.UIDeleteTNConsumerController;
import tn.piezo.controller.TNConsumer.UIEditTNConsumerController;
import tn.piezo.controller.TNKolodets.UIAddTNKolodetsController;
import tn.piezo.controller.TNKolodets.UIDeleteTNKolodetsController;
import tn.piezo.controller.TNKolodets.UIEditTNKolodetsController;
import tn.piezo.controller.TNMain.UIAddTNMainController;
import tn.piezo.controller.TNMain.UIDeleteTNMainController;
import tn.piezo.controller.TNMain.UIEditTNMainController;
import tn.piezo.controller.TNPart.UIAddTNPartController;
import tn.piezo.controller.TNPart.UIDeleteTNPartController;
import tn.piezo.controller.TNPart.UIEditTNPartController;
import tn.piezo.model.*;
import javafx.collections.*;
import tn.piezo.controller.UIMainGSController;
//для отчетов



public class Main extends Application {

    /**
     * Конструктор для главного метода приложения
     */
    public Main() {
    }

    /**
     * Запуск программы
     */
    public static void main(String[] args) {
        launch(args);
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
        //подключаем базу данных PiezoDerbyDB
        DerbyDBParser.connectDataBase();
        //гидравлическая таблица и график
        showGSMainOverview();

    }

    //закрытие программы
    @Override
    public void stop(){
        DerbyDBParser.closeConnectDB();
        System.exit(0);
    }

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

    public ArrayList getHydraDataArrayList() {
        return hydraDataArrayList;
    }

    private ArrayList hydraDataArrayList = new ArrayList();
    private ArrayList piezoDataArrayList = new ArrayList();

    //БД SQL DBPiezo
    public void runGRMain(String conditionBoiler, String conditionTNMain, String conditionTNBranch, double Hist) {
        //считывание из БД + проводим гидрарасчет
        hydraDataArrayList.clear();
        piezoDataArrayList.clear();
        //из SQL PiezoDerbyDB
        hydraDataArrayList = DerbyDBParser.parseHydraT(conditionBoiler, conditionTNMain, conditionTNBranch, Hist);
        piezoDataArrayList = DerbyDBParser.parsePiezoPlot(hydraDataArrayList);

    }

    /**
     * сохранения данных в файл-таблицу Excel
     */
    private void saveDataTable(ArrayList DataHydra, String fileName) {
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
                    objHydraDCS.dH_fist, objHydraDCS.Hrasp_endP, i,
                    objHydraDCS.BoilerName, objHydraDCS.MainName, objHydraDCS.BranchName));
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
     * @return hydraData
     */
    public ObservableList<HydraC> getHydraData() {
        return hydraData;
    }

    /**
     * Задаем новые данные для расчета.
     */
    private void setHydraData(ArrayList hydra) {
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
                    objHydraDCS.dH_fist, objHydraDCS.Hrasp_endP, i,
                    objHydraDCS.BoilerName, objHydraDCS.MainName, objHydraDCS.BranchName));
        }

    }

    /**
     * Возвращает данные в виде наблюдаемого списка участков для ПГ.
     * @return piezoData
     */
    public ObservableList<PiezoC> getPiezoData() {
        return piezoData;
    }

    /**
     * Инициализирует корневой макет.
     */
    private void initRootLayout() {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();
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
     * Открывает диалоговое окно для вывода ПГ
     */
    public void showPiezoGraphic() {
        try {
            Stage dialogStage = new Stage();
            PiezoChartDrawer LPchart = new PiezoChartDrawer();
            LPchart.startLayeredXyChart(dialogStage, piezoData);
            dialogStage.setTitle("PiezoGraphic");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает главную сцену.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Показывает в корневом макете гидравлическую таблицу и график.
     */
    private void showGSMainOverview() {
        try {
            // Загружаем сведения об участках.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIMainGS.fxml"));
            AnchorPane gsMainOverview = loader.load();
            // Помещаем сведения об участках в центр корневого макета.
            rootLayout.setCenter(gsMainOverview);
            // выполним гидрарасчет - участок по умолчанию
            // PiezoDerbyDB
            runGRMain("Котельная Баскуат","М700","Магистраль Д700", 90.0);
            //запуск расчетов
            runGRSolver();
            // Даём контроллеру доступ к главному приложению.
            UIMainGSController controller = loader.getController();
            controller.setMain(this);
            controller.setPiezoData(piezoData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //окна для редактирования
    /**
     * Диалоговое окно для выбора источника редактирования
     */
    public void showEditBtnDialog(HydraC selectedHydra) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIAddEditWindow.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Выберите элемент для редактирования");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIAddEditWindowController controller = loader.getController();
            controller.setSelectedHydra(selectedHydra);
            controller.setDialogStage(dialogStage);
            controller.setMain(this);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Диалоговое окно редактирование источника (Котельной)
     */
    public void showEditBoilerDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIEditTNBoiler.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактирование источника (котельной)");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIEditTNBoilerController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Диалоговое окно редактирование магистральной сети
     */
    public void showEditMainDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIEditTNMain.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактирование магистральной");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIEditTNMainController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Диалоговое окно редактирование ответвления
     */
    public void showEditBranchDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIEditTNBranch.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактирование ответвления");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIEditTNBranchController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Открывает диалоговое окно для изменения деталей указанного участка.
     * Если пользователь кликнул OK, то изменения сохраняются и возвращается значение true.
     *
     * @return true, если пользователь кликнул OK, в противном случае false.
     */
    public void showEditPartDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIEditTNPart.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактирование участка");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIEditTNPartController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Открывает диалоговое окно для изменения деталей указанного колодца.
     * Если пользователь кликнул OK, то изменения сохраняются и возвращается значение true.
     *
     * @return true, если пользователь кликнул OK, в противном случае false.
     */
    public void showEditKolodetsDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIEditTNKolodets.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактирование колодцы");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIEditTNKolodetsController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Открывает диалоговое окно для изменения деталей указанного колодца.
     * Если пользователь кликнул OK, то изменения сохраняются и возвращается значение true.
     *
     * @return true, если пользователь кликнул OK, в противном случае false.
     */
    public void showEditConsumerDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIEditTNConsumer.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Редактирование потребителя");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIEditTNConsumerController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //окна для добавления
    /**
     * Диалоговое окно для выбора источника добавления информации
     */
    public void showAddBtnDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIAddEditWindow.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Выберите элемент для добавления");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIAddEditWindowController controller = loader.getController();
            controller.setSelectedMode(true); //режим добавления данных
            controller.setDialogStage(dialogStage);
            controller.setMain(this);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Диалоговое окно редактирование источника (Котельной)
     */
    public void showAddBoilerDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIAddTNBoiler.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавить источник тепла (котельная)");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIAddTNBoilerController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Диалоговое окно добавления магистральной сети
     */
    public void showAddMainDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIAddTNMain.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавить магистральную сеть");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIAddTNMainController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Диалоговое окно добавления ответвления
     */
    public void showAddBranchDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIAddTNBranch.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавить ответвления");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIAddTNBranchController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Диалоговое окно для добавления участка
     */
    public void showAddPartDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIAddTNPart.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавить участок");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIAddTNPartController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Диалоговое окно для добавления колодцы
     */
    public void showAddKolodetsDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIAddTNKolodets.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавить колодец");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIAddTNKolodetsController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Диалоговое окно для добавления
     */
    public void showAddConsumerDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIAddTNConsumer.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Добавить потребителя");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIAddTNConsumerController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //окна для удаления
    /**
     * Диалоговое окно для удаления источника тепла (котельная)
     */
    public void showDeleteBtnDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIDeleteWindow.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Выберите элемент для УДАЛЕНИЯ");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIDeleteWindowController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMain(this);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Диалоговое окно удаления источника (Котельной)
     */
    public void showDeleteBoilerDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIDeleteTNBoiler.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Удалить источник тепла (котельная)");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIDeleteTNBoilerController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Диалоговое окно удаления магистральной сети
     */
    public void showDeleteMainDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIDeleteTNMain.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Удалить магистральную сеть");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIDeleteTNMainController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Диалоговое окно удаления ответвления
     */
    public void showDeleteBranchDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIDeleteTNBranch.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Удалить ответвления");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIDeleteTNBranchController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Диалоговое окно для удаления участка
     */
    public void showDeletePartDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIDeleteTNPart.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Удалить участок");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIDeleteTNPartController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Диалоговое окно для удаления колодца
     */
    public void showDeleteKolodetsDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIDeleteTNKolodets.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Удалить колодец");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIDeleteTNKolodetsController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Диалоговое окно для удаления потребителя
     */
    public void showDeleteConsumerDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIDeleteTNConsumer.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Удалить потребителя");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIDeleteTNConsumerController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //дополнительно
    /**
     * Диалоговое окно для просмотра данных из датчиков (терминалов тесла2)
     */
    public void showDataSensorDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UIDataSensor.fxml"));
            AnchorPane editBtnDialog = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Данные из датчиков");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(editBtnDialog);
            dialogStage.setScene(scene);
            UIDataSensorController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Диалоговое окно для просмотра данных из датчиков (терминалов тесла2)
    public void showSchemeGraph() {
        try {
            // Загружаем сведения об участках.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UITNSchemeGraph.fxml"));
            BorderPane schemeGraph = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Граф тепловой сети");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            //Scene scene = new Scene(schemeGraph);
            UITNSchemeGraphController controller = loader.getController();
            controller.setData(hydraDataArrayList, schemeGraph);
            controller.setDialogStage(dialogStage);
            dialogStage.setScene(controller.getScene());
            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    //Диалоговое окно для просмотра данных из датчиков (терминалов тесла2)
    public void showSchemeGraph() {
        try {
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Граф тепловой сети");
            Graph graph = new Graph(hydraDataArrayList);
            dialogStage.setScene(graph.getScene());
            dialogStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    //отчеты
    public void showReportDesign() {

    }

    /*
    устраевший код (для перемещенных-удаленных) файлов

     //Показывает в корневом макете интрефейс для изменения данных.
    public void showGSOverview() {
        try {
            // Загружаем сведения об участках.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/GSOverview.fxml"));
            AnchorPane gsOverview = loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("HydraEdit");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(gsOverview);
            dialogStage.setScene(scene);

            // Передаём участок в контроллер.
            GSOverviewController gsOverviewController = loader.getController();
            gsOverviewController.setDialogStage(dialogStage);
            gsOverviewController.setMain(this);
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     //Открывает диалоговое окно для добавления нового участка.
    public boolean showGSNewTableDialog() {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/GSNewTableDialog.fxml"));
            AnchorPane page = loader.load();

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
            setHydraDataClassStruct(hydraDataArrayList);
            saveDataTable(hydraDataArrayList,controller.getFileName());

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Открывает диалоговое окно для изменения деталей указанного участка.
    public HydraC showTPartEditDialog(HydraC hydra) {
        HydraC hydraEditData = new HydraC();
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/GSEditDialog.fxml"));
            AnchorPane page = loader.load();

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
            //получаем новые измененные данные
            hydraEditData = controller.getHydra();

            return hydraEditData;
        } catch (IOException e) {
            e.printStackTrace();
            return hydraEditData;
        }
    }

    */

}
