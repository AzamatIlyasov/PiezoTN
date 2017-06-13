package tn.piezo.controller;

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
public class UIEditBranchController {

    @FXML
    private ComboBox comboBoiler;
    @FXML
    private ComboBox comboMain;
    @FXML
    private ComboBox comboBranch;
    @FXML
    private ComboBox comboNewMain;
    @FXML
    private TextField NewNameBranch;
    @FXML
    private CheckBox chkEnableEditSourceMain;
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
            DerbyDBParser.writeBranchData(comboBranch.getValue().toString(), NewNameBranch.getText(),
                    comboBoiler.getValue().toString(), comboMain.getValue().toString(), comboNewMain.getValue().toString());
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
        if (NewNameBranch.getText() == null || NewNameBranch.getText().length() == 0) {
            alert.setHeaderText("Перемещение ответвления в другую магистраль,\nCохранить текущее название ответвления?");
            NewNameBranch.setText(comboBranch.getValue().toString());
        }
        warningMessage = ("хотите сохранить название: " + NewNameBranch.getText() + " ?\n");
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
            //котельная
            comboBoiler.getItems().clear();
            DerbyDBParser.dbReadForComboboxBoiler();
            comboBoiler.getItems().addAll(DerbyDBParser.listBoiler);
            comboMain.setDisable(false);
        }
        if (comboMain.isFocused()) {
            //магистраль
            comboMain.getItems().clear();
            DerbyDBParser.dbReadForComboboxTNMain(comboBoiler.getValue().toString());
            comboMain.getItems().addAll(DerbyDBParser.listMain);
            comboBranch.setDisable(false);
        }
        if (comboBranch.isFocused()) {
            //
            comboNewMain.getItems().clear();
            comboNewMain.getItems().addAll(DerbyDBParser.listMain);
            comboNewMain.setValue(comboMain.getValue());
            //ответвления
            comboBranch.getItems().clear();
            DerbyDBParser.dbReadForComboboxTNBranch(comboBoiler.getValue().toString(), comboMain.getValue().toString());
            comboBranch.getItems().addAll(DerbyDBParser.listBranch);
            btnOK.setDisable(false);
        }
        if (comboNewMain.isFocused() && chkEnableEditSourceMain.isSelected()) {
            comboNewMain.getItems().clear();
            DerbyDBParser.dbReadForComboboxTNMain(comboBoiler.getValue().toString());
            comboNewMain.getItems().addAll(DerbyDBParser.listMain);
        }
    }
    /**
    * вызывается при нажатии на combobox
    */
    @FXML
    private void chkEnableAction() {
        if (chkEnableEditSourceMain.isSelected())
            comboNewMain.setDisable(false);
        else
            comboNewMain.setDisable(true);
    }

}
