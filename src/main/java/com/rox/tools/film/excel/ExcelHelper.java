package com.rox.tools.film.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Iterator;

/**
 * Created by Rox on 2017/7/2.
 */
public class ExcelHelper {

    public static void readXlsx(String xlsFile, ExcelRowHandler handler) {
        readXlsx(new File(xlsFile), handler);
    }

    public static void readXlsx(File xlsFile, ExcelRowHandler handler)  {
        InputStream is = null;
        try {
            is = new FileInputStream(xlsFile);
            XSSFWorkbook workbook = new XSSFWorkbook(is);

            if (workbook.getNumberOfSheets() < 1) {
                System.err.println("No Sheet found in xml file.");
            }

            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                handler.handleRow(i, sheet.getRow(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeXls(String xlsFile, ExcelRowHandler handler)  {
        writeXls(new File(xlsFile), handler);
    }

    public static void writeXls(File xlsFile, ExcelRowHandler handler) {
        InputStream is = null;
        try {
            is = new FileInputStream(xlsFile);
            HSSFWorkbook workbook = new HSSFWorkbook(is);

            if (workbook.getNumberOfSheets() < 1) {
                System.err.println("No Sheet found in xml file.");
            }

            Iterator<Sheet> it = workbook.sheetIterator();

            while (it.hasNext()) {
                HSSFSheet sheet = (HSSFSheet)it.next();

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    handler.handleRow(i, sheet.getRow(i));
                }
            }

            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(xlsFile);
            workbook.write(fileOut);
            fileOut.close();

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeXlsx(String xlsFile, ExcelRowHandler handler)  {
        writeXlsx(new File(xlsFile), handler);
    }

    public static void writeXlsx(File xlsFile, ExcelRowHandler handler) {
        InputStream is = null;
        try {
            is = new FileInputStream(xlsFile);
            XSSFWorkbook workbook = new XSSFWorkbook(is);

            if (workbook.getNumberOfSheets() < 1) {
                System.err.println("No Sheet found in xml file.");
            }

            Iterator<Sheet> it = workbook.sheetIterator();
            while (it.hasNext()) {
                XSSFSheet sheet = (XSSFSheet)it.next();

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    handler.handleRow(i, sheet.getRow(i));
                }
            }

            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(xlsFile);
            workbook.write(fileOut);
            fileOut.close();

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        String result = "";
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                try {
                    result = String.valueOf(cell.getNumericCellValue());
                } catch (Exception e) {
                    result = cell.getRichStringCellValue().getString();
                }
                break;
            case STRING:
                if (cell.getRichStringCellValue() == null) {
                    result = null;
                } else {
                    result = cell.getRichStringCellValue().getString();
                }
                break;
            case FORMULA:
                if (cell.getRichStringCellValue() == null) {
                    result = null;
                } else {
                    result = cell.getRichStringCellValue().getString();
                }
                break;
            case BLANK:
                result = null;
                break;
            case BOOLEAN:
                result = String.valueOf(cell.getBooleanCellValue());
                break;

            default:
                result = "";
        }

        return result;
    }

}
