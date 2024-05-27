package com.excel;

import com.model.ImageProcessor;

import java.nio.file.Path;
import java.util.Set;

public class ExcelService {
    private ExcelWorkbook excelWorkbook;
    private ImageProcessor imageProcessor;

    public ExcelService(ExcelWorkbook excelWorkbook, ImageProcessor imageProcessor) {
        this.excelWorkbook = excelWorkbook;
        this.imageProcessor = imageProcessor;
    }




    public void addImageFromPathIntoExcel(Path imageDirectory) {
//        imageProcessor.writeLandscapeImageToModifiedDirectoryIfImageIsPortraitRotateBeforeWrite();
    }

    public ExcelWorkbook getExcelWorkbook() {
        return excelWorkbook;
    }

    public void setExcelWorkbook(ExcelWorkbook excelWorkbook) {
        this.excelWorkbook = excelWorkbook;
    }

    public ImageProcessor getImageProcessor() {
        return imageProcessor;
    }

    public void setImageProcessor(ImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }


}
