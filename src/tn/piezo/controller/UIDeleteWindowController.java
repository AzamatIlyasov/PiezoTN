package tn.piezo.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tn.piezo.Main;
import tn.piezo.model.HydraC;

/**
 * Окно для удаления данных в БД.
 *
 * @author Azamat Ilyasov
 */
public class UIDeleteWindowController {

    // Ссылка на главное приложение.
    private Main main;

    private Stage dialogStage;
    private HydraC selectedHydra;
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

    public void BtnTNBoiler() {
        main.showDeleteBoilerDialog();
    }

    public void BtnTNMain() {
        main.showDeleteMainDialog();
    }

    public void BtnTNBranch() {
        main.showDeleteBranchDialog();
    }

    public void BtnTNPart() {
        main.showDeletePartDialog();
    }

    public void BtnTNKolodets() {
        main.showDeleteKolodetsDialog();
    }

    public void BtnTNConsumer() {
        main.showDeleteConsumerDialog();
    }
}
