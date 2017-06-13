package tn.piezo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tn.piezo.model.DerbyDBParser;
import tn.piezo.model.HydraC;

import java.util.ArrayList;

/**
 * Окно для изменения информации об адресате.
 *
 * @author Azamat Ilyasov
 */
public class GSEditDialogController {

    // переменные - исходные данные для расчета
    @FXML
    private TextField NamePartTNField;
    @FXML
    private TextField NamePartTNpredField;
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
    private TextField Hrasp_istField;

    private Stage dialogStage;
    private HydraC hydra;
    private boolean okClicked = false;

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Устанавливает сцену для этого окна.
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
        this.hydra = hydra;

        NamePartTNField.setText(hydra.getNamePartTN());
        NamePartTNpredField.setText(hydra.getNamePartTNpred());
        DField.setText(hydra.getD().toString());
        LField.setText(hydra.getL().toString());
        GField.setText(hydra.getG().toString());
        KekvField.setText(hydra.getKekv().toString());
        GeoField.setText(hydra.getGeo().toString());
        ZdanieEtajField.setText(hydra.getZdanieEtaj().toString());
        Hrasp_istField.setText(hydra.getHrasp_ist().toString());

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
            hydra.setNamePartTN(NamePartTNField.getText());
            hydra.setNamePartTNpred(NamePartTNpredField.getText());
            hydra.setD(Double.parseDouble(DField.getText()));
            hydra.setL(Double.parseDouble(LField.getText()));
            hydra.setG(Double.parseDouble(GField.getText()));
            hydra.setKekv(Double.parseDouble(KekvField.getText()));
            hydra.setGeo(Double.parseDouble(GeoField.getText()));
            hydra.setZdanieEtaj(Double.parseDouble(ZdanieEtajField.getText()));
            hydra.setHrasp_ist(Double.parseDouble(Hrasp_istField.getText()));

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
        String errorMessage = "";
        if (NamePartTNField.getText() == null || NamePartTNField.getText().length() == 0) {
            errorMessage += "Неправильное название расчетного участка!\n";
        }
        if (NamePartTNpredField.getText() == null || NamePartTNpredField.getText().length() == 0) {
            errorMessage += "Неправильное название предыдущего участка!\n";
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
        if (Hrasp_istField.getText() == null || Hrasp_istField.getText().length() == 0 || Double.parseDouble(Hrasp_istField.getText()) <= 0 ) {
            errorMessage += "Неправильный напор у источника!\n Напор у источника необходимо ввести в м";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Показываем сообщение об ошибке.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

}
