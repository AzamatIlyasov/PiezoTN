package tn.piezo.model;

import javax.sql.RowSet;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by djaza on 08.02.2017.
 */
public class DBParser {

    //переменные для сохр исх данных
    static String[] NamePartTNras = null;
    static String[] NamePartTNpred = null;
    static double[] D = null;
    static double[] L = null;
    static double[] G = null;
    static double[] Kekv = null;
    static double[] Geo = null;
    static double[] ZdanieEtaj = null;
    static double Hrasp_ist;

    private static ArrayList<ArrayList> hydraData = new ArrayList<ArrayList>();
    private static ArrayList<ArrayList> piezoData = new ArrayList<ArrayList>();

    //считывание с базы данных DBPiezo (MSSql server - AZAPCSQLEXPRESS)

    public static ArrayList parseHydraT(String fileName)
    {
        //данные для подключения к БД
        String serverName = "AZAMATPC\\AZAPCSQLEXPRESS";
        String dbName = "DBPiezo";
        String userName = "sa";
        String userPassword = "Server510";
        String connectionUrl = "jdbc:sqlserver://%1$s;databaseName=%2$s;user=%3$s;password=%4$s;";
        String connectionString = String.format(connectionUrl, serverName, dbName, userName, userPassword);
        //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        //подключаемся к БД
        try {
            Connection con = DriverManager.getConnection(connectionString);
            Statement stmt = con.createStatement();
            //выборка необходимых данных
            ResultSet executeQuery = stmt.executeQuery("SELECT num_rasch_Uch, num_pred_Uch, Diametr_Uch, " +
                    "Length_Uch, G_Uch, Kekv_Uch, Geo_Uch, ZdanEtaj_Uch FROM [dbo].[inputSampleTable]");
            //кол-во строк-участков
            int n = executeQuery.getRow();
            //инициализация массивов для исх данных
            NamePartTNras = new String[n];
            NamePartTNpred = new String[n];
            D = new double[n];
            L = new double[n];
            G = new double[n];
            Kekv = new double[n];
            Geo = new double[n];
            ZdanieEtaj = new double[n];
            int i = 0;
            //проходимся по всем строкам запроса
            while (executeQuery.next()) {
                NamePartTNras[i] = executeQuery.getString("num_rasch_Uch");
                NamePartTNpred[i] = executeQuery.getString("num_pred_Uch");
                D[i] = executeQuery.getDouble("Diametr_Uch");
                L[i] = executeQuery.getDouble("Length_Uch");
                G[i] = executeQuery.getDouble("G_Uch");
                Kekv[i] = executeQuery.getDouble("Kekv_Uch");
                Geo[i] = executeQuery.getDouble("Geo_Uch");
                ZdanieEtaj[i] = executeQuery.getDouble("ZdanEtaj_Uch");
                //следующая строка - участок
                i++;
            }

        }
        catch (SQLException sqlE) {
            //логируем исключения
            Logger.getLogger(DBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }

        Hrasp_ist = 90.0; // пока пост величина. в дальнейшем считывать интерфейса

        // ГР
        HydraSolverC hydraPartTN = new HydraSolverC(NamePartTNras, NamePartTNpred, D, L, G, Kekv, Geo, ZdanieEtaj, Hrasp_ist);
        hydraData = hydraPartTN.HydraPartTN(hydraPartTN);

        //выход их функции
        return hydraData;
    }
/*
    //запись в файл Excel
    public static void writeExcelHydra(ArrayList HydraData , String fileName) {
        //создаем таблицу
        InputStream inputStream = null;
        HSSFWorkbook hssfWorkbook = null;
        hssfWorkbook = new HSSFWorkbook();
        HSSFSheet sheet = hssfWorkbook.createSheet("Исходные данные");
        // счетчик для строк
        int rowNum = 0;
        // создаем подписи к столбам
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue("№рас");
        row.createCell(1).setCellValue("№пред");
        row.createCell(2).setCellValue("Ф, мм");
        row.createCell(3).setCellValue("L, м");
        row.createCell(4).setCellValue("G, т/ч");
        row.createCell(5).setCellValue("К экв., мм");
        row.createCell(6).setCellValue("Гео, м");
        row.createCell(7).setCellValue("Здания, этажность");
        // заполняем лист данными
        HydraDataClassStruct objHydraDCS;
        for (int i = 0; i < HydraData.size(); i++)
        {
            rowNum++;
            Row rows = sheet.createRow(rowNum);
            objHydraDCS = (HydraDataClassStruct)HydraData.get(i);
            rows.createCell(0).setCellValue(objHydraDCS.NamePartTN);
            rows.createCell(1).setCellValue(objHydraDCS.NamePartTNpred);
            rows.createCell(2).setCellValue(objHydraDCS.D);
            rows.createCell(3).setCellValue(objHydraDCS.L);
            rows.createCell(4).setCellValue(objHydraDCS.G);
            rows.createCell(5).setCellValue(objHydraDCS.Kekv);
            rows.createCell(6).setCellValue(objHydraDCS.Geo);
            rows.createCell(7).setCellValue(objHydraDCS.ZdanieEtaj);
        }
        //записываем созданные в памети Excel в файл
        String fullFileName = "";
        fullFileName = "resources/ExcelDataBase/test files/" + fileName + ".xls";
        try (FileOutputStream out = new FileOutputStream(new File(fullFileName))) {
            hssfWorkbook.write(out);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
*/

    //расчет для ПГ
    public static ArrayList parsePiezoPlot(ArrayList HydraData)
    {
        //считываем данные на входе с использованием временных переменных
        String[] tempNamePartTN = new String[HydraData.size()];
        double[] tempL = new double[HydraData.size()];
        double[] tempGeo = new double[HydraData.size()];
        double[] tempZdanieEtaj = new double[HydraData.size()];
        double tempHrasp_ist = 0;
        double[] tempH1x = new double[HydraData.size()];
        HydraDataClassStruct objHydraDCS;
        for (int i = 0; i < HydraData.size(); i++)
        {
            objHydraDCS = (HydraDataClassStruct)HydraData.get(i);
            tempNamePartTN[i] = objHydraDCS.NamePartTN;
            tempL[i] = objHydraDCS.L;
            tempGeo[i] = objHydraDCS.Geo;
            tempZdanieEtaj[i] = objHydraDCS.ZdanieEtaj;
            tempHrasp_ist = objHydraDCS.Hrasp_ist;
            tempH1x[i] = objHydraDCS.H1x/1000; // из ММ -> в М
        }
        //создаем объект для ПГ
        PiezoPlotC piezoPlotPartTN = new PiezoPlotC(50, 20, 10, tempNamePartTN, tempL,  tempGeo,
                tempZdanieEtaj, tempHrasp_ist, tempH1x);
        //делаем расчеты для ПГ
        piezoData = piezoPlotPartTN.PiezoSolver(piezoPlotPartTN);

        return piezoData;
    }

}