package tn.piezo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by djaza on 08.02.2017.
 * парсер для работы с БД mssql DBPiezo
 */
public class DBParser {

    //переменные для сохр исх данных
    private static String[] NamePartTNras = null;
    private static String[] NamePartTNpred = null;
    private static double[] D = null;
    private static double[] L = null;
    private static double[] G = null;
    private static double[] Kekv = null;
    private static double[] Geo = null;
    private static double[] ZdanieEtaj = null;
    private static double Hrasp_ist;

    public static Connection con = null;
    private static ArrayList<ArrayList> hydraData = new ArrayList<ArrayList>();
    private static ArrayList<ArrayList> piezoData = new ArrayList<ArrayList>();
    // списки для combobox -источник, тс, ответвление
    public static ArrayList<String> listSourse = new ArrayList<>();
    public static ArrayList<String> listTN = new ArrayList<>();
    public static ArrayList<String> listBranchTN = new ArrayList<>();

    //подключаем базу
    public static void connectDataBase() {
        //данные для подключения к БД
        String serverName = "AZAMATPC\\AZAPCSQLEXPRESS";
        String dbName = "DBPiezo";
        String userName = "sa";
        String userPassword = "Server510";
        String connectionUrl = "jdbc:sqlserver://%1$s;databaseName=%2$s;user=%3$s;password=%4$s;";
        String connectionString = String.format(connectionUrl, serverName, dbName, userName, userPassword);
        //подключаемся к БД
        try {
            con = DriverManager.getConnection(connectionString);
            //соединяемся
            if (con != null) {
                System.out.println("Соединение успешно выполнено");
            } else {
                System.out.println("Соединение не установлено");
                System.exit(0);
            }

        }
        catch (SQLException sqlE) {
            //логируем исключения
            Logger.getLogger(DBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }
    }

    //отключаем соединение с БД
    public static void closeConnectDB() {
        try {
            con.close();
            //соединяемся
            if (con.isClosed()) {
                System.out.println("Соединение разорвано");
            }
        }
        catch (SQLException sqlE) {
            //логируем исключения
            Logger.getLogger(DBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }
    }


    //считывание с базы данных DBPiezo (MSSql server - AZAPCSQLEXPRESS)
    public static ArrayList parseHydraT(String conditionBoiler, String conditionTNMain, String conditionTNBranch)
    {
        String myQuery, myQueryPartBranch;
        if ( conditionTNBranch.equals("Без ответвления") || conditionTNBranch.equals("") ) {
            myQueryPartBranch = "is null ";
        }
        else {
            myQueryPartBranch = "= ( "+
                    "select [id_TNBranches] "+
                    "from [DBPiezo].[dbo].[TNBranches] "+
                    "where [name_TNBranches] = '" + conditionTNBranch + "') ";
        }
        try {
            Statement stmt = con.createStatement();

            //пишем запрос и сохраняем его в строковой переменной
            myQuery =
                    "select name_TNParts as num_rasch_Uch, (	"+
                            "select name_TNParts "+
                            "from DBPiezo.dbo.TNParts "+
                            "where DBPiezo.dbo.mainTable.id_TNParts_previous=DBPiezo.dbo.TNParts.id_TNParts	"+
                            ") as num_pred_Uch, "+
                            "Diametr_Uch, Length_Uch, G_Uch, Kekv_Uch, Geo_Uch, ZdanEtaj_Uch "+
                    "from DBPiezo.dbo.TNParts, DBPiezo.dbo.mainTable "+
                    "where DBPiezo.dbo.mainTable.id_TNParts_current = DBPiezo.dbo.TNParts.id_TNParts "+
                    "and DBPiezo.dbo.mainTable.id_Boilers = ( "+
                            "select [DBPiezo].[dbo].[Boilers].[id_Boilers] "+
                            "from [DBPiezo].[dbo].[Boilers] "+
                            "where [DBPiezo].[dbo].[Boilers].[name_Boilers] = '" + conditionBoiler + "' "+
                            ")"+
                    "and DBPiezo.dbo.mainTable.id_TNMains = ( "+
                            "select [DBPiezo].[dbo].[TNMains].[id_TNMains] "+
                            "from [DBPiezo].[dbo].[TNMains] "+
                            "where [DBPiezo].[dbo].[TNMains].[name_TNMain] = '" + conditionTNMain + "' "+
                    "and [DBPiezo].[dbo].[TNMains].[id_Boilers]=( "+
                            "select [DBPiezo].[dbo].[Boilers].[id_Boilers] "+
                            "from [DBPiezo].[dbo].[Boilers] "+
                            "where [DBPiezo].[dbo].[Boilers].[name_Boilers] = '" + conditionBoiler + "' ) "+
                            ") "+
                    "and (DBPiezo.dbo.mainTable.id_TNBranches " + myQueryPartBranch + ") ";

            //выборка всех данных для подсчета кол-ва участков
            ResultSet rsQueryForN = stmt.executeQuery(myQuery);
            //кол-во строк-участков
            int n = 0;
            while (rsQueryForN.next()) n++;
            //выборка данных для выполнения программы
            ResultSet rsMyQuery = stmt.executeQuery(myQuery);

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
            while (rsMyQuery.next()) {
                NamePartTNras[i] = rsMyQuery.getString("num_rasch_Uch");
                NamePartTNpred[i] = rsMyQuery.getString("num_pred_Uch");
                D[i] = rsMyQuery.getDouble("Diametr_Uch");
                L[i] = rsMyQuery.getDouble("Length_Uch");
                G[i] = rsMyQuery.getDouble("G_Uch");
                Kekv[i] = rsMyQuery.getDouble("Kekv_Uch");
                Geo[i] = rsMyQuery.getDouble("Geo_Uch");
                ZdanieEtaj[i] = rsMyQuery.getDouble("ZdanEtaj_Uch");
                //следующая строка - участок
                i++;
            }
            // очищаем список источников и заполняем новыми данными в комбобокс
            listSourse.clear();
            ResultSet rsQuerySourceBox = stmt.executeQuery("select name_Boilers from DBPiezo.dbo.Boilers");
            while (rsQuerySourceBox.next()) {
                listSourse.add(rsQuerySourceBox.getString("name_Boilers"));
            }
            stmt.close();

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

    //запись в данных в БД
    public static void writeTableHydra(ArrayList HydraData) {

    }

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

    /**
     * считвание списков с файлов для combobox TNBranching
     */
    public static void dbReadForComboboxTNMain(String conditionBoiler) {
        // очистка от старых данных
        listTN.clear();
        //считывание списков магистральных тс
        // из базы данных
        try {
            Statement stmt = DBParser.con.createStatement();
            String myQueryTNMains = "select name_TNMain " +
                    "from DBPiezo.dbo.TNMains " +
                    "where [id_Boilers] = (select [DBPiezo].[dbo].[Boilers].[id_Boilers] " +
                    "from [DBPiezo].[dbo].[Boilers] " +
                    "where [DBPiezo].[dbo].[Boilers].[name_Boilers] = '" + conditionBoiler + "' )";
            ResultSet rsQueryCBox = stmt.executeQuery(myQueryTNMains);
            while (rsQueryCBox.next()) {
                listTN.add(rsQueryCBox.getString("name_TNMain"));
            }
        }
        catch (SQLException sqlE) {
            //логируем исключения
            Logger.getLogger(DBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }

    }
    /**
     * считвание списков с файлов для combobox TNBranching
     */
    public static void dbReadForComboboxTNBranch(String conditionBoiler, String conditionTNMain) {
        // очистка от старых данных
        listBranchTN.clear();
        //считывание списков ответвлений для всех combobox
        // из базы данных
        try {
            Statement stmt = DBParser.con.createStatement();
            String myQueryTNBranches = "select DBPiezo.[dbo].[TNBranches].[name_TNBranches] " +
                    "from DBPiezo.[dbo].[TNBranches] " +
                    "where DBPiezo.[dbo].[TNBranches].[id_TNMains] = (select DBPiezo.dbo.TNMains.id_TNMains " +
                    "from DBPiezo.dbo.TNMains " +
                    "where DBPiezo.dbo.TNMains.name_TNMain = '" + conditionTNMain + "' "+
                    "and DBPiezo.dbo.TNMains.[id_Boilers] = (select [DBPiezo].[dbo].[Boilers].[id_Boilers] " +
                    "from [DBPiezo].[dbo].[Boilers] " +
                    "where [DBPiezo].[dbo].[Boilers].[name_Boilers] = '" + conditionBoiler + "') )";
            ResultSet rsQueryCBox = stmt.executeQuery(myQueryTNBranches);
            while (rsQueryCBox.next()) {
                listBranchTN.add(rsQueryCBox.getString("name_TNBranches"));
            }
        }
        catch (SQLException sqlE) {
            //логируем исключения
            Logger.getLogger(DBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }

    }

}
