package com.rox.tools.film.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;

/**
 * Created by Rox on 2017/7/19.
 */
@RunWith(JUnit4.class)
public class ConvertHourToMinute {
    @Test
    public void testConvert() {
        try {
            ExcelHelper.writeXlsx("演唱会共560场.xlsx", new XssfRowHandler() {
                @Override
                public void handleRow(int index, XSSFRow row) {
                    try {

                        Cell c1 = row.getCell(1);

                        if (c1 == null) {
                            return;
                        }
                        String value = ExcelHelper.getStringCellValue(c1);

                        if (value == null || value.equals("")) {
                            return;
                        }

                        String[] split = value.split(":");

                        //mm:ss
                        if (split.length == 2) {
                            Cell cell = row.createCell(2);

                            int s = Integer.parseInt(split[1]);
                            if (s >= 30) {
                                int m = Integer.parseInt(split[0]);

                                cell.setCellValue(String.valueOf(m + 1));
                            } else {
                                cell.setCellValue(split[0]);
                            }
                        } else if (split.length == 3) {
                            //hh:mm:ss
                            Cell cell = row.createCell(2);

                            int h = Integer.parseInt(split[0]);
                            int m = Integer.parseInt(split[1]);
                            int s = Integer.parseInt(split[2]);
                            if (s >= 30) {
                                cell.setCellValue(String.valueOf(h * 60 + m + 1));
                            } else {
                                cell.setCellValue(String.valueOf(h * 60 + m));
                            }
                        } else {
                            try {
                                double h = Double.parseDouble(value);
                                h = h * 24 * 60;
                                Cell cell = row.createCell(2);

//                                BigDecimal n = new BigDecimal(h).setScale(0).intValue();
                                cell.setCellValue(String.valueOf(new BigDecimal(h).setScale(0, BigDecimal.ROUND_HALF_UP).intValue()));
                            } catch (NumberFormatException e) {
//                                e.printStackTrace();
                                System.out.println("无效的输入：" + value);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Row: " + row.getRowNum() + "-> " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
