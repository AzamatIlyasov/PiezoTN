package tn.piezo.controller.TNMain;

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
public class UIEditTNMainController {

    @FXML
    private ComboBox comboTNBoiler;
    @FXML
    private ComboBox comboTNMain;
    @FXML
    private ComboBox comboNewTNBoiler;
    @FXML
    private TextField NewNameTNMain;
    @FXML
    private CheckBox chkEnableEditTNSourceBoiler;
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
            DerbyDBParser.writeMainData(comboTNMain.getValue().toString(), NewNameTNMain.getText(),
                    comboTNBoiler.getValue().toString(), comboNewTNBoiler.getValue().toString());

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
            NewNameTNMain.setText(comboTNMain.getValue().toString());
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
        if (comboTNBoiler.isFocused()) {
            comboTNBoiler.getItems().clear();
            DerbyDBParser.dbReadForComboboxBoiler();
            comboTNBoiler.getItems().addAll(DerbyDBParser.listTNBoiler);
            comboTNMain.setDisable(false);
        }
        if (comboTNMain.isFocused()) {
            //
            comboNewTNBoiler.getItems().clear();
            comboNewTNBoiler.getItems().addAll(DerbyDBParser.listTNBoiler);
            comboNewTNBoiler.setValue(comboTNBoiler.getValue());
            //магистраль
            comboTNMain.getItems().clear();
            DerbyDBParser.dbReadForComboboxTNMain(comboTNBoiler.getValue().toString());
            comboTNMain.getItems().addAll(DerbyDBParser.listTNMain);
            btnOK.setDisable(false);
        }
        if (comboNewTNBoiler.isFocused() && chkEnableEditTNSourceBoiler.isSelected()) {
            comboNewTNBoiler.getItems().clear();
            DerbyDBParser.dbReadForComboboxBoiler();
            comboNewTNBoiler.getItems().addAll(DerbyDBParser.listTNBoiler);
        }
    }
    /**
    * вызывается при нажатии на combobox
    */
    @FXML
    private void chkEnableAction() {
        if (chkEnableEditTNSourceBoiler.isSelected())
            comboNewTNBoiler.setDisable(false);
        else
            comboNewTNBoiler.setDisable(true);
    }

}
