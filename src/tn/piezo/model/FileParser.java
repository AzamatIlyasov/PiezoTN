package tn.piezo.model;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by djaza on 15.03.2017.
 */
public class FileParser {
    // списки для combobox -источник, тс, ответвление
    public static ArrayList<String> fileSourse = new ArrayList<>();
    public static ArrayList<String> fileTN = new ArrayList<>();
    public static ArrayList<String> fileBranchTN = new ArrayList<>();

    /**
     * считвание списков с файлов для combobox
     */
    public void readTxtToCombobox() {
        //считывание списков источников, тс и ответвлений для всех combobox
        FileParser fileParser = new FileParser();
        fileSourse = fileParser.fileParser("resources/ExcelDataBase/1_Source/CollectionSource.txt");
        fileTN = fileParser.fileParser("resources/ExcelDataBase/2_TN/CollectionTN.txt");
        fileBranchTN = fileParser.fileParser("resources/ExcelDataBase/3_PartTN/CollectionBranch.txt");
    }

    /**
     * запись созданных источников и др
     */
    public void writeTxtFromCombobox() {

    }

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
