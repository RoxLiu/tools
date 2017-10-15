package com.rox.tools.film.excel;

import com.rox.tools.film.FilmInfo;
import com.rox.tools.film.Searcher;
import com.rox.tools.film.aiqiyi.IqiyiFilmInfoSearcher;
import com.rox.tools.film.le.LeFilmInfoSearcher;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.io.File;

/**
 * Created by Rox on 2017/7/2.
 */
public class AutoFillExcelContent implements XssfRowHandler, HssfRowHandler {
    private Searcher searcher;

    public Searcher getSearcher() {
        return searcher;
    }

    public void setSearcher(Searcher searcher) {
        this.searcher = searcher;
    }

    protected void fillInDir(String dir) {
        File[] files = new File(dir).listFiles();

        for(File file : files) {
            if(file.getName().endsWith(".xls")) {
                fillXls(file);
            } else if(file.getName().endsWith(".xlsx")) {
                fillXlsx(file);
            }
        }
    }

    protected void fillXls(File xls) {
        try {
            System.out.println("开始处理文件：" + xls.getName());
            ExcelHelper.writeXls(xls, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void fillXlsx(File xls) {
        try {
            System.out.println("开始处理文件：" + xls.getName());
            ExcelHelper.writeXlsx(xls, this);
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

        int col = 10;
        Cell cell = row.getCell(col);
        if(cell == null || ExcelHelper.getStringCellValue(cell) == null) {
            film = film.trim();
            System.out.print("查询[" + film + "]........\t\t\t\t");
            FilmInfo info = searcher.search(film);

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

        int col = 10;
        Cell cell = row.getCell(col);
        if(cell == null || ExcelHelper.getStringCellValue(cell) == null) {
            film = film.trim();
            System.out.print("查询[" + film + "]........\t\t\t\t");
            FilmInfo info = searcher.search(film);

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
        AutoFillExcelContent auto = new AutoFillExcelContent();
        auto.setSearcher(new IqiyiFilmInfoSearcher());
        auto.fillXlsx(new File("乐视独家.xlsx"));

        auto.setSearcher(new LeFilmInfoSearcher());
        auto.fillXlsx(new File("乐视独家.xlsx"));

/*
        fillInDir("./files");
*/
    }
}

