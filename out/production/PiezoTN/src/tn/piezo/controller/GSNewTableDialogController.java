package tn.piezo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tn.piezo.Main;
import tn.piezo.model.HydraC;
import tn.piezo.model.HydraDataClassStruct;
import tn.piezo.model.HydraSolverC;

import java.util.ArrayList;

/**
 * Окно для изменения информации об адресате.
 *
 * @author Azamat Ilyasov
 */
public class GSNewTableDialogController {

    // поля для данных
    int indexPartTN = 0;
    // переменные - исходные данные для расчета
    String[] NamePartTN;
    String[] NamePartTNpred;
    double[] D;
    double[] L;
    double[] G;
    double[] Kekv;
    double Hrasp_ist;
    double[] Geo;
    double[] ZdanieEtaj;
    // поля для
    @FXML
    private TextField NamePartTNField;
    @FXML
    private TextField NamePartTNpredField;
    @FXML
    private TextField DField;
    @FXML
    private TextField LField;
    @FXML
    private TextField GField;
    @FXML
    private TextField KekvField;
    @FXML
    private TextField GeoField;
    @FXML
    private TextField ZdanieEtajField;
    @FXML
    private TextField Hrasp_istField;
    @FXML
    private TextField CountOfTN;
    @FXML
    private TextField IndexPartTN;
    @FXML
    private ComboBox listSourceTN;
    @FXML
    private ComboBox listBranchingOfTN;
    @FXML
    private ComboBox listTN;

    private Stage dialogStage;
    private HydraC hydraC;
    ArrayList hydraArray = new ArrayList();
    private boolean okClicked = false;
    // Ссылка на главное приложение.
    private Main main;

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
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        // Устанавливаем иконку для диалгового окна
        this.dialogStage.getIcons().add(new Image("file:resources/images/Edit1.png"));
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
     * Вызывается, когда пользователь кликнул по кнопке OK.
     */
    @FXML
    private void handleOk() {
        saveHydraData();
        okClicked = true;
        dialogStage.close();
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке Cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Добавление-создание следующего участка
     */
    @FXML
    private void handleNext() {
        if (isInputValid()) {
            //инициализация массивов для исх данных
            int n = Integer.parseInt(CountOfTN.getText());
            NamePartTN = new String[n];
            NamePartTNpred = new String[n];
            D = new double[n];
            L = new double[n];
            G = new double[n];
            Kekv = new double[n];
            Geo = new double[n];
            ZdanieEtaj = new double[n];
            editDataHydra(indexPartTN);
            // очищаем диалоговое окно
            clearDialogStage();
            indexPartTN++;
            IndexPartTN.setText(Integer.toString(indexPartTN));
            Hrasp_istField.setText(Double.toString(Hrasp_ist));
        }
    }

    /**
     * добавления и редактирование новой таблицы
     */
    private void editDataHydra(int ind) {
        NamePartTN[ind] = (NamePartTNField.getText());
        NamePartTNpred[ind]=(NamePartTNpredField.getText());
        D[ind]=(Double.parseDouble(DField.getText()));
        L[ind]=(Double.parseDouble(LField.getText()));
        G[ind]=(Double.parseDouble(GField.getText()));
        Kekv[ind]=(Double.parseDouble(KekvField.getText()));
        Geo[ind]=(Double.parseDouble(GeoField.getText()));
        ZdanieEtaj[ind]=(Double.parseDouble(ZdanieEtajField.getText()));
        Hrasp_ist=(Double.parseDouble(Hrasp_istField.getText()));
    }

    /**
     * Возврат-редактирование предыдущего участка
     */
    @FXML
    private void handlePrevious() {
        if (isInputValid()) {
            //инициализация массивов для исх данных
            int n = Integer.parseInt(CountOfTN.getText());
            NamePartTN = new String[n];
            NamePartTNpred = new String[n];
            D = new double[n];
            L = new double[n];
            G = new double[n];
            Kekv = new double[n];
            Geo = new double[n];
            ZdanieEtaj = new double[n];
            editDataHydra(indexPartTN);
            // очищаем диалоговое окно
            clearDialogStage();
            indexPartTN--;
            IndexPartTN.setText(Integer.toString(indexPartTN));
            Hrasp_istField.setText(Double.toString(Hrasp_ist));
        }
    }

    /**
     * Очистка полей в окне
     */
    private void clearDialogStage() {
        NamePartTNField.clear();
        NamePartTNpredField.clear();
        DField.clear();
        LField.clear();
        GField.clear();
        KekvField.clear();
        GeoField.clear();
        ZdanieEtajField.clear();
        Hrasp_istField.clear();
    }

    /**
     * Проверяет пользовательский ввод в текстовых полях.
     *
     * @return true, если пользовательский ввод корректен
     */
    private boolean isInputValid() {
        String errorMessage = "";
        if (NamePartTNField.getText() == null || NamePartTNField.getText().length() == 0) {
            errorMessage += "Неправильное название расчетного участка!\n";
        }
        if (NamePartTNpredField.getText() == null || NamePartTNpredField.getText().length() == 0) {
            errorMessage += "Неправильное название предыдущего участка!\n";
        }
        if (DField.getText() == null || DField.getText().length() == 0 || Double.parseDouble(DField.getText()) <1 ) {
            errorMessage += "Неправильный диаметр участка!\n Диаметр необходимо ввести в мм\n";
        }
        if (LField.getText() == null || LField.getText().length() == 0 || Double.parseDouble(LField.getText()) <=0.5 ) {
            errorMessage += "Неправильная длина участка!\n Длину необходимо ввести в м\n";
        }
        if (GField.getText() == null || GField.getText().length() == 0 || Double.parseDouble(GField.getText()) <=0 ) {
            errorMessage += "Неправильный расход участка!\n Расход необходимо ввести в т/ч\n";
        }
        if (KekvField.getText() == null || KekvField.getText().length() == 0 || Double.parseDouble(KekvField.getText()) <=0 ) {
            errorMessage += "Неправильный коэффициент шероховатости участка!\n К-т шероховатости необходимо ввести в мм\n";
        }
        if (GeoField.getText() == null || GeoField.getText().length() == 0 || Double.parseDouble(KekvField.getText()) <=0 ) {
            errorMessage += "Неправильная геодезическая отметка участка!\n Геодезическую отметку необходимо ввести в м\n";
        }
        if (ZdanieEtajField.getText() == null || ZdanieEtajField.getText().length() == 0 || Double.parseDouble(ZdanieEtajField.getText()) < 0 ) {
            errorMessage += "Неправильная этажность здания участка!\n Этажность здания необходимо ввести в штуках ( 1 этаж, 2 этажа, 3 этажа и тд)\n";
        }
        if (Hrasp_istField.getText() == null || Hrasp_istField.getText().length() == 0 || Double.parseDouble(Hrasp_istField.getText()) <= 0 ) {
            errorMessage += "Неправильный напор у источника!\n Напор у источника необходимо ввести в м\n";
        }
        if (CountOfTN.getText() == null || CountOfTN.getText().length() == 0 || Double.parseDouble(CountOfTN.getText()) <= 0 ) {
            errorMessage += "Неправильный ввод данных - укажите корректное количество участков!\n ";
        }
        if (IndexPartTN.getText() == null || IndexPartTN.getText().length() == 0 || Double.parseDouble(IndexPartTN.getText()) <= 0 ) {
            errorMessage += "Неправильный ввод данных - укажите корректный номер (индекс) редактируемого участка!\n ";
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

    /**
     * Выбор/создание источника тепла
     */
    @FXML
    private void handleTNSource() {}

    /**
     * Выбор/создание тепловой сети
     */
    @FXML
    private void handleTermalNet() {}

    /**
     * Выбор/создание тепловой сети
     */
    @FXML
    private void handleTNPart() {}

    /**
     * Метод для добавления и сохранения введеных данных в ArrayList
     */
    private void saveHydraData() {
        // ГР
        HydraSolverC hydraPartTN = new HydraSolverC(NamePartTN, NamePartTNpred, D, L, G, Kekv, Geo, ZdanieEtaj, Hrasp_ist);
        hydraArray = hydraPartTN.HydraPartTN(hydraPartTN);
        main.setHydraData(hydraArray);
    }

}
