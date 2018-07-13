package com.rox.tools.film.excel;

import com.rox.tools.film.FilmInfo;
import com.rox.tools.film.Loggable;
import com.rox.tools.film.Searcher;
import com.rox.tools.film.TitleInfo;
import com.rox.tools.film.searcher.IqiyiFilmInfoSearcher;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.util.Random;

/**
 * Created by Rox on 2017/7/2.
 */
public class AutoFillExcelContent implements ExcelRowHandler {
    private Searcher searcher;
    private Loggable log;

    private Random random = new Random();

    public AutoFillExcelContent() {
    }

    public AutoFillExcelContent(Loggable log) {
        this.log = log;
    }

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

    public void fillXls(File xls) {
        try {
            System.out.println("开始处理文件：" + xls.getName());
            ExcelHelper.writeXls(xls, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillXlsx(File xls) {
        try {
            System.out.println("开始处理文件：" + xls.getName());
            ExcelHelper.writeXlsx(xls, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleRow(TitleInfo titleInfo, Row row) {
        if (row == null) {
            return;
        }

        int index = row.getRowNum();
        String film = ExcelHelper.getStringCellValue(row.getCell(titleInfo.nameColumn));
        if (film == null) {
            println("行【" + index + "】，名称为空。");
            return;
        }

        if(needSearch(titleInfo, row)) {
            film = film.trim();
            String category = null;
            if(titleInfo.categoryColumn > -1) {
                category = ExcelHelper.getStringCellValue(row.getCell(titleInfo.categoryColumn));
            }

            FilmInfo info = searcher.search(film, category);

            if(info != null && info.name != null) {
                //年代  地区   导演  主演  简介
                replaceCell(row, titleInfo.directorColumn, info.director);
                replaceCell(row, titleInfo.characterColumn, info.character);
                replaceCell(row, titleInfo.briefColumn, info.brief);
//                replaceCell(row, col++, info.episode);

                String age = info.age != null? info.age : info.release;
                replaceCell(row, titleInfo.ageColumn, age);
                replaceCell(row, titleInfo.regionColumn, info.region);
                println("行【" + index + "】，查询[" + film + "]........OK");
//                println(info);
            } else {
                println("行【" + index + "】，查询[" + film + "]........未能查询到相关信息");
            }

            try {
                Thread.sleep(random.nextInt(5000));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            println("行【" + index + "】，不需要搜索，跳过");
        }
    }

    private static boolean needSearch(TitleInfo titleInfo, Row row) {
        return (!row.getZeroHeight()) &&
                (isBlank(row.getCell(titleInfo.directorColumn))
                || isBlank(row.getCell(titleInfo.characterColumn))
                || isBlank(row.getCell(titleInfo.briefColumn))
                || isBlank(row.getCell(titleInfo.ageColumn))
                || isBlank(row.getCell(titleInfo.regionColumn))
                );
    }

    private static boolean isBlank(Cell cell) {
        return cell == null || ExcelHelper.getStringCellValue(cell) == null || ExcelHelper.getStringCellValue(cell).trim().equals("");
    }

    private static void replaceCell(Row row, int col, String value) {
        if(value != null && !value.equals("")) {
            if(isBlank(row.getCell(col))) {
                row.createCell(col).setCellValue(value);
            }
        }
    }

    private void println(String s) {
        System.out.println(s);
        if(log != null) {
            log.println(s);
        }
    }
    public static void main(String[] args) {
        AutoFillExcelContent auto = new AutoFillExcelContent();
//        auto.setSearcher(new YoukuFilmInfoSearcher());
        auto.setSearcher(new IqiyiFilmInfoSearcher());
        auto.fillXlsx(new File("./files/iQIYI 授权IPTV纪录片单to四川有线20180711.xlsx"));

/*
        auto.setSearcher(new LeFilmInfoSearcher());
        auto.fillXlsx(new File("乐视独家.xlsx"));
*/

/*
        fillInDir("./files");
*/
    }
}

