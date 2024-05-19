package com.example.demo;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelDemoMergeAndDataValidation {
    public static void main(String[] args) {

        try(XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Demo sheet");

            // merge cells from b1 to f1
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 1, 5); // B1 to F1
            sheet.addMergedRegion(cellRangeAddress);

            // Create the cell in B1 and set its value
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(1);
            cell.setCellValue("Select an Option");

            // Create data validation
            String[] option = {"option1", "option2", "Option3"};
            DataValidationHelper validationHelper = sheet.getDataValidationHelper();
            DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(option);
            CellRangeAddressList addressList = new CellRangeAddressList(0,0,1,1);
            DataValidation validation = validationHelper.createValidation(constraint, addressList);

            // Add the data validation to the sheet
            validation.setShowErrorBox(true);
            sheet.addValidationData(validation);

            for (int i=1; i<=5; i++) {
                sheet.setColumnWidth(i, 40000);
            }

            // write the output to a file
            try (FileOutputStream fileOut = new FileOutputStream("merged_cells_with_DataValidation.xlsx")) {
                workbook.write(fileOut);
            }
            System.out.printf("Excel file created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
