package tn.piezo.controller.TNPart;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tn.piezo.Main;
import tn.piezo.model.DerbyDBParser;
import tn.piezo.model.HydraC;

import java.util.Optional;

/**
 * Окно для редактированая участка.
 *
 * @author Azamat Ilyasov
 */
public class UIAddTNPartController {

    @FXML
    private ComboBox comboTNBoiler;
    @FXML
    private ComboBox comboTNMain;
    @FXML
    private ComboBox comboTNBranch;
    @FXML
    private TextField NameTNPartRas;
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
            DerbyDBParser.writeAddPartData(
                    TNBoiler, TNMain, TNBranch,
                    NameTNPartRas.getText(),
                    ((comboNameTNPartPred.getItems().isEmpty())?"":comboNameTNPartPred.getValue().toString()),
                    Double.parseDouble(DField.getText()),
                    Double.parseDouble(LField.getText()),
                    Double.parseDouble(GField.getText()),
                    Double.parseDouble(KekvField.getText()),
                    Double.parseDouble(GeoField.getText()),
                    Double.parseDouble(ZdanieEtajField.getText()) );
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
        warningMessage = ("хотите использовать название: " + NameTNPartRas.getText() + " ?\n");
        //вывод
        alertWarning.setContentText(warningMessage);
        Optional<ButtonType> result = alertWarning.showAndWait();
        if (result.get() == ButtonType.OK) {
            //проверка правильности введенных данных
            String errorMessage = "";
            //if (comboNameTNPartPred.getValue().toString() == null || comboNameTNPartPred.getValue().toString().length() == 0) {
            //    errorMessage += "Не выбран предыдущий участок!\n";
            //}
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
            NameTNPartRas.setDisable(false);
        }
        /*
        if (comboNameTNPartPred.isFocused()) {
            comboNameTNPartPred.getItems().clear();
            comboNameTNPartPred.getItems().addAll(DerbyDBParser.listTNPart_All_in_TNBoiler);
        }
*/
    }

    /**
    * вызывается при внесение изменения в название участка
    */
    @FXML
    private void NamePartTxtChange() {
        //редактирование данных нового участка
        if (!NameTNPartRas.getText().isEmpty()) {
            //участки
            TNBranch = comboTNBranch.getValue().toString();
            DerbyDBParser.dbReadPart(TNBoiler, TNMain, TNBranch);
            comboNameTNPartPred.getItems().clear();
            comboNameTNPartPred.getItems().addAll(DerbyDBParser.listTNPart);

            comboNameTNPartPred.setDisable(false);
            DField.setDisable(false);
            LField.setDisable(false);
            GField.setDisable(false);
            KekvField.setDisable(false);
            GeoField.setDisable(false);
            ZdanieEtajField.setDisable(false);
            btnOK.setDisable(false);
        }
        else {
            comboNameTNPartPred.setDisable(true);
            DField.setDisable(true);
            LField.setDisable(true);
            GField.setDisable(true);
            KekvField.setDisable(true);
            GeoField.setDisable(true);
            ZdanieEtajField.setDisable(true);
        }
    }

}
