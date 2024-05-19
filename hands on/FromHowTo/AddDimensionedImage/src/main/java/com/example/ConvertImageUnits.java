package com.example;

public class ConvertImageUnits {
    // Each cell contains a fixed number of coordinate point; this number does not vary with row height or column width or with font
    // These two constants are defined below.
    public static final int TOTAL_COLUMN_COORDINATE_POSITIONS = 1023;
    public static final int TOTAL_ROW_COORDINATE_POSITIONS = 255;

    // The resolution of an image can be expressed as a specific number
    // of pixels per inch. Displays and printers differ but 96 pixels per
    // inch is an acceptable standard to begin with.
    public static final int PIXELS_PER_INCH = 96;

    // Constants that defines how many pixels and points there are in a millimetre. These values are required for the conversion algorithm.
    public static final double PIXELS_PER_MILLIMETRES = 3.78;
    public static final double POINTS_PER_MILLIMETRE = 2.83;

    // The column width returned by HSSF and the width of a picture when
    // positioned to exactly cover one cell are different by almost exactly
    // 2mm - give or take rounding errors. This constant allows that
    // additional amount to be accounted for when calculating how many
    // cells the image ought to overlie.
    public static final double CELL_BORDER_WIDTH_MILLIMETRES = 2.0D;
    public static final short EXCEL_COLUMN_WIDTH_FACTOR = 256;
    public static final int UNIT_OFFSET_LENGTH = 7;
    private static final int[] UNIT_OFFSET_MAP = {0, 36, 73, 109, 146, 182, 219};

    /**
     * pixel units to excel width units(units of 1/256th of a character width)
     */
    public static short pixel2WidthUnits(int pxs) {
        int widthUnits = (EXCEL_COLUMN_WIDTH_FACTOR *
                (pxs / UNIT_OFFSET_LENGTH));
        widthUnits += UNIT_OFFSET_MAP[(pxs % UNIT_OFFSET_LENGTH)];
        return (short) widthUnits;
    }

    /**
     * excel width units(units of 1/256th of a character width) to pixel
     * units.
     */
    public static int widthUnits2Pixel(short widthUnits) {
        int pixels = (widthUnits / EXCEL_COLUMN_WIDTH_FACTOR)
                * UNIT_OFFSET_LENGTH;
        int offsetWidthUnits = widthUnits % EXCEL_COLUMN_WIDTH_FACTOR;
        pixels += Math.round(offsetWidthUnits /
                ((float) EXCEL_COLUMN_WIDTH_FACTOR / UNIT_OFFSET_LENGTH));
        return pixels;
    }

    /**
     * Convert Excels width units into millimetres.
     *
     * @param widthUnits The width of the column or the height of the
     *                   row in Excels units.
     * @return A primitive double that contains the columns width or rows
     * height in millimetres.
     */
    public static double widthUnits2Millimetres(short widthUnits) {
        return (ConvertImageUnits.widthUnits2Pixel(widthUnits) /
                ConvertImageUnits.PIXELS_PER_MILLIMETRES);
    }

    /**
     * Convert into millimetres Excels width units..
     *
     * @param millimetres A primitive double that contains the columns
     *                    width or rows height in millimetres.
     * @return A primitive int that contains the columns width or rows
     * height in Excels units.
     */
    public static int millimetres2WidthUnits(double millimetres) {
        return (ConvertImageUnits.pixel2WidthUnits((int) (millimetres *
                ConvertImageUnits.PIXELS_PER_MILLIMETRES)));
    }
}
