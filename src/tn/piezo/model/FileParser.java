package tn.piezo.model;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by djaza on 15.03.2017.
 * парсер для работы с текстовым файлом - для считывание в комбобоксы
 */
public class FileParser {
    // списки для combobox -источник, тс, ответвление
    public static ArrayList<String> listSourse = new ArrayList<>();
    public static ArrayList<String> listTN = new ArrayList<>();
    public static ArrayList<String> listBranchTN = new ArrayList<>();

    /**
     * считвание списков с файлов для combobox
     */
    public void readTxtToCombobox() {
        // очистка от старых данных
        listSourse.clear();
        listTN.clear();
        listBranchTN.clear();
        //считывание списков источников, тс и ответвлений для всех combobox
        FileParser fileParser = new FileParser();
        //с текстового файла
        listSourse = fileParser.fileParser("resources/ExcelDataBase/1_Source/CollectionSource.txt");
        listTN = fileParser.fileParser("resources/ExcelDataBase/2_TN/CollectionTN.txt");
        listBranchTN = fileParser.fileParser("resources/ExcelDataBase/3_PartTN/CollectionBranch.txt");

    }

    /**
     * запись созданных источников и др в текстовый файл
     */
    public void writeTxtFromCombobox() {
        File fileSrc = new File("resources/ExcelDataBase/1_Source/CollectionSource.txt");
        File fileTN = new File("resources/ExcelDataBase/2_TN/CollectionTN.txt");
        File fileBranchTN = new File("resources/ExcelDataBase/3_PartTN/CollectionBranch.txt");
        try {
            fileSrc.createNewFile();
            fileTN.createNewFile();
            fileBranchTN.createNewFile();
            PrintWriter printWriterSrc = new PrintWriter(fileSrc.getAbsoluteFile());
            PrintWriter printWriterTN = new PrintWriter(fileTN.getAbsoluteFile());
            PrintWriter printWriterBranchTN = new PrintWriter(fileBranchTN.getAbsoluteFile());
            for (String str: listSourse) {
                printWriterSrc.println(str);
            }
            printWriterSrc.close();
            for (String str: listTN) {
                printWriterTN.println(str);
            }
            printWriterTN.close();
            for (String str: listBranchTN) {
                printWriterBranchTN.println(str);
            }
            printWriterBranchTN.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // считывание с текстового файла
    public ArrayList fileParser(String filePath) {
        ArrayList<String> strFile = new ArrayList<>();
        try {
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                strFile.add(line);
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strFile;
    }

}
