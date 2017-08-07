package tn.piezo.controller;

import javafx.fxml.FXML;
import javafx.scene.image.WritableImage;
import tn.piezo.Main;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * Created by djaza on 16.02.2017.
 * контроллер для работы с RootLayout
 */
public class RootLayoutController {

    // Ссылка на главное приложение.
    private Main main;
    private UIMainGSController UIMainGSController;

    /**
     * Конструктор.
     * Конструктор вызывается раньше метода initialize().
     */
    public RootLayoutController() {
    }

    /**
     * Инициализация класса-контроллера. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Вызывается, когда пользователь кликает по кнопке Пьезометрический график
     * Открывает отображения ПГ.
     */
    @FXML
    private void handlePiezoGraphic() {
        main.showPiezoGraphic();
    }

    /**
     * сохранить график в файл-изображения
     */
    @FXML
    private void savePiezoPlot() throws IOException {
        WritableImage snapShot = UIMainGSController.stackPaneGraph.snapshot(null,null);
        ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(snapShot, null), "png",
                new File("resources/test.png"));
    }

    /**
     * закрыть программу
     */
    @FXML
    private void closeProgram() {

    }

}
