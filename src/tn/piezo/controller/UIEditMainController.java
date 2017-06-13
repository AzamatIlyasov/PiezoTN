package tn.piezo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tn.piezo.model.DerbyDBParser;

import java.util.Optional;

/**
 * Окно для редактированая магистрали .
 *
 * @author Azamat Ilyasov
 */
public class UIEditMainController {

    @FXML
    private ComboBox comboBoiler;
    @FXML
    private ComboBox comboMain;
    @FXML
    private ComboBox comboNewBoiler;
    @FXML
    private TextField NewNameTNMain;
    @FXML
    private CheckBox chkEnableEditSourceBoiler;
    @FXML
    private Button btnOK;

    private Stage dialogStage;
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
            DerbyDBParser.writeMainData(comboMain.getValue().toString(), NewNameTNMain.getText(),
                    comboBoiler.getValue().toString(), comboNewBoiler.getValue().toString());

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
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Предупреждение");
        // Показываем сообщение-предупреждение.
        alert.setHeaderText("Перемещение магистрали в другой источник,\nИзменить название?");
        //проверка на изменение название магистрали
        if (NewNameTNMain.getText() == null || NewNameTNMain.getText().length() == 0) {
            alert.setHeaderText("Перемещение магистрали в другой источник,\nCохранить текущее название?");
            NewNameTNMain.setText(comboMain.getValue().toString());
        }
        warningMessage = ("хотите сохранить название: " + NewNameTNMain.getText() + " ?\n");
        //вывод
        alert.setContentText(warningMessage);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            resultFunc = true;
        }
        else {
            resultFunc = false;
        }
        return resultFunc;
    }

    /**
     * вызывается при нажатии на combobox
     */
    @FXML
    private void mouseClickComboBox() {
        // очистка от старых данных и заносим новые данные
        if (comboBoiler.isFocused()) {
            comboBoiler.getItems().clear();
            DerbyDBParser.dbReadForComboboxBoiler();
            comboBoiler.getItems().addAll(DerbyDBParser.listBoiler);
            comboMain.setDisable(false);
        }
        if (comboMain.isFocused()) {
            //
            comboNewBoiler.getItems().clear();
            comboNewBoiler.getItems().addAll(DerbyDBParser.listBoiler);
            comboNewBoiler.setValue(comboBoiler.getValue());
            //магистраль
            comboMain.getItems().clear();
            DerbyDBParser.dbReadForComboboxTNMain(comboBoiler.getValue().toString());
            comboMain.getItems().addAll(DerbyDBParser.listMain);
            btnOK.setDisable(false);
        }
        if (comboNewBoiler.isFocused() && chkEnableEditSourceBoiler.isSelected()) {
            comboNewBoiler.getItems().clear();
            DerbyDBParser.dbReadForComboboxBoiler();
            comboNewBoiler.getItems().addAll(DerbyDBParser.listBoiler);
        }
    }
    /**
    * вызывается при нажатии на combobox
    */
    @FXML
    private void chkEnableAction() {
        if (chkEnableEditSourceBoiler.isSelected())
            comboNewBoiler.setDisable(false);
        else
            comboNewBoiler.setDisable(true);
    }

}
