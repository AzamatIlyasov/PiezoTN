package tn.piezo.controller.TNConsumer;

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
public class UIEditTNConsumerController {

    @FXML
    private ComboBox comboTNBoiler;
    @FXML
    private ComboBox comboTNMain;
    @FXML
    private ComboBox comboTNBranch;
    @FXML
    private ComboBox comboTNPart;
    @FXML
    private ComboBox comboNewTNBranch;
    @FXML
    private TextField NewNameTNPart;
    @FXML
    private CheckBox chkEnableEditTNSourceBranch;
    @FXML
    private CheckBox chkEnableEditData;

    // переменные - исходные данные для расчета
    @FXML
    private ComboBox comboNameTNPartPred;
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
    private Button btnShowDataTNPart;
    @FXML
    private Button btnOK;

    // Ссылка на главное приложение.
    private Main main;

    private Stage dialogStage;
    private HydraC hydra;
    private boolean okClicked = false;
    String TNBoiler = "";
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
        NewNameTNPart.setText(hydra.getNameTNPartRas());
        comboNameTNPartPred.setValue(hydra.getNameTNPartPred());
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
            hydra.setNameTNPartRas(NewNameTNPart.getText());
            hydra.setNameTNPartPred(comboNameTNPartPred.getValue().toString());
            hydra.setD(Double.parseDouble(DField.getText()));
            hydra.setL(Double.parseDouble(LField.getText()));
            hydra.setG(Double.parseDouble(GField.getText()));
            hydra.setKekv(Double.parseDouble(KekvField.getText()));
            hydra.setGeo(Double.parseDouble(GeoField.getText()));
            hydra.setZdanieEtaj(Double.parseDouble(ZdanieEtajField.getText()));
            //hydra.setHrasp_ist(Double.parseDouble(Hrasp_istField.getText()));
            //обновлене данные в БД
            DerbyDBParser.writePartData(
                    comboTNBoiler.getValue().toString(), comboTNMain.getValue().toString(), comboTNBranch.getValue().toString(),
                    comboNewTNBranch.getValue().toString(), comboTNPart.getValue().toString(), NewNameTNPart.getText(),
                    comboNameTNPartPred.getValue().toString(),
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
        if (NewNameTNPart.getText() == null || NewNameTNPart.getText().length() == 0) {
            alertWarning.setHeaderText("Перемещение участка в другое ответвление,\nCохранить текущее название участка?");
            NewNameTNPart.setText(comboTNPart.getValue().toString());
        }
        warningMessage = ("хотите сохранить название: " + NewNameTNPart.getText() + " ?\n");
        //вывод
        alertWarning.setContentText(warningMessage);
        Optional<ButtonType> result = alertWarning.showAndWait();
        if (result.get() == ButtonType.OK) {
            //проверка правильности введенных данных
            String errorMessage = "";
            if (comboNameTNPartPred.getValue().toString() == null || comboNameTNPartPred.getValue().toString().length() == 0) {
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
        if (comboTNBoiler.isFocused()) {
            //котельная
            comboTNBoiler.getItems().clear();
            DerbyDBParser.dbReadForComboboxBoiler();
            comboTNBoiler.getItems().addAll(DerbyDBParser.listTNBoiler);
            comboTNMain.setDisable(false);
        }
        if (comboTNMain.isFocused()) {
            //магистраль
            comboTNMain.getItems().clear();
            TNBoiler = comboTNBoiler.getValue().toString();
            DerbyDBParser.dbReadForComboboxTNMain(TNBoiler);
            comboTNMain.getItems().addAll(DerbyDBParser.listTNMain);
            comboTNBranch.setDisable(false);
        }
        if (comboTNBranch.isFocused()) {
            //ответвления
            comboTNBranch.getItems().clear();
            TNMain = comboTNMain.getValue().toString();
            DerbyDBParser.dbReadForComboboxTNBranch(TNBoiler, TNMain);
            comboTNBranch.getItems().addAll(DerbyDBParser.listTNBranch);
            comboTNPart.setDisable(false);
        }
        if (comboTNPart.isFocused()) {
            //
            comboNewTNBranch.getItems().clear();
            comboNewTNBranch.getItems().addAll(DerbyDBParser.listTNMain);
            comboNewTNBranch.setValue(comboTNBranch.getValue());
            //участки
            comboTNPart.getItems().clear();
            comboNameTNPartPred.getItems().clear();
            TNBranch = comboTNBranch.getValue().toString();
            DerbyDBParser.dbReadPart(TNBoiler, TNMain, TNBranch);
            comboTNPart.getItems().addAll(DerbyDBParser.listTNPart);
            comboNameTNPartPred.getItems().addAll(DerbyDBParser.listTNPart_All_in_TNBoiler);
            chkEnableEditTNSourceBranch.setDisable(false);
            btnShowDataTNPart.setDisable(false);
            btnOK.setDisable(false);
        }
        if (comboNewTNBranch.isFocused() && chkEnableEditTNSourceBranch.isSelected()) {
            comboNewTNBranch.getItems().clear();
            DerbyDBParser.dbReadForComboboxTNMain(comboTNBoiler.getValue().toString());
            comboNewTNBranch.getItems().addAll(DerbyDBParser.listTNMain);
        }
    }

    /**
    * вызывается при нажатии на combobox
    */
    @FXML
    private void chkEnableAction() {
        //редактирование источника ответвления участка
        if (chkEnableEditTNSourceBranch.isSelected())
            comboNewTNBranch.setDisable(false);
        else
            comboNewTNBranch.setDisable(true);
        //редактирование данных участка
        if (chkEnableEditData.isSelected()) {
            comboNameTNPartPred.setDisable(false);
            DField.setDisable(false);
            LField.setDisable(false);
            GField.setDisable(false);
            KekvField.setDisable(false);
            GeoField.setDisable(false);
            ZdanieEtajField.setDisable(false);
            //Hrasp_istField.setDisable(false);
        }
        else {
            comboNameTNPartPred.setDisable(true);
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
        TNPart = comboTNPart.getValue().toString();
        HydraSolverC hydraPart = DerbyDBParser.parseHydraPartData(TNBoiler,TNMain,TNBranch,TNPart);
        ArrayList<HydraDataClassStruct> hydraPartData = new ArrayList<>();
        hydraPartData.add( new HydraDataClassStruct(
                hydraPart.NamePartTN[0],
                ((hydraPart.NamePartTNpred.length==0)?"":hydraPart.NamePartTNpred[0]),
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
