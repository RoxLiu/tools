package com.rox.tools.film.excel;

import com.rox.tools.film.TitleInfo;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
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
            if(sheet.getLastRowNum() > 0) {
                TitleInfo titleInfo = getTitleInfo(sheet.getRow(0));
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    handler.handleRow(titleInfo, sheet.getRow(i));
                }
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

                if(sheet.getLastRowNum() > 0) {
                    TitleInfo titleInfo = getTitleInfo(sheet.getRow(0));
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        handler.handleRow(titleInfo, sheet.getRow(i));
                    }
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

                if(sheet.getLastRowNum() > 0) {
                    TitleInfo titleInfo = getTitleInfo(sheet.getRow(0));
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        handler.handleRow(titleInfo, sheet.getRow(i));
                    }
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

    private static TitleInfo getTitleInfo(Row row) {
        TitleInfo info = new TitleInfo();

        for(int i = 0; i < row.getLastCellNum(); i++) {
            String title = getStringCellValue(row.getCell(i));
            if(title.equals("片名") || title.equals("节目名称")) {
                info.nameColumn = i;
            } else if(title.equals("节目属性") || title.equals("类别")) {
                info.categoryColumn = i;
            } else if(title.equals("导演")) {
                info.directorColumn = i;
            } else if(title.equals("演员")) {
                info.characterColumn = i;
            } else if(title.equals("简介")) {
                info.briefColumn = i;
            } else if(title.equals("年代")) {
                info.ageColumn = i;
            } else if(title.equals("国别") || title.equals("地区")) {
                info.regionColumn = i;
            }
        }

        int col = row.getLastCellNum() + 1;
        if(info.directorColumn == 0) {
            info.directorColumn = col;
            row.createCell(col++).setCellValue("导演");
        }

        if(info.characterColumn == 0) {
            info.characterColumn = col;
            row.createCell(col++).setCellValue("演员");
        }

        if(info.briefColumn == 0) {
            info.briefColumn = col;
            row.createCell(col++).setCellValue("简介");
        }

        if(info.ageColumn == 0) {
            info.ageColumn = col;
            row.createCell(col++).setCellValue("年代");
        }

        if(info.regionColumn == 0) {
            info.regionColumn = col;
            row.createCell(col++).setCellValue("地区");
        }

        return info;
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
                try {
                    FormulaEvaluator evaluator = cell.getRow().getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                    result = getCellValue(evaluator.evaluate(cell));
                } catch (Exception e) {
//                    e.printStackTrace();
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

    private static String getCellValue(CellValue cell) {
        String cellValue = null;
        switch (cell.getCellTypeEnum()) {
            case STRING:
                System.out.print("String :");
                cellValue=cell.getStringValue();
                break;
            case NUMERIC:
                System.out.print("NUMERIC:");
                cellValue=String.valueOf(cell.getNumberValue());
                break;
            default:
                break;
        }

        return cellValue;
    }

}
