package tn.piezo.controller.TNBranch;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tn.piezo.model.DerbyDBParser;
import tn.piezo.model.HydraC;

/**
 * Окно для добавления новой котельной .
 *
 * @author Azamat Ilyasov
 */
public class UIDeleteTNBranchController {

    @FXML
    private ComboBox comboTNBoiler;
    @FXML
    private ComboBox comboTNMain;
    @FXML
    private TextField NameTNBranch;
    @FXML
    private TextField Hrasp_ist;
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
            DerbyDBParser.writeAddBranchData(comboTNBoiler.getValue().toString(),comboTNMain.getValue().toString(),
                    NameTNBranch.getText(),Double.parseDouble(Hrasp_ist.getText()) );

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
        if (NameTNBranch.getText() == null || NameTNBranch.getText().length() == 0) {
            errorMessage += "Неправильное название ответвления!\n";
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

    @FXML
    private void comboMainMClick() {
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
            NameTNBranch.setDisable(false);
            Hrasp_ist.setDisable(false);
        }

    }

    @FXML
    private void txtTNBranchChange() {
        btnOK.setDisable(false);
    }

}
