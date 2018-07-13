package com.rox.tools.film.excel;

import com.rox.tools.film.TitleInfo;
import org.apache.poi.ss.usermodel.Row;

/**
 * Created by Rox on 2017/7/2.
 */
public interface ExcelRowHandler {
    public void handleRow(TitleInfo titleInfo, Row row);
}
