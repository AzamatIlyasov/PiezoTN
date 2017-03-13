package tn.piezo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.piezo.Main;
import tn.piezo.model.HydraC;

/**
 * Created by djaza on 16.02.2017.
 */
public class GSOverviewController {

    @FXML
    private TableView<HydraC> HydraOverviewTable;
    // переменные - исходные данные для расчета
    @FXML
    private TableColumn<HydraC, Integer> Number_column; //нумерация участков
    @FXML
    private TableColumn<HydraC, String> NamePartTN_column;
    @FXML
    private TableColumn<HydraC, String> NamePartTNpred_column;
    @FXML
    private TableColumn<HydraC, Double> D_column;
    @FXML
    private TableColumn<HydraC, Double> L_column;
    @FXML
    private Label NamePartTNLabel;
    @FXML
    private Label NamePartTNpredLabel;
    @FXML
    private Label DLabel;
    @FXML
    private Label LLabel;
    @FXML
    private Label GLabel;
    @FXML
    private Label KekvLabel;
    @FXML
    private Label GeoLabel;
    @FXML
    private Label ZdanieEtajLabel;
    @FXML
    private Label Hrasp_istLabel;

    private Stage dialogStage;
    private HydraC hydra;
    private boolean okClicked = false;

    // Ссылка на главное приложение.
    private Main main;

    /**
     * Конструктор.
     * Конструктор вызывается раньше метода initialize().
     */
    public GSOverviewController() {
    }

    /**
     * Инициализация класса-контроллера. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        // Инициализация таблицы
        Number_column.setCellValueFactory(cellData -> cellData.getValue().NumProperty().asObject());
        NamePartTN_column.setCellValueFactory(cellData -> cellData.getValue().NamePartTNProperty());
        NamePartTNpred_column.setCellValueFactory(cellData -> cellData.getValue().NamePartTNpredProperty());
        D_column.setCellValueFactory(cellData -> cellData.getValue().DProperty().asObject());
        L_column.setCellValueFactory(cellData -> cellData.getValue().LProperty().asObject());

        showHydraDetails(null);

        // слушаем изменения выбора, и при изменении отображаем
        // дополнительную информацию об участке
        HydraOverviewTable.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> showHydraDetails(newValue))
        );

    }

    /**
     * Вызывается главным приложением, которое даёт на себя ссылку.
     *
     * @param main
     */
    public void setMain(Main main) {
        this.main = main;

        // Добавление в таблицу данных из наблюдаемого списка
        HydraOverviewTable.setItems(main.getHydraData());
    }

    /**
     * Заполняет все текстовые поля, отображая подробности об участке
     *
     * @param hydra - участок типа HydraC
     */
    private void showHydraDetails(HydraC hydra)
    {
        if (hydra != null)
        {
            // Заполняем метки информацией из объекта hydra
            NamePartTNLabel.setText(hydra.getNamePartTN());
            NamePartTNpredLabel.setText(hydra.getNamePartTNpred());
            DLabel.setText(hydra.getD().toString());
            LLabel.setText(hydra.getL().toString());
            GLabel.setText(hydra.getG().toString());
            KekvLabel.setText(hydra.getKekv().toString());
            GeoLabel.setText(hydra.getGeo().toString());
            ZdanieEtajLabel.setText(hydra.getZdanieEtaj().toString());
            Hrasp_istLabel.setText(hydra.getHrasp_ist().toString());
        }
        else
        {
            // если hydra - null, то убираем весь текст
            NamePartTNLabel.setText("");
            NamePartTNpredLabel.setText("");
            DLabel.setText("");
            LLabel.setText("");
            GLabel.setText("");
            KekvLabel.setText("");
            GeoLabel.setText("");
            ZdanieEtajLabel.setText("");
            Hrasp_istLabel.setText("");
        }
    }

    /**
     * Кнопка delete (удаление выбранного участка)
     */
    @FXML
    private void handleDeleteHydra()
    {
        int selectedIndex = HydraOverviewTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >=0)
        {
            Alert alertDel = new Alert(Alert.AlertType.CONFIRMATION);
            alertDel.setTitle("Запрос на удаления");
            alertDel.setHeaderText("Вы действительно хотите удалить участок?");
            alertDel.setContentText("Нажмите Cancel для отмены");
            alertDel.showAndWait();
            if (alertDel.getResult().getText().equals("OK"))
                //если да то удаляем строку
                HydraOverviewTable.getItems().remove(selectedIndex);
        }
        else
        {
            //ничего не выбрано
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("НЕТ ВЫДЕЛЕННОГО");
            alert.setHeaderText("Не выбран участок");
            alert.setContentText("Пожалуйста, выберите участок в таблице");

            alert.showAndWait();
        }
    }

    /**
     * Вызывается, когда пользователь кликает по кнопке New... (добавить)
     * Открывает диалоговое окно с дополнительной информацией нового участка.
     */
    @FXML
    private void handleNewHydraPart() {
        HydraC tempHydra = new HydraC();
        boolean okClicked = main.showTPartEditDialog(tempHydra);
        if (okClicked) {
            main.getHydraData().add(tempHydra);
        }
    }

    /**
     * Вызывается, когда пользователь кликает по кнопке New... (добавить новую таблицу)
     * Открывает диалоговое окно для создания новой гидравлической таблицы.
     */
    @FXML
    private void handleNewTermalNetwork() {
        HydraC tempHydra = new HydraC();
        boolean okClicked = main.showGSNewTableDialog(tempHydra);
        if (okClicked) {
            main.getHydraData().add(tempHydra);
        }

    }

    /**
     * Вызывается, когда пользователь кликает по кнопка Edit... (изменить)
     * Открывает диалоговое окно для изменения выбранного участка.
     */
    @FXML
    private void handleEditHydraPart() {
        HydraC selectedHydra = HydraOverviewTable.getSelectionModel().getSelectedItem();
        if (selectedHydra != null) {
            boolean okClicked = main.showTPartEditDialog(selectedHydra);
            if (okClicked) {
                showHydraDetails(selectedHydra);
            }

        } else {
            // Ничего не выбрано.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("НЕТ ВЫДЕЛЕННОГО");
            alert.setHeaderText("Не выбран участок");
            alert.setContentText("Пожалуйста, выберите участок в таблице.");

            alert.showAndWait();
        }
    }

    /**
     * Устанавливает сцену для этого окна.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Returns true, если пользователь кликнул OK, в другом случае false.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке Cancel.
     */
    @FXML
    private void handleCancel() {
        main.showGSMainOverview();
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке OK.
     */
    @FXML
    private void handleDone() {
            okClicked = true;
            main.showGSMainOverview();
    }

}
