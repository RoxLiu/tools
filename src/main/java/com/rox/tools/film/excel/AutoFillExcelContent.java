package com.rox.tools.film.excel;

import com.rox.tools.film.FilmInfo;
import com.rox.tools.film.aiqiyi.IqiyiFilmInfoSearcher;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.io.File;

/**
 * Created by Rox on 2017/7/2.
 */
public class AutoFillExcelContent implements XssfRowHandler, HssfRowHandler {
    protected static void fillInDir(String dir) {
        File[] files = new File(dir).listFiles();

        for(File file : files) {
            if(file.getName().endsWith(".xls")) {
                fillXls(file);
            } else if(file.getName().endsWith(".xlsx")) {
                fillXlsx(file);
            }
        }
    }

    protected static void fillXls(File xls) {
        try {
            System.out.println("开始处理文件：" + xls.getName());
            ExcelHelper.writeXls(xls, new AutoFillExcelContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void fillXlsx(File xls) {
        try {
            System.out.println("开始处理文件：" + xls.getName());
            ExcelHelper.writeXlsx(xls, new AutoFillExcelContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleRow(int index, HSSFRow row) {
        if (row == null) {
            return;
        }

        String film = ExcelHelper.getStringCellValue(row.getCell(2));
        if (film == null) {
            System.err.printf("名称为空\r\n");
            return;
        }

        Cell cell = row.getCell(7);
        if(cell == null || ExcelHelper.getStringCellValue(cell) == null) {
            film = film.trim();
            System.out.print("查询[" + film + "]........\t\t\t\t");
            FilmInfo info = IqiyiFilmInfoSearcher.search(film);

            if(info != null) {
                //年代  地区   导演  主演  简介
                row.createCell(7).setCellValue(info.age);
                row.createCell(8).setCellValue(info.region);
                row.createCell(9).setCellValue(info.director);
                row.createCell(10).setCellValue(info.character);
                row.createCell(11).setCellValue(info.brief);
                System.out.println("OK!");
            } else {
                System.out.println("未能查询到相关信息");
            }
        }
    }

    @Override
    public void handleRow(int index, XSSFRow row) {
        if (row == null) {
            return;
        }

        String film = ExcelHelper.getStringCellValue(row.getCell(2));
        if (film == null) {
            System.err.printf("名称为空\r\n");
            return;
        }

        int col = 7;
        Cell cell = row.getCell(col);
        if(cell == null || ExcelHelper.getStringCellValue(cell) == null) {
            film = film.trim();
            System.out.print("查询[" + film + "]........\t\t\t\t");
            FilmInfo info = IqiyiFilmInfoSearcher.search(film);

            if(info != null) {
                //年代  地区   导演  主演  简介
                row.createCell(col++).setCellValue(info.age);
                row.createCell(col++).setCellValue(info.region);
                row.createCell(col++).setCellValue(info.director);
                row.createCell(col++).setCellValue(info.character);
                row.createCell(col++).setCellValue(info.brief);
                System.out.println("OK!");
            } else {
                System.out.println("未能查询到相关信息");
            }
        }
    }

    public static void main(String[] args) {
/*
        String file = "电视剧已选40部1074.54小时-爱奇艺全量片单.xlsx";
        fill(new File(file));
*/
        fillInDir("./files");
    }
}

