package tn.piezo.controller.TNMain;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tn.piezo.model.DerbyDBParser;
import tn.piezo.model.HydraC;

import java.util.Optional;

/**
 * Окно для удаления магистральной .
 *
 * @author Azamat Ilyasov
 */
public class UIDeleteTNMainController {

    @FXML
    private ComboBox comboTNBoiler;
    @FXML
    private ComboBox comboTNMain;
    @FXML
    private Button btnOK;

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
            DerbyDBParser.deleteMainData(comboTNBoiler.getValue().toString(), comboTNMain.getValue().toString());
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
     * Предупреждает пользователя о удаленни данных
     *
     * @return true, если пользовательский ввод корректен
     */
    private boolean isInputValid() {
        String warningMessage = "";
        Boolean resultFunc = false;
        Alert alertWarning = new Alert(AlertType.CONFIRMATION);
        alertWarning.initOwner(dialogStage);
        alertWarning.setTitle("Предупреждение, удаление данных");
        warningMessage = ("Хотите удалить магистраль -> " + comboTNMain.getValue().toString()
                + " от источника тепла: " + comboTNBoiler.getValue().toString() +  " ?\n");
        //вывод
        alertWarning.setContentText(warningMessage);
        Optional<ButtonType> result = alertWarning.showAndWait();
        if (result.get() == ButtonType.OK) {
            resultFunc =  true;
        }
        else {
            resultFunc = false;
        }
        //выход
        return resultFunc;
    }

    @FXML
    private void comboSourceMClick() {
        // очистка от старых данных и заносим новые данные
        if (comboTNBoiler.isFocused()) {
            comboTNBoiler.getItems().clear();
            DerbyDBParser.dbReadForComboboxBoiler();
            comboTNBoiler.getItems().addAll(DerbyDBParser.listTNBoiler);
            comboTNMain.setDisable(false);
        }
        if (comboTNMain.isFocused()) {
            //магистраль
            comboTNMain.getItems().clear();
            DerbyDBParser.dbReadForComboboxTNMain(comboTNBoiler.getValue().toString());
            comboTNMain.getItems().addAll(DerbyDBParser.listTNMain);
            btnOK.setDisable(false);
        }

    }

}
