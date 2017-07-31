package tn.piezo.controller.TNBranch;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tn.piezo.model.DerbyDBParser;

import java.util.Optional;

/**
 * Окно для редактированая ответвления .
 *
 * @author Azamat Ilyasov
 */
public class UIEditTNBranchController {

    @FXML
    private ComboBox comboTNBoiler;
    @FXML
    private ComboBox comboTNMain;
    @FXML
    private ComboBox comboTNBranch;
    @FXML
    private ComboBox comboNewTNMain;
    @FXML
    private TextField NewNameTNBranch;
    @FXML
    private CheckBox chkEnableEditTNSourceMain;
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
            DerbyDBParser.writeBranchData(comboTNBranch.getValue().toString(), NewNameTNBranch.getText(),
                    comboTNBoiler.getValue().toString(), comboTNMain.getValue().toString(), comboNewTNMain.getValue().toString());
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
        alert.setHeaderText("Перемещение ответвления в другую магистраль,\nИзменить название?");
        //проверка на изменение название магистрали
        if (NewNameTNBranch.getText() == null || NewNameTNBranch.getText().length() == 0) {
            alert.setHeaderText("Перемещение ответвления в другую магистраль,\nCохранить текущее название ответвления?");
            NewNameTNBranch.setText(comboTNBranch.getValue().toString());
        }
        warningMessage = ("хотите сохранить название: " + NewNameTNBranch.getText() + " ?\n");
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
            //котельная
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
            comboTNBranch.setDisable(false);
        }
        if (comboTNBranch.isFocused()) {
            //
            comboNewTNMain.getItems().clear();
            comboNewTNMain.getItems().addAll(DerbyDBParser.listTNMain);
            comboNewTNMain.setValue(comboTNMain.getValue());
            //ответвления
            comboTNBranch.getItems().clear();
            DerbyDBParser.dbReadForComboboxTNBranch(comboTNBoiler.getValue().toString(), comboTNMain.getValue().toString());
            comboTNBranch.getItems().addAll(DerbyDBParser.listTNBranch);
            btnOK.setDisable(false);
        }
        if (comboNewTNMain.isFocused() && chkEnableEditTNSourceMain.isSelected()) {
            comboNewTNMain.getItems().clear();
            DerbyDBParser.dbReadForComboboxTNMain(comboTNBoiler.getValue().toString());
            comboNewTNMain.getItems().addAll(DerbyDBParser.listTNMain);
        }
    }
    /**
    * вызывается при нажатии на combobox
    */
    @FXML
    private void chkEnableAction() {
        if (chkEnableEditTNSourceMain.isSelected())
            comboNewTNMain.setDisable(false);
        else
            comboNewTNMain.setDisable(true);
    }

}
