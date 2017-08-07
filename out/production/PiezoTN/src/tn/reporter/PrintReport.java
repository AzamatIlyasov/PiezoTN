package tn.reporter;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.EventHandler;
import java.io.Closeable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.AncestorListener;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import tn.piezo.model.DerbyDBParser;
import tn.piezo.model.HydraDataClassStruct;

import static com.sun.deploy.uitoolkit.ToolkitStore.dispose;

public class PrintReport extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public Image piezoPlotImage;

    private ArrayList hydraDataArrayList;

    //основной отчет
    public void showReportGeneral() throws JRException, ClassNotFoundException, SQLException {
                   /* Output file location */
        try {
            //String outputFile = "resources/JasperTableExample.pdf";

            /* List to hold Items */
            List<Item> listItems = new ArrayList<Item>();

            /* Create Items */
            Item[] itemsTN = new Item[hydraDataArrayList.size()];
            for (int i = 0; i < hydraDataArrayList.size(); i++) {
                itemsTN[i] = new Item();
                itemsTN[i].setName(((HydraDataClassStruct) hydraDataArrayList.get(i)).NamePartTN);
                itemsTN[i].setLength(((HydraDataClassStruct) hydraDataArrayList.get(i)).L);
                itemsTN[i].setFlow(((HydraDataClassStruct) hydraDataArrayList.get(i)).G);
                itemsTN[i].setDiametr(((HydraDataClassStruct) hydraDataArrayList.get(i)).D);
                itemsTN[i].setHrasp(((HydraDataClassStruct) hydraDataArrayList.get(i)).Hrasp_endP);
                itemsTN[i].setdhpoteri(((HydraDataClassStruct) hydraDataArrayList.get(i)).dH_fist);
                listItems.add(itemsTN[i]);
            }

            /* Convert List to JRBeanCollectionDataSource */
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(listItems);

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("logoImage", getPicture("resources/images/dynamicreports.png"));
            parameters.put("piezoPlot", getPicture("resources/test.png"));
            parameters.put("BoilerName", ((HydraDataClassStruct) hydraDataArrayList.get(0)).BoilerName);
            parameters.put("MainName", ((HydraDataClassStruct) hydraDataArrayList.get(0)).MainName);
            parameters.put("BranchName", ((HydraDataClassStruct) hydraDataArrayList.get(0)).BranchName);
            parameters.put("TextAnalisePG", DerbyDBParser.TextAnalizePiezoPlot);
            parameters.put("ItemDataSource", itemsJRBean);
            /* Using compiled version(.jasper) of Jasper report to generate PDF */

            JasperPrint jasperPrint = JasperFillManager.fillReport("resources/template_HydraTable.jasper",
                    parameters, new JREmptyDataSource());

            JRViewer viewer = new JRViewer(jasperPrint);
            WindowListener winListener = new TestWindowListener();
            // прослушка событий окон (корректно зарываем окно)
            this.addWindowListener(winListener);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            this.add(viewer);
            this.setSize(700, 500);
            this.setVisible(true);
            System.out.println("View Generated");
            //очиста буфера изображения
            piezoPlotImage.flush();

        } catch (JRException ex) {
            ex.printStackTrace();
        }

    }

    private Image getPicture(String src) {
        ImageIcon icon = new ImageIcon(src);
        piezoPlotImage = icon.getImage();
        return piezoPlotImage;
    }

    public void setHydraData(ArrayList hydraDataArrayList) {
        this.hydraDataArrayList = hydraDataArrayList;
    }

}

class TestWindowListener implements WindowListener {
    public void windowActivated(WindowEvent e) {
        System.out.println("windowActivated()");
    }
    public void windowClosed(WindowEvent e) {
        System.out.println("windowClosed()");
    }
    public void windowClosing(WindowEvent e) {
        System.out.println("windowClosing()");
        try {
            onExit();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
    public void windowDeactivated(WindowEvent e) {
        System.out.println("windowDeactivated()");
    }
    public void windowDeiconified(WindowEvent e) {
        System.out.println("windowDeiconified()");
    }
    public void windowIconified(WindowEvent e) {
        System.out.println("windowIconified()");
    }
    public void windowOpened(WindowEvent e) {
        System.out.println("windowOpened()");
    }

    private void onExit() throws Exception {dispose(); }
}