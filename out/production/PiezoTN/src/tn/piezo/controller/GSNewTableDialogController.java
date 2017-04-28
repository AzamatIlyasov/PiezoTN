package tn.piezo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tn.piezo.model.DBParser;
import tn.piezo.model.FileParser;
import tn.piezo.model.HydraSolverC;

import java.util.ArrayList;

/**
 * Окно для изменения информации об адресате.
 *
 * @author Azamat Ilyasov
 */
public class GSNewTableDialogController {

    // поля для данных
    private int indexPartTN = 0;
    private String fileName = "";
    private String sourceName = "";
    private String tnName = "";
    private String branchTNName = "";
    // переменные - исходные данные для расчета
    private String[] NamePartTN;
    private String[] NamePartTNpred;
    private double[] D;
    private double[] L;
    private double[] G;
    private double[] Kekv;
    private double Hrasp_ist;
    private double[] Geo;
    private double[] ZdanieEtaj;
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
    private boolean okClicked = false;

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        // инициализация combobox - выбор источника
        listSourceTN.getItems().add("New");
        listSourceTN.getItems().addAll(DBParser.listSourse);
        // инициализация combobox - выбор тепловой сети
        DBParser.dbReadForComboboxTNMain(listSourceTN.getValue().toString());
        listTN.getItems().add("New");
        listTN.getItems().addAll(DBParser.listTN);
        // инициализация combobox - выбор ответвления тепловой сети
        DBParser.dbReadForComboboxTNBranch(listSourceTN.getValue().toString(), listTN.getValue().toString());
        listBranchingOfTN.getItems().add("New");
        listBranchingOfTN.getItems().add("Без ответвления");
        listBranchingOfTN.getItems().addAll(DBParser.listBranchTN);

    }

    /**
     * Устанавливает сцену для этого окна.
     *
     * @param dialogStage - диалоговое окно
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        // Устанавливаем иконку для диалгового окна
        this.dialogStage.getIcons().add(new Image("file:resources/images/Edit1.png"));
    }

    /**
     * имя файла для сохранения
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns true, если пользователь кликнул OK, в другом случае false.
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке OK.
     */
    @FXML
    private void handleOk() {
        //сохраняем данные в массивах
        editDataHydra(indexPartTN);
        okClicked = true;
        dialogStage.close();
        fileName = "input" + "-" + sourceName + "-" + tnName + "-" + branchTNName;
    }

    /**
     * Количество участков изменилось
     */
    @FXML
    private void countChanged() {
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
            //сохраняем данные в массивах
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
            //сохраняем тек информацию
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
        if (IndexPartTN.getText() == null || IndexPartTN.getText().length() == 0 || Double.parseDouble(IndexPartTN.getText()) < 0 ) {
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
    private void handleTNSource() {
        if (listSourceTN.getValue().toString().equals("New")) {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setHeaderText("Введите название источника тепла");
            inputDialog.setTitle("Название источника");
            inputDialog.showAndWait();
            sourceName = inputDialog.getResult();
            listSourceTN.getItems().add(sourceName);
            listSourceTN.setValue(sourceName);
            //через БД
            DBParser.listSourse.add(sourceName);
            //через текстовый файл парсер
            //FileParser.listSourse.add(sourceName);
        }
        else {
            sourceName = listSourceTN.getValue().toString();
        }
    }

    /**
     * Выбор/создание тепловой сети
     */
    @FXML
    private void handleTermalNet() {
        if (listTN.getValue().toString().equals("New")) {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setHeaderText("Введите название тепловой сети");
            inputDialog.setTitle("Название тепловой сети");
            inputDialog.showAndWait();
            tnName = inputDialog.getResult();
            listTN.getItems().add(tnName);
            listTN.setValue(tnName);
            //через БД
            DBParser.listTN.add(tnName);
            //через текстовый файл парсер
            //FileParser.listTN.add(tnName);
        }
        else {
            tnName = listTN.getValue().toString();
        }
    }

    /**
     * Выбор/создание тепловой сети
     */
    @FXML
    private void handleTNPart() {
        if (listBranchingOfTN.getValue().toString().equals("New")) {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setHeaderText("Введите название ответвления");
            inputDialog.setTitle("Название ответвления");
            inputDialog.showAndWait();
            branchTNName = inputDialog.getResult();
            listBranchingOfTN.getItems().add(branchTNName);
            listBranchingOfTN.setValue(branchTNName);
            FileParser.listBranchTN.add(branchTNName);
        }
        else {
            branchTNName = listBranchingOfTN.getValue().toString();
        }
    }

    /**
     * Метод для добавления и сохранения введеных данных в ArrayList
     * @return ArrayList - hydraArray
     */
    public ArrayList getNewHydraData() {
        // ГР
        HydraSolverC hydraPartTN = new HydraSolverC(NamePartTN, NamePartTNpred, D, L, G, Kekv, Geo, ZdanieEtaj, Hrasp_ist);
        return hydraPartTN.HydraPartTN(hydraPartTN);
    }



}
