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
 * Окно для удаления участка.
 *
 * @author Azamat Ilyasov
 */
public class UIDeleteTNPartController {

    @FXML
    private ComboBox comboTNBoiler;
    @FXML
    private ComboBox comboTNMain;
    @FXML
    private ComboBox comboTNBranch;
    @FXML
    private ComboBox comboTNPart;

    // переменные - исходные данные для расчета
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
            DerbyDBParser.deletePartData(
                    TNBoiler, TNMain, TNBranch,
                    comboTNPart.getValue().toString() );
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
        warningMessage = ("Хотите удалить участок -> " + comboTNPart.getValue().toString() +
                " в ответвлении: " + comboTNBranch.getValue().toString() +
                " в магистрали: " + comboTNMain.getValue().toString() +
                " от источника тепла: " + comboTNBoiler.getValue().toString() +  " ?\n");
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
            //ответвления
            comboTNPart.getItems().clear();
            TNBranch = comboTNBranch.getValue().toString();
            DerbyDBParser.dbReadPart(TNBoiler, TNMain, TNBranch);
            comboTNPart.getItems().addAll(DerbyDBParser.listTNPart);
            btnOK.setDisable(false);
        }

    }

}
