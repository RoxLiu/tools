package com.rox.tools.film.excel;

import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 * Created by Rox on 2017/7/2.
 */
public interface HssfRowHandler {
    public void handleRow(int index, HSSFRow row);
}
