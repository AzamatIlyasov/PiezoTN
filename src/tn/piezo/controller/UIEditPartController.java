package tn.piezo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tn.piezo.Main;
import tn.piezo.model.DerbyDBParser;
import tn.piezo.model.HydraC;
import tn.piezo.model.HydraDataClassStruct;
import tn.piezo.model.HydraSolverC;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Окно для редактированая участка.
 *
 * @author Azamat Ilyasov
 */
public class UIEditPartController {

    @FXML
    private ComboBox comboBoiler;
    @FXML
    private ComboBox comboMain;
    @FXML
    private ComboBox comboBranch;
    @FXML
    private ComboBox comboPart;
    @FXML
    private ComboBox comboNewBranch;
    @FXML
    private TextField NewNamePart;
    @FXML
    private CheckBox chkEnableEditSourceBranch;
    @FXML
    private CheckBox chkEnableEditData;

    // переменные - исходные данные для расчета
    @FXML
    private ComboBox comboNamePartTNpred;
    @FXML
    private TextField DField;
    @FXML
    private TextField LField;
    @FXML
    private TextField GField;
    @FXML
    private TextField KekvField;
    @FXML
    private TextField GeoField;
    @FXML
    private TextField ZdanieEtajField;
    //@FXML
    //private TextField Hrasp_istField;
    @FXML
    private Button btnShowPartData;
    @FXML
    private Button btnOK;

    // Ссылка на главное приложение.
    private Main main;

    private Stage dialogStage;
    private HydraC hydra;
    private boolean okClicked = false;
    String Boiler = "";
    String TNMain = "";
    String TNBranch = "";
    String TNPart = "";

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Устанавливает сцену для этого окна.
     *
     * @param dialogStage - диалоговое окно
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        // Устанавливаем иконку приложения.
        this.dialogStage.getIcons().add(new Image("file:resources/images/Edit1.png"));
    }

    /**
     * Задаёт участка, информацию о котором будем менять
     * @param hydra - данные участка
     */
    public void setHydra(HydraC hydra) {
        NewNamePart.setText(hydra.getNamePartTN());
        comboNamePartTNpred.setValue(hydra.getNamePartTNpred());
        DField.setText(hydra.getD().toString());
        LField.setText(hydra.getL().toString());
        GField.setText(hydra.getG().toString());
        KekvField.setText(hydra.getKekv().toString());
        GeoField.setText(hydra.getGeo().toString());
        ZdanieEtajField.setText(hydra.getZdanieEtaj().toString());
        //Hrasp_istField.setText(hydra.getHrasp_ist().toString());

    }

    public HydraC getHydra() {return hydra;}

    /**
     * Returns true, если пользователь кликнул OK, в другом случае false.
     *
     * @return okClicked - нажал кнопку ОК
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке OK.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            //получение данных из полей
            hydra.setNamePartTN(NewNamePart.getText());
            hydra.setNamePartTNpred(comboNamePartTNpred.getValue().toString());
            hydra.setD(Double.parseDouble(DField.getText()));
            hydra.setL(Double.parseDouble(LField.getText()));
            hydra.setG(Double.parseDouble(GField.getText()));
            hydra.setKekv(Double.parseDouble(KekvField.getText()));
            hydra.setGeo(Double.parseDouble(GeoField.getText()));
            hydra.setZdanieEtaj(Double.parseDouble(ZdanieEtajField.getText()));
            //hydra.setHrasp_ist(Double.parseDouble(Hrasp_istField.getText()));
            //обновлене данные в БД
            DerbyDBParser.writePartData(
                    comboBoiler.getValue().toString(), comboMain.getValue().toString(), comboBranch.getValue().toString(),
                    comboNewBranch.getValue().toString(),comboPart.getValue().toString(), NewNamePart.getText(),
                    comboNamePartTNpred.getValue().toString(),
                    hydra.getD(),
                    hydra.getL(),
                    hydra.getG(),
                    hydra.getKekv(),
                    hydra.getGeo(),
                    hydra.getZdanieEtaj() );

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке Cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Проверяет пользовательский ввод в текстовых полях.
     *
     * @return true, если пользовательский ввод корректен
     */
    private boolean isInputValid() {
        String warningMessage = "";
        Boolean resultFunc = false;
        Alert alertWarning = new Alert(AlertType.CONFIRMATION);
        alertWarning.initOwner(dialogStage);
        alertWarning.setTitle("Предупреждение");
        // Показываем сообщение-предупреждение.
        alertWarning.setHeaderText("Перемещение участка в другое ответвление,\nИзменить название?");
        //проверка на изменение название магистрали
        if (NewNamePart.getText() == null || NewNamePart.getText().length() == 0) {
            alertWarning.setHeaderText("Перемещение участка в другое ответвление,\nCохранить текущее название участка?");
            NewNamePart.setText(comboPart.getValue().toString());
        }
        warningMessage = ("хотите сохранить название: " + NewNamePart.getText() + " ?\n");
        //вывод
        alertWarning.setContentText(warningMessage);
        Optional<ButtonType> result = alertWarning.showAndWait();
        if (result.get() == ButtonType.OK) {
            //проверка правильности введенных данных
            String errorMessage = "";
            if (comboNamePartTNpred.getValue().toString() == null || comboNamePartTNpred.getValue().toString().length() == 0) {
                errorMessage += "Не выбран предыдущий участок!\n";
            }
            if (DField.getText() == null || DField.getText().length() == 0 || Double.parseDouble(DField.getText()) <1 ) {
                errorMessage += "Неправильный диаметр участка!\n Диаметр необходимо ввести в мм";
            }
            if (LField.getText() == null || LField.getText().length() == 0 || Double.parseDouble(LField.getText()) <=0.5 ) {
                errorMessage += "Неправильная длина участка!\n Длину необходимо ввести в м";
            }
            if (GField.getText() == null || GField.getText().length() == 0 || Double.parseDouble(GField.getText()) <=0 ) {
                errorMessage += "Неправильный расход участка!\n Расход необходимо ввести в т/ч";
            }
            if (KekvField.getText() == null || KekvField.getText().length() == 0 || Double.parseDouble(KekvField.getText()) <=0 ) {
                errorMessage += "Неправильный коэффициент шероховатости участка!\n К-т шероховатости необходимо ввести в мм";
            }
            if (GeoField.getText() == null || GeoField.getText().length() == 0 || Double.parseDouble(KekvField.getText()) <=0 ) {
                errorMessage += "Неправильная геодезическая отметка участка!\n Геодезическую отметку необходимо ввести в м";
            }
            if (ZdanieEtajField.getText() == null || ZdanieEtajField.getText().length() == 0 || Double.parseDouble(ZdanieEtajField.getText()) < 0 ) {
                errorMessage += "Неправильная этажность здания участка!\n Этажность здания необходимо ввести в штуках ( 1 этаж,2 этажа, 3 этажа и тд";
            }
            //if (Hrasp_istField.getText() == null || Hrasp_istField.getText().length() == 0 || Double.parseDouble(Hrasp_istField.getText()) <= 0 ) {
            //    errorMessage += "Неправильный напор у источника!\n Напор у источника необходимо ввести в м";
            //}
            if (errorMessage.length() == 0) {
                resultFunc =  true;
            } else {
                // Показываем сообщение об ошибке.
                Alert alertError = new Alert(AlertType.ERROR);
                alertError.initOwner(dialogStage);
                alertError.setTitle("Некорректный ввод данных");
                alertError.setHeaderText("Пожалуйста, проверте введенные данные");
                alertError.setContentText(errorMessage);
                alertError.showAndWait();
                resultFunc = false;
            }
        }
        else {
            resultFunc = false;
        }
        //выход
        return resultFunc;
    }

    /**
     * вызывается при нажатии на combobox
     */
    @FXML
    private void mouseClickComboBox() {
        // очистка от старых данных и заносим новые данные
        if (comboBoiler.isFocused()) {
            //котельная
            comboBoiler.getItems().clear();
            DerbyDBParser.dbReadForComboboxBoiler();
            comboBoiler.getItems().addAll(DerbyDBParser.listBoiler);
            comboMain.setDisable(false);
        }
        if (comboMain.isFocused()) {
            //магистраль
            comboMain.getItems().clear();
            Boiler = comboBoiler.getValue().toString();
            DerbyDBParser.dbReadForComboboxTNMain(Boiler);
            comboMain.getItems().addAll(DerbyDBParser.listMain);
            comboBranch.setDisable(false);
        }
        if (comboBranch.isFocused()) {
            //ответвления
            comboBranch.getItems().clear();
            TNMain = comboMain.getValue().toString();
            DerbyDBParser.dbReadForComboboxTNBranch(Boiler, TNMain);
            comboBranch.getItems().addAll(DerbyDBParser.listBranch);
            comboPart.setDisable(false);
        }
        if (comboPart.isFocused()) {
            //
            comboNewBranch.getItems().clear();
            comboNewBranch.getItems().addAll(DerbyDBParser.listMain);
            comboNewBranch.setValue(comboBranch.getValue());
            //участки
            comboPart.getItems().clear();
            TNBranch = comboBranch.getValue().toString();
            DerbyDBParser.dbReadPart(Boiler, TNMain, TNBranch);
            comboPart.getItems().addAll(DerbyDBParser.listPart);
            comboNamePartTNpred.getItems().addAll(DerbyDBParser.listAllPartInBoiler);
            chkEnableEditSourceBranch.setDisable(false);
            btnShowPartData.setDisable(false);
            btnOK.setDisable(false);
        }
        if (comboNewBranch.isFocused() && chkEnableEditSourceBranch.isSelected()) {
            comboNewBranch.getItems().clear();
            DerbyDBParser.dbReadForComboboxTNMain(comboBoiler.getValue().toString());
            comboNewBranch.getItems().addAll(DerbyDBParser.listMain);
        }
    }

    /**
    * вызывается при нажатии на combobox
    */
    @FXML
    private void chkEnableAction() {
        //редактирование источника ответвления участка
        if (chkEnableEditSourceBranch.isSelected())
            comboNewBranch.setDisable(false);
        else
            comboNewBranch.setDisable(true);
        //редактирование данных участка
        if (chkEnableEditData.isSelected()) {
            comboNamePartTNpred.setDisable(false);
            DField.setDisable(false);
            LField.setDisable(false);
            GField.setDisable(false);
            KekvField.setDisable(false);
            GeoField.setDisable(false);
            ZdanieEtajField.setDisable(false);
            //Hrasp_istField.setDisable(false);
        }
        else {
            comboNamePartTNpred.setDisable(true);
            DField.setDisable(true);
            LField.setDisable(true);
            GField.setDisable(true);
            KekvField.setDisable(true);
            GeoField.setDisable(true);
            ZdanieEtajField.setDisable(true);
            //Hrasp_istField.setDisable(true);
        }
    }

    /**
     * вызывается при выборе участка
     */
    @FXML
    private void partSelected() {
        //изменение данных
        TNPart = comboPart.getValue().toString();
        HydraSolverC hydraPart = DerbyDBParser.parseHydraPartData(Boiler,TNMain,TNBranch,TNPart);
        ArrayList<HydraDataClassStruct> hydraPartData = new ArrayList<>();
        hydraPartData.add( new HydraDataClassStruct(
                hydraPart.NamePartTN[0],
                hydraPart.NamePartTNpred[0],
                hydraPart.D[0],
                hydraPart.L[0],
                hydraPart.G[0],
                hydraPart.Kekv[0],
                hydraPart.Geo[0],
                hydraPart.ZdanieEtaj[0],
                hydraPart.BoilerName,
                hydraPart.MainName,
                hydraPart.BranchName
        ) );
        //создаем объект для считывания ГР
        HydraDataClassStruct objHydraDCS;
        //запоминаем данные
        // каждый участок (строка) сохраняем как новый объект
        objHydraDCS = hydraPartData.get(0);
        hydra = new HydraC(objHydraDCS.NamePartTN, objHydraDCS.NamePartTNpred, objHydraDCS.D,
                        objHydraDCS.L, objHydraDCS.G, objHydraDCS.Kekv, objHydraDCS.Geo, objHydraDCS.ZdanieEtaj, 0,
                        objHydraDCS.BoilerName, objHydraDCS.MainName, objHydraDCS.BranchName);
        setHydra(hydra);
        chkEnableEditData.setDisable(false);
    }

}
