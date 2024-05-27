package com.excel;

import com.model.ImageProcessor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ExcelServiceTest {
    ExcelService service = null;
    ExcelWorkbook wb = null;
    ImageProcessor im = null;
    @BeforeEach
    void init() {
        wb = new ExcelWorkbook(new XSSFWorkbook());
        im = new ImageProcessor();
        service = new ExcelService(wb, im);
    }

    @Test
    void shouldReturnNonNullWorkbook() {
        assertNotNull(service.getExcelWorkbook().workbook());
        assertNotNull(service.getImageProcessor());
    }

    @ParameterizedTest
    @ValueSource(strings = {"sheet1", "a"})
    void shouldCreateASheet(String sheetName) {
        XSSFSheet sheet = service.getExcelWorkbook().workbook().createSheet(sheetName);
        assertEquals(sheetName, sheet.getSheetName());
    }

    @Test
    @DisplayName("Test addImageFile to excel in sheet")
    void addImageToExcel(@TempDir Path tempDir) {
        Path imageDirectory = tempDir.resolve("images");

        service.addImageFromPathIntoExcel(imageDirectory);
    }

}