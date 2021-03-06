package tn.piezo.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tn.piezo.Main;
import tn.piezo.model.HydraC;

/**
 * Окно для добавления-редактирования данных в БД.
 *
 * @author Azamat Ilyasov
 */
public class UIAddEditWindowController {

    // Ссылка на главное приложение.
    private Main main;

    private Stage dialogStage;
    private HydraC selectedHydra;
    private Boolean AddMode = false;
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
     * Вызывается главным приложением, которое даёт на себя ссылку.
     * @param main - главное приложение
     */
    public void setMain(Main main) {
        this.main = main;
    }

    /**
     * Задаёт участка, информацию о котором будем менять
     * @param selectedHydra - данные редактируемого участка
     */
    public void setSelectedHydra(HydraC selectedHydra) {
        this.selectedHydra = selectedHydra;
    }

    /**
     * Задает тип окна - редактирование или добавления данных
     * @param selectedMode - true (добавления),false (редактирование)
     */
    public void setSelectedMode(Boolean selectedMode) {
        this.AddMode = selectedMode;
    }

    public void BtnTNBoiler() {
        if (AddMode) {
            main.showAddBoilerDialog();
        }
        else main.showEditBoilerDialog();
    }

    public void BtnTNMain() {
        if (AddMode) {
            main.showAddMainDialog();
        }
        else main.showEditMainDialog();
    }

    public void BtnTNBranch() {
        if (AddMode) {
            main.showAddBranchDialog();
        }
        else main.showEditBranchDialog();
    }

    public void BtnTNPart() {
        if (AddMode) {
            main.showAddPartDialog();
        }
        else main.showEditPartDialog();
    }

    public void BtnTNKolodets() {
        if (AddMode) {
            main.showAddKolodetsDialog();
        }
        else main.showEditKolodetsDialog();
    }

    public void BtnTNConsumer() {
        if (AddMode) {
            main.showAddConsumerDialog();
        }
        else main.showEditConsumerDialog();
    }

}
