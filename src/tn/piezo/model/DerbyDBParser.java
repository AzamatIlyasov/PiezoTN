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
 * парсер для работы с БД mssql PiezoDerbyDB
 */
public class DerbyDBParser {

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
    //доп сведения
    public static String BoilerName = null;
    public static String MainName = null;
    public static String BranchName = null;

    public static Connection con = null;
    private static ArrayList<ArrayList> hydraData = new ArrayList<ArrayList>();
    private static ArrayList<ArrayList> piezoData = new ArrayList<ArrayList>();
    // списки для combobox -источник, магистраль, ответвления, участки
    public static ArrayList<String> listBoiler = new ArrayList<>();
    public static ArrayList<String> listMain = new ArrayList<>();
    public static ArrayList<String> listBranch = new ArrayList<>();
    public static ArrayList<String> listPart = new ArrayList<>();
    public static ArrayList<String> listAllPartInBoiler = new ArrayList<>();

    //подключаем базу
    public static void connectDataBase() {
        //данные для подключения к БД
        /*
        // через APACHE DERBY
        //   ## DEFINE VARIABLES SECTION ##
        // define the driver to use
        String driver = "org.apache.derby.jdbc.EmbeddedDriver";
        // the database name
        String dbName="PiezoDerbyDB";
        // define the Derby connection URL to use
        String connectionURL = "jdbc:derby:" + dbName + ";create=true";
        String connectionString = connectionURL;
        Statement s;
        PreparedStatement psInsert;
        */
        // через ms sql server PiezoDerbyDB
        String serverName = "AZAMATPC\\AZAPCSQLEXPRESS";
        String dbName = "PiezoDerbyDB";
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
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, sqlE);
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
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }
    }

    //считывание гидравлической таблицы с базы данных
    public static HydraSolverC parseHydraTableData(String conditionBoiler, String conditionTNMain, String conditionTNBranch) {
        try {
            Statement stmt = con.createStatement();
            CallableStatement callProcStmt = con.prepareCall("{ call SelectTableDataFromDB(?,?,?) }");
            callProcStmt.setString(1,conditionBoiler);
            callProcStmt.setString(2,conditionTNMain);
            callProcStmt.setString(3,conditionTNBranch);

            //выборка всех данных для подсчета кол-ва участков
            callProcStmt.execute();
            ResultSet rsQueryForN = callProcStmt.getResultSet();
            //кол-во строк-участков
            int n = 0;
            while (rsQueryForN.next()) n++;
            //выборка данных для выполнения программы
            callProcStmt.execute();
            ResultSet rsMyQuery = callProcStmt.getResultSet();

            //инициализация массивов для исх данных
            NamePartTNras = new String[n];
            NamePartTNpred = new String[n];
            D = new double[n];
            L = new double[n];
            G = new double[n];
            Kekv = new double[n];
            Geo = new double[n];
            ZdanieEtaj = new double[n];
            //доп сведения
            BoilerName = conditionBoiler;
            MainName = conditionTNMain;
            BranchName = conditionTNBranch;
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
            listBoiler.clear();
            ResultSet rsQuerySourceBox = stmt.executeQuery("select [NAMEboiler] from [PiezoDerbyDB].[dbo].[BOILER]");
            while (rsQuerySourceBox.next()) {
                listBoiler.add(rsQuerySourceBox.getString("NAMEboiler"));
            }
            stmt.close();

        }
        catch (SQLException sqlE) {
            //логируем исключения
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }

        // ГР
        HydraSolverC hydraPartTN = new HydraSolverC(NamePartTNras, NamePartTNpred, D, L, G, Kekv, Geo, ZdanieEtaj,
                BoilerName, MainName, BranchName);
        return hydraPartTN;
    }

    //считывание гидравлической таблицы определенного участка с базы данных
    public static HydraSolverC parseHydraPartData(String conditionBoiler, String conditionTNMain, String conditionTNBranch, String conditionTNPart) {
        try {
            Statement stmt = con.createStatement();
            CallableStatement callProcStmt = con.prepareCall("{ call SelectPartDataFromDB(?,?,?,?) }");
            callProcStmt.setString(1,conditionBoiler);
            callProcStmt.setString(2,conditionTNMain);
            callProcStmt.setString(3,conditionTNBranch);
            callProcStmt.setString(4,conditionTNPart);
            //выборка всех данных для подсчета кол-ва участков
            callProcStmt.execute();
            ResultSet rsQueryForN = callProcStmt.getResultSet();
            //кол-во строк-участков
            int n = 0;
            while (rsQueryForN.next()) n++;
            //выборка данных для выполнения программы
            callProcStmt.execute();
            ResultSet rsMyQuery = callProcStmt.getResultSet();
            //инициализация массивов для исх данных
            NamePartTNras = new String[n];
            NamePartTNpred = new String[n];
            D = new double[n];
            L = new double[n];
            G = new double[n];
            Kekv = new double[n];
            Geo = new double[n];
            ZdanieEtaj = new double[n];
            //доп сведения
            BoilerName = conditionBoiler;
            MainName = conditionTNMain;
            BranchName = conditionTNBranch;
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
            stmt.close();

        }
        catch (SQLException sqlE) {
            //логируем исключения
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }

        // ГР
        HydraSolverC hydraPartTN = new HydraSolverC(NamePartTNras, NamePartTNpred, D, L, G, Kekv, Geo, ZdanieEtaj,
                BoilerName, MainName, BranchName);
        return hydraPartTN;
    }

    //считывание с базы данных + гидравлический расчет
    public static ArrayList parseHydraT(String conditionBoiler, String conditionTNMain, String conditionTNBranch, double Hist) {
        parseHydraTableData(conditionBoiler, conditionTNMain, conditionTNBranch);

        // ГР
        Hrasp_ist = Hist;// напор у источника - считывается из главного окна
        HydraSolverC hydraPartTN = new HydraSolverC(NamePartTNras, NamePartTNpred, D, L, G, Kekv, Geo, ZdanieEtaj, Hrasp_ist,
                BoilerName, MainName, BranchName);

        hydraData = hydraPartTN.HydraPartTN(hydraPartTN);

        //выход их функции
        return hydraData;
    }

    //-запись гидравлической таблицы в БД
    public static void writeTableHydra(HydraC HydraEditData) {
        try {
            Statement stmt = con.createStatement();
            CallableStatement callProcStmt = con.prepareCall("{ call  }");
        } catch (SQLException sqlE) {
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }
    }

    //запись данных о котельной в БД
    public static void writeBoilerData(String OldNameBoilerString, String NewNameBoiler) {
        try {
            CallableStatement callProcStmt = con.prepareCall("{ call EditBoiler(?,?) }");
            callProcStmt.setString(1, OldNameBoilerString);
            callProcStmt.setString(2, NewNameBoiler);
            callProcStmt.execute();
        } catch (SQLException sqlE) {
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }
    }

    //запись данных магистрали в БД
    public static void writeMainData(String OldNameMain, String NewNameMain, String OldSource, String NewSource) {
        try {
            CallableStatement callProcStmt = con.prepareCall("{ call EditMainName(?,?,?) }");
            callProcStmt.setString(1, OldNameMain);
            callProcStmt.setString(2, NewNameMain);
            callProcStmt.setString(3, OldSource);
            callProcStmt.execute();
            callProcStmt = con.prepareCall("{ call EditMainSource(?,?,?) }");
            callProcStmt.setString(1, OldSource);
            callProcStmt.setString(2, NewSource);
            callProcStmt.setString(3, NewNameMain);
            callProcStmt.execute();

        } catch (SQLException sqlE) {
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }
    }

    //запись данных ответвления в БД
    public static void writeBranchData(String OldNameBranch, String NewNameBranch, String Boiler, String OldMain, String NewMain) {
        try {
            CallableStatement callProcStmt = con.prepareCall("{ call EditBranchName(?,?,?,?) }");
            callProcStmt.setString(1, OldNameBranch);
            callProcStmt.setString(2, NewNameBranch);
            callProcStmt.setString(3, Boiler);
            callProcStmt.setString(4, OldMain);
            callProcStmt.execute();
            callProcStmt = con.prepareCall("{ call EditBranchSourceMain(?,?,?,?) }");
            callProcStmt.setString(1, OldMain);
            callProcStmt.setString(2, NewMain);
            callProcStmt.setString(3, Boiler);
            callProcStmt.setString(4, NewNameBranch);
            callProcStmt.execute();

        } catch (SQLException sqlE) {
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }
    }

    //запись-обновление данных ответвления в БД
    public static void writePartData(String Boiler, String Main, String OldBranch, String NewBranch,
                                     String OldNamePart, String NewNamePart,
                                     String NamePartTNpred,
                                     double DField,
                                     double LField,
                                     double GField,
                                     double KekvField,
                                     double GeoField,
                                     double ZdanieEtajField) {
        try {
            CallableStatement callProcStmt = con.prepareCall("{ call EditPart(?,?,?,?,?,?,?,?,?,?,?,?) }");
            callProcStmt.setString(1,Boiler);
            callProcStmt.setString(2,Main);
            callProcStmt.setString(3,OldBranch);
            callProcStmt.setString(4,NewBranch);
            callProcStmt.setString(5,OldNamePart);
            callProcStmt.setString(6,NewNamePart);
            callProcStmt.setDouble(7,DField);
            callProcStmt.setDouble(8,LField);
            callProcStmt.setDouble(9,GField);
            callProcStmt.setDouble(10,KekvField);
            callProcStmt.setDouble(11,GeoField);
            callProcStmt.setDouble(12,ZdanieEtajField);
            //callProcStmt.setString(13,NamePartTNpred);
            //callProcStmt.setDouble(14,Hrasp_istField);
            callProcStmt.execute();

            int IDPartTNpred = 0;
            Statement stmt = con.createStatement();
            String myQueryTNPredPart = "select [IDtnpart],[NAMEtnpart] " +
                    "from [dbo].[TNPART] " +
                    "where [IDtnbranch] IN (select [IDtnbranch] " +
                    "from [dbo].[TNBRANCH] " +
                    "where [IDtnmain] IN (select [IDtnmain] " +
                    "from [dbo].[TNMAIN] " +
                    "where [IDboiler] = (select [IDboiler] " +
                    "from [dbo].[BOILER] " +
                    "where [NAMEboiler] = '" + Boiler + "' ) ) ) " +
                    "and [NAMEtnpart] = '" + NamePartTNpred + "'";
            ResultSet rsQuerySourceBox = stmt.executeQuery(myQueryTNPredPart);
            while (rsQuerySourceBox.next()) {
                IDPartTNpred = rsQuerySourceBox.getInt("IDtnpart");
            }

            callProcStmt = con.prepareCall("{ call EditHydraTable(?,?,?,?,?) }");
            callProcStmt.setString(1,Boiler);
            callProcStmt.setString(2,Main);
            callProcStmt.setString(3,NewBranch);
            callProcStmt.setString(4,NewNamePart);
            callProcStmt.setInt(5,IDPartTNpred);

            callProcStmt.execute();

        } catch (SQLException sqlE) {
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }
    }

    //запись-обновление данных ответвления в БД
    public static void writeAddPartData(String Boiler, String Main, String Branch, String NamePart,
                                     String NamePartTNpred,
                                     double DField,
                                     double LField,
                                     double GField,
                                     double KekvField,
                                     double GeoField,
                                     double ZdanieEtajField) {
        try {
            CallableStatement callProcStmt = con.prepareCall("{ call AddPart(?,?,?,?,?,?,?,?,?,?) }");
            callProcStmt.setString(1,Boiler);
            callProcStmt.setString(2,Main);
            callProcStmt.setString(3,Branch);
            callProcStmt.setString(4,NamePart);
            callProcStmt.setDouble(5,DField);
            callProcStmt.setDouble(6,LField);
            callProcStmt.setDouble(7,GField);
            callProcStmt.setDouble(8,KekvField);
            callProcStmt.setDouble(9,GeoField);
            callProcStmt.setDouble(10,ZdanieEtajField);
            callProcStmt.execute();

            int IDPartTNpred = 0;
            Statement stmt = con.createStatement();
            String myQueryTNPredPart = "select [IDtnpart],[NAMEtnpart] " +
                    "from [dbo].[TNPART] " +
                    "where [IDtnbranch] IN (select [IDtnbranch] " +
                    "from [dbo].[TNBRANCH] " +
                    "where [IDtnmain] IN (select [IDtnmain] " +
                    "from [dbo].[TNMAIN] " +
                    "where [IDboiler] = (select [IDboiler] " +
                    "from [dbo].[BOILER] " +
                    "where [NAMEboiler] = '" + Boiler + "' ) ) ) " +
                    "and [NAMEtnpart] = '" + NamePartTNpred + "'";
            ResultSet rsQuerySourceBox = stmt.executeQuery(myQueryTNPredPart);
            while (rsQuerySourceBox.next()) {
                IDPartTNpred = rsQuerySourceBox.getInt("IDtnpart");
            }

            callProcStmt = con.prepareCall("{ call AddDataHydraTable(?,?,?,?,?) }");
            callProcStmt.setString(1,Boiler);
            callProcStmt.setString(2,Main);
            callProcStmt.setString(3,Branch);
            callProcStmt.setString(4,NamePart);
            callProcStmt.setInt(5,IDPartTNpred);
            callProcStmt.execute();

        } catch (SQLException sqlE) {
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }
    }

    //расчет для ПГ
    public static ArrayList parsePiezoPlot(ArrayList HydraData) {
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
        PiezoPlotC piezoPlotPartTN = new PiezoPlotC(Hrasp_ist, 5, Hrasp_ist, tempNamePartTN, tempL,  tempGeo,
                tempZdanieEtaj, tempHrasp_ist, tempH1x);
        //делаем расчеты для ПГ
        piezoData = piezoPlotPartTN.PiezoSolver(piezoPlotPartTN);

        return piezoData;
    }

    /**
     * считывание списков с файлов для combobox Boiler
     */
    public static void dbReadForComboboxBoiler() {
        try {
            Statement stmt = DerbyDBParser.con.createStatement();
            // очищаем список источников и заполняем новыми данными в комбобокс
            listBoiler.clear();
            ResultSet rsQuerySourceBox = stmt.executeQuery("select [NAMEboiler] from [PiezoDerbyDB].[dbo].[BOILER]");
            while (rsQuerySourceBox.next()) {
                listBoiler.add(rsQuerySourceBox.getString("NAMEboiler"));
            }
        }
        catch (SQLException sqlE) {
            //логируем исключения
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }

    }
    /**
     * считывание списков с файлов для combobox TNMain
     */
    public static void dbReadForComboboxTNMain(String conditionBoiler) {
        try {
            Statement stmt = DerbyDBParser.con.createStatement();
            // очистка от старых данных
            listMain.clear();
            //считывание списков магистральных тс
            // из базы данных
            String myQueryTNMains = "select NAMEtnmain " +
                    "from [PiezoDerbyDB].[dbo].[TNMAIN] " +
                    "where [IDboiler] = (select [IDboiler] from [PiezoDerbyDB].[dbo].[BOILER] " +
                                        "where [NAMEboiler] = '" + conditionBoiler + "' )";
            ResultSet rsQueryCBox = stmt.executeQuery(myQueryTNMains);
            while (rsQueryCBox.next()) {
                listMain.add(rsQueryCBox.getString("NAMEtnmain"));
            }
        }
        catch (SQLException sqlE) {
            //логируем исключения
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }

    }

    /**
     * считвание списков с файлов для combobox TNBranching
     */
    public static void dbReadForComboboxTNBranch(String conditionBoiler, String conditionTNMain) {
        try {
            Statement stmt = DerbyDBParser.con.createStatement();
            // очистка от старых данных
            listBranch.clear();
            //считывание списков ответвлений для всех combobox
            // из базы данных
            String myQueryTNBranches = "select NAMEtnbranch " +
                    "from [PiezoDerbyDB].[dbo].[TNBRANCH] " +
                    "where [IDtnmain] = (select [IDtnmain] " +
                                        "from [PiezoDerbyDB].[dbo].[TNMAIN] " +
                                        "where [NAMEtnmain] = '" + conditionTNMain + "' "+
                                        "and [IDboiler] = (select [IDboiler] from [PiezoDerbyDB].[dbo].[BOILER] " +
                                                            "where [NAMEboiler] = '" + conditionBoiler + "') )";
            ResultSet rsQueryCBox = stmt.executeQuery(myQueryTNBranches);
            while (rsQueryCBox.next()) {
                listBranch.add(rsQueryCBox.getString("NAMEtnbranch"));
            }
        }
        catch (SQLException sqlE) {
            //логируем исключения
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }

    }

    /**
     * считвание списков из БД участков Part
     */
    public static void dbReadPart(String conditionBoiler, String conditionTNMain, String conditionBranch) {
        try {
            Statement stmt = DerbyDBParser.con.createStatement();
            // очистка от старых данных
            listPart.clear();
            //считывание из базы данных
            String myQueryTNPart = "select [NAMEtnpart] from [dbo].[TNPART] \n" +
                                   "where [IDtnbranch] = (select [IDtnbranch] from [dbo].[TNBRANCH] \n" +
                                                         "where [NAMEtnbranch] = '" + conditionBranch + "' \n" +
                                                         "and  [IDtnmain] = (select [IDtnmain] from [dbo].[TNMAIN] where [NAMEtnmain] = '" + conditionTNMain + "' \n" +
                                                         "and [IDboiler] = (select [IDboiler] from [dbo].[BOILER] where [NAMEboiler] = '" + conditionBoiler + "') ) )";
            ResultSet rsQueryCBox = stmt.executeQuery(myQueryTNPart);
            while (rsQueryCBox.next()) {
                listPart.add(rsQueryCBox.getString("NAMEtnpart"));
            }
            // очистка от старых данных
            listAllPartInBoiler.clear();
            //считывание из базы данных
            String myQueryTNPredPart = "select [IDtnpart],[NAMEtnpart] " +
                    "from [dbo].[TNPART] " +
                    "where [IDtnbranch] IN (select [IDtnbranch] " +
                                            "from [dbo].[TNBRANCH] " +
                                            "where [IDtnmain] IN (select [IDtnmain] " +
                                                                "from [dbo].[TNMAIN] " +
                                                                "where [IDboiler] = (select [IDboiler] " +
                                                                                    "from [dbo].[BOILER] " +
                                                                                    "where [NAMEboiler] = '" + conditionBoiler + "' ) ) ) ";

            rsQueryCBox = stmt.executeQuery(myQueryTNPredPart);
            while (rsQueryCBox.next()) {
                listAllPartInBoiler.add(rsQueryCBox.getString("NAMEtnpart"));
            }
        }
        catch (SQLException sqlE) {
            //логируем исключения
            Logger.getLogger(DerbyDBParser.class.getName()).log(Level.SEVERE, null, sqlE);
        }

    }
}
