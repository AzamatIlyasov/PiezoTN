package tn.piezo.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

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
    /*
    public static ArrayList parseHydraT(String fileName)
    {
        // инициализируем потоки
        String result = "";
        InputStream inputStream = null;
        HSSFWorkbook workbook = null;
        try {
            inputStream = new FileInputStream(fileName);
            workbook = new HSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //разбираем первый лист входного файла на объектную модель
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> itRforN = sheet.iterator();
        itRforN.next();

        int i = 0, n = 0;
        //проходим по всему листу для получения кол-ва строк
        String cellTestN = "", rowTestN = "";

        while (itRforN.hasNext()) {
            Row rowForN = itRforN.next();
            Iterator<Cell> cellsForN = rowForN.iterator();
            do{
                Cell cellForN = cellsForN.next();
                //перебираем возможные типы ячеек
                switch (cellForN.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        cellTestN = cellForN.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        cellTestN = Double.toString(cellForN.getNumericCellValue());
                        break;
                    default:
                        if (cellsForN.hasNext())
                            cellTestN = "error"; //обозначения конца файла
                        else cellTestN = "error2"; //обозначение конца строки
                        break;
                }
                //проверка на конец строки
                if (!cellsForN.hasNext()) break;

                Cell cellForN2 = cellsForN.next();
                //перебираем возможные типы ячеек
                switch (cellForN2.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        rowTestN = cellForN2.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        rowTestN = Double.toString(cellForN2.getNumericCellValue());
                        break;
                    default:
                        if (cellsForN.hasNext())
                            rowTestN = "error";
                        else rowTestN = "error2";
                        break;
                }

                if ((rowTestN=="error") && (cellTestN=="error"))
                    break;
            } while(cellsForN.hasNext());

            //увеличить кол-во участков
            n++;
            //проверка - закончились участки?, т.е. конец файла
            if ((rowTestN=="error") && (cellTestN=="error")) {
                n--;
                break;
            }
            //продолжаем считать

        }
        //инициализация массивов для исх данных
        NamePartTNras = new String[n];
        NamePartTNpred = new String[n];
        D = new double[n];
        L = new double[n];
        G = new double[n];
        Kekv = new double[n];
        Geo = new double[n];
        ZdanieEtaj = new double[n];
        Iterator<Row> itR = sheet.iterator();
        itR.next(); // начинаем считывать со второй строки листа excel (первая заголовки)
        //проходим по всему листу
        while (itR.hasNext()) {
            Row row = itR.next();
            Iterator<Cell> cells = row.iterator();
            //сохраняем наз текущего участка
            Cell cell = cells.next();
            if (!cells.hasNext()) break;
            // т.к. наз могут служить и цифры
            //перебираем возможные типы ячеек
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    NamePartTNras[i] = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    NamePartTNras[i] = Double.toString(cell.getNumericCellValue());
                    break;
                default:
                    NamePartTNras[i] = "error";
                    break;
            }
            //сохраняем наз пред участка
            Cell cell2 = cells.next();
            // еще раз перебираем возможные типы ячеек
            switch (cell2.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    NamePartTNpred[i] = cell2.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    NamePartTNpred[i] = Double.toString(cell2.getNumericCellValue());
                    break;
                default:
                    NamePartTNpred[i] = "error";
                    break;
            }
            //сохр D,L,G,Kekv,Geo,ZdanieEtaj
            Cell cell3 = cells.next();
            D[i] = cell3.getNumericCellValue();
            Cell cell4 = cells.next();
            L[i] = cell4.getNumericCellValue();
            Cell cell5 = cells.next();
            G[i] = cell5.getNumericCellValue();
            Cell cell6 = cells.next();
            Kekv[i] = cell6.getNumericCellValue();
            Cell cell7 = cells.next();
            Geo[i] = cell7.getNumericCellValue();
            Cell cell8 = cells.next();
            ZdanieEtaj[i] = cell8.getNumericCellValue();

            i++; //следующий участок
            if (i >= n) break; //участки закончились - выход за пределы массива
        }
        Hrasp_ist = 90.0; // пока пост величина. в дальнейшем считывать интерфейса

        // ГР
        HydraSolverC hydraPartTN = new HydraSolverC(NamePartTNras, NamePartTNpred, D, L, G, Kekv, Geo, ZdanieEtaj, Hrasp_ist);
        hydraData = hydraPartTN.HydraPartTN(hydraPartTN);

        //выход их функции
        return hydraData;
    }

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
