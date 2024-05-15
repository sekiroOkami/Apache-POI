package com.example.demo;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class CellStyleExample {
    public static void main(String[] args) {
        Workbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("Styled Sheet");

        //  create a row and put some cells in it
        var row = sheet.createRow(0);

        // create a cell and put a value in it
        var cell = row.createCell(0);
        cell.setCellValue("Styled Cell");

        // Create a CellStyle
        CellStyle cellStyle = workbook.createCellStyle();

        // Set the font
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 16);
        font.setFontName("Arial");
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFont(font);

        // Set background color
        cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Set borders
        cellStyle.setBorderBottom(BorderStyle.THICK);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THICK);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(BorderStyle.THICK);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(BorderStyle.THICK);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

        // Set text alignment
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // Apply the style to the cell
        cell.setCellStyle(cellStyle);

        // Autosize the columns
        sheet.autoSizeColumn(0);

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream(".\\styled_excel.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
