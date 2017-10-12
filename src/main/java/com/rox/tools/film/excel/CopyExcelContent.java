package com.rox.tools.film.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rox on 2017/7/2.
 */
public class CopyExcelContent {
    public static void main(String[] args) {
        String src = "四川有线片单媒资信息(1).xlsx";
        String dst = "爱奇艺第四批供片.xlsx";

        final Map<String, String> descCache = new HashMap<>();
        final Map<String, String> areaCache = new HashMap<>();
        final Map<String, String> typeCache = new HashMap<>();
        final Map<String, String> actorCache = new HashMap<>();
        final Map<String, String> directorCache = new HashMap<>();

        try {
            ExcelHelper.readXlsx(src, new XssfRowHandler() {
                @Override
                public void handleRow(int index, XSSFRow row) {
                    if (row == null) {
                        return;
                    }

                    Cell c1 = row.getCell(1);
                    if (c1 == null) {
                        return;
                    }

                    String film = c1.getStringCellValue();

                    Cell c7 = row.getCell(7);
                    if (c7 == null) {
                        System.err.printf("%s没有简介\r\n", film);
                        return;
                    }
                    String desc = c7.getStringCellValue();
                    descCache.put(film, desc);
//                    System.out.printf("发现简介【%s】\r\n", film);

                    Cell c8 = row.getCell(8);
                    if (c8 == null) {
                        System.err.printf("%s没有地区\r\n", film);
                        return;
                    }
                    String area = c8.getStringCellValue();
                    areaCache.put(film, area);
//                    System.out.printf("发现地区【%s】\r\n", film);

                    Cell c9 = row.getCell(9);
                    if (c9 == null) {
                        System.err.printf("%s没有类型\r\n", film);
                        return;
                    }
                    String type = c9.getStringCellValue();
                    typeCache.put(film, type);
//                    System.out.printf("发现类型【%s】\r\n", film);

                    Cell c10 = row.getCell(10);
                    if (c10 == null) {
                        System.err.printf("%s没有主演\r\n", film);
                        return;
                    }
                    String actor = c10.getStringCellValue();
                    actorCache.put(film, actor);
//                    System.out.printf("发现主演【%s】\r\n", film);

                    Cell c11 = row.getCell(11);
                    if (c11 == null) {
                        System.err.printf("%s没有导演\r\n", film);
                        return;
                    }
                    String director = c11.getStringCellValue();
                    directorCache.put(film, director);
//                    System.out.printf("发现导演【%s】\r\n", film);
                }
            });

            ExcelHelper.writeXlsx(dst, new XssfRowHandler() {
                @Override
                public void handleRow(int index, XSSFRow row) {
                    Cell c1 = row.getCell(1);

                    if (c1 == null) {
                        return;
                    }
                    String film = c1.getStringCellValue();

                    String desc = descCache.get(film);
                    if (desc != null) {
                        Cell cell = row.createCell(7);
                        cell.setCellValue(desc);
                    } else {
                        System.err.printf("没有查询到【%s】的简介\r\n", film);
                    }

                    String area = areaCache.get(film);
                    if (area != null) {
                        Cell cell = row.createCell(8);
                        cell.setCellValue(area);
                    } else {
                        System.err.printf("没有查询到【%s】的地区\r\n", film);
                    }

                    String type = typeCache.get(film);
                    if (type != null) {
                        Cell cell = row.createCell(9);
                        cell.setCellValue(type);
                    } else {
                        System.err.printf("没有查询到【%s】的类型\r\n", film);
                    }

                    String actor = actorCache.get(film);
                    if (actor != null) {
                        Cell cell = row.createCell(10);
                        cell.setCellValue(actor);
                    } else {
                        System.err.printf("没有查询到【%s】的主演\r\n", film);
                    }

                    String director = directorCache.get(film);
                    if (director != null) {
                        Cell cell = row.createCell(11);
                        cell.setCellValue(director);
                    } else {
                        System.err.printf("没有查询到【%s】的导演\r\n", film);
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

