package com.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.impl.CTRowImpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AligningCells {
    public static void main(String[] args) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet();
            XSSFRow row = sheet.createRow(2);
            row.setHeightInPoints(30);
            for (int i = 0; i < 8; i++) {
                // column width is set in units of 1/256th of a character width
                sheet.setColumnWidth(i, 256 * 15);
            }

            createCell(wb, row, 0, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
            createCell(wb, row, 1, HorizontalAlignment.CENTER_SELECTION, VerticalAlignment.BOTTOM);
            createCell(wb, row, 2, HorizontalAlignment.FILL, VerticalAlignment.CENTER);
            createCell(wb, row, 3, HorizontalAlignment.GENERAL, VerticalAlignment.CENTER);
            createCell(wb, row, 4, HorizontalAlignment.JUSTIFY, VerticalAlignment.JUSTIFY);
            createCell(wb, row, 5, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
            createCell(wb, row, 6, HorizontalAlignment.RIGHT, VerticalAlignment.TOP);

            // center text over b4, c4, d4
            row = sheet.createRow(3);
            centerAcrossSelection(wb, row, 1, 3, VerticalAlignment.CENTER);

            saveFile("xssf-align.xlsx", wb);
        }
    }

    /**
     * Center a text over multiple columns using ALIGN_CENTER_SELECTION
     *
     * @param wb
     * @param row the row to create the cell in
     * @param start_column the column number to create the cell in and where the selection starts
     * @param end_column    the column number where the selection ends
     * @param verticalAlignment
     */
    private static void centerAcrossSelection(XSSFWorkbook wb, XSSFRow row, int start_column, int end_column, VerticalAlignment verticalAlignment) {
        CreationHelper ch = wb.getCreationHelper();

        // create cell style with ALIGN_CENTER_SELECTION
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        cellStyle.setVerticalAlignment(verticalAlignment);

        // create cells over the selected area
        for (int i=start_column; i<=end_column; i++) {
            var cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }

        // set value to the first cell
        var cell = row.getCell(start_column);
        cell.setCellValue(ch.createRichTextString("Align it"));

        // Make the selection
        CTRowImpl ctRow = (CTRowImpl) row.getCTRow();

        // Add obj with format start_coll:end_coll. For example 1: 3 will span from cell 1 to cell 3, where the column index start with 0
        // You can add multiple spans for one row
        Object span = start_column + ":" + end_column;
        List<Object> spanList = new ArrayList<>();
        spanList.add(span);
    }


    /**
     * Creates a cell and aligns it a certain way.
     *
     * @param workbook     the workbook
     * @param row    the row to create the cell in
     * @param column the column number to create the cell in
     * @param halign the horizontal alignment for the cell.
     */
    public static void createCell(XSSFWorkbook workbook, XSSFRow row,
                                  int column, HorizontalAlignment halign, VerticalAlignment valign) {
        CreationHelper ch = workbook.getCreationHelper();
        Cell cell = row.createCell(column);
        cell.setCellValue(ch.createRichTextString("Align It"));
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cell.setCellStyle(cellStyle);
    }

    public static void saveFile(String fileName, XSSFWorkbook wb) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(fileName)){
            wb.write(fileOut);
        }
    }
}
