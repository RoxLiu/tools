package com.rox.tools.film.excel;

import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 * Created by Rox on 2017/7/2.
 */
public interface XssfRowHandler {
    public void handleRow(int index, XSSFRow row);
}
