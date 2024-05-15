package com.example;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import java.io.IOException;
import java.net.URL;

public class AddDimensionedImage {
    public static final int EXPAND_ROW = 1;
    public static final int EXPAND_COLUMN = 2;
    public static final int EXPAND_ROW_AND_COLUMN = 3;
    public static final int OVERLAY_ROW_AND_COLUMN = 7;

    private static final int EMU_PER_MM = 36000;

    /**
     *
     * @param cellNumber    A String that contains the location of the cell whose top left hand corner should
     *                      be aligned with the top left hand corner of the image;
     *                      for example "A1", "A2" etc.
     *                      This is to support the familiar Excel syntax.
     *                      images are not actually inserted into cells this provides a convenient method of
     *                      indicating where the image should be positioned on the sheet.
     *
     * @param sheet         A reference to the sheet that contains the cell referenced above.
     * @param drawing       An instance of the DrawingPatriarch class. This is now passed into the method where
     *                      it was recovered from the sheet in order to allow multiple pictures be inserted.
     *                      if the patriarch was not 'cached' in this manner each time it was created any previously
     *                      positioned images would be simply over-written.
     *
     * @param imageFile        An instance of the URL class that encapsulates the name of and path to the image that
     *                         is to be 'inserted into' the sheet
     *
     * @param reqImageWidthMM   A double that contains the required width of the image in millimetres.
     * @param reqImageHeightMM  A double that contains the required height of the image in millimetres.
     * @param resizeBehaviour   A int whose value will determine how the code should reac if the image is larger than
     *                          the cell referenced by the cellNumber parameter.
     *                          four constants are provided to determine what should happen
     *                          AddDimensionedImage.EXPAND_ROW
     *                          AddDimensionedImage.EXPAND_COLUMN
     *                          AddDimensionedImage.EXPAND_ROW_AND_COLUMN
     *                          AddDimensionedImage.OVERLAY_ROW_AND_COLUMN
     *
     * @throws java.io.FileNotFoundException
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public void addImageToSheet(String cellNumber, Sheet sheet, Drawing<?> drawing,
                                URL imageFile, double reqImageWidthMM, double reqImageHeightMM,
                                int resizeBehaviour) throws IOException, IllegalArgumentException {
        // Convert the String into column and row indices then chain the
        // call to the overridden addImageToSheet() method.
        CellReference cellRef = new CellReference(cellNumber);
        this.addImageToSheet(cellRef.getCol(), cellRef.getRow(), sheet, drawing,
                imageFile, reqImageWidthMM, reqImageHeightMM, resizeBehaviour);
    }

    public void addImageToSheet(int colNumber, int rowNumber, Sheet sheet, Drawing<?> drawing,
                                URL imageFile, double reqImageWidthMM, double reqImageHeightMM,
                                int resizeBehaviour) throws IOException, IllegalArgumentException {

        ClientAnchor anchor;
        ClientAnchorDetail rowClientAnchorDetail;
        ClientAnchorDetail colClientAnchorDetail;
        int imageType;

        // Validate the resizeBahaviour parameter.
        if ((resizeBehaviour != AddDimensionedImage.EXPAND_COLUMN) &&
                (resizeBehaviour != AddDimensionedImage.EXPAND_ROW) &&
                (resizeBehaviour != AddDimensionedImage.EXPAND_ROW_AND_COLUMN) &&
                (resizeBehaviour != AddDimensionedImage.OVERLAY_ROW_AND_COLUMN)) {
            throw new IllegalArgumentException("Invalid value passed to the " +
                    "resizeBehaviour parameter of AddDimensionedImage.addImageToSheet()");
        }

        // Call methods to calculate how the image and sheet should be
        // manipulated to accommodate the image; columns and then rows.
        colClientAnchorDetail = this.fitImageToColumns(sheet, colNumber, reqImageWidthMM, resizeBehaviour);
        rowClientAnchorDetail = this.fitImageToRows(sheet, rowNumber, reqImageHeightMM, resizeBehaviour);

//        anchor

    }

    private ClientAnchorDetail fitImageToRows(Sheet sheet, int rowNumber, double reqImageHeightMM, int resizeBehaviour) {
        return null;
    }


    /**
     * Determines whether the sheets columns should be re-sized to accommodate
     * the image, adjusts the columns width if necessary and creates then
     * returns a ClientAnchorDetail object that facilitates construction of
     * an ClientAnchor that will fix the image on the sheet and establish
     * its size.
     *
     * @param sheet           A reference to the sheet that will 'contain' the image.
     * @param colNumber       A primitive int that contains the index number of a
     *                        column on the sheet.
     * @param reqImageWidthMM A primitive double that contains the required
     *                        width of the image in millimetres
     * @param resizeBehaviour A primitive int whose value will indicate how the
     *                        width of the column should be adjusted if the
     *                        required width of the image is greater than the
     *                        width of the column.
     * @return An instance of the ClientAnchorDetail class that will contain
     * the index number of the column containing the cell whose top
     * left hand corner also defines the top left hand corner of the
     * image, the index number column containing the cell whose top
     * left hand corner also defines the bottom right hand corner of
     * the image and an inset that determines how far the right hand
     * edge of the image can protrude into the next column - expressed
     * as a specific number of coordinate positions.
     */
    private ClientAnchorDetail fitImageToColumns(Sheet sheet, int colNumber, double reqImageWidthMM, int resizeBehaviour) {
        double colWidthMM;
        double colCoordinatesPerMM;
        int pictureWidthCoordinates;
        ClientAnchorDetail colClientAnchorDetail = null;

        // Get the column's width in millimetres
        colWidthMM = ConvertImageUnits.widthUnits2Millimetres((short) sheet.getColumnWidth(colNumber));

        // Check that the column's width will accommodate the image at the
        // required dimension. If the width of the column is LESS than the
        // required width of the image, decide how the application should
        // respond - resize the column or overlay the image across one or more
        // columns.

        if (colWidthMM < reqImageWidthMM) {
            // should the column's width simply be expanded?
            if ((resizeBehaviour == AddDimensionedImage.EXPAND_COLUMN) ||
                    (resizeBehaviour == AddDimensionedImage.EXPAND_COLUMN)) {
                // set the width of the column by converting the required image
                // width from millimetres into Excel's column width units.
                sheet.setColumnWidth(colNumber,
                        ConvertImageUnits.millimetres2WidthUnits(reqImageWidthMM));
                // to make the image occupy the full width of the column, convert
                // the required width of the image into co-ordinates
                // This value will become the inset for the ClientAnchorDetail class that
                // is then instantiated.
                if (sheet instanceof HSSFSheet) {
                    colWidthMM = reqImageWidthMM;
                    colCoordinatesPerMM = colWidthMM == 0 ? 0
                            : ConvertImageUnits.TOTAL_COLUMN_COORDINATE_POSITIONS / colWidthMM;
                    pictureWidthCoordinates = (int) (reqImageWidthMM * colCoordinatesPerMM);
                } else {
                    pictureWidthCoordinates = (int) reqImageWidthMM * AddDimensionedImage.EMU_PER_MM;
                }
                colClientAnchorDetail = new ClientAnchorDetail(colNumber, colNumber, pictureWidthCoordinates);
            }
            // If the user has chosen to overlay both rows and columns or just
            // to expand ONLY the size of the rows, then calculate how to lay
            // the image out across one or more columns.
            else if ((resizeBehaviour == AddDimensionedImage.OVERLAY_ROW_AND_COLUMN) ||
                    (resizeBehaviour == AddDimensionedImage.EXPAND_ROW)) {
                colClientAnchorDetail = this.calculateColumnLocation(sheet,
                        colNumber, reqImageWidthMM);
            }
        }
        // If the column is wider than the image.
        else {
            if (sheet instanceof HSSFSheet) {
                // Mow many co-ordinate positions are there per millimetre?
                colCoordinatesPerMM = ConvertImageUnits.TOTAL_COLUMN_COORDINATE_POSITIONS /
                        colWidthMM;
                // Given the width of the image, what should be its co-ordinate?
                pictureWidthCoordinates = (int) (reqImageWidthMM * colCoordinatesPerMM);
            } else {
                pictureWidthCoordinates = (int) reqImageWidthMM *
                        AddDimensionedImage.EMU_PER_MM;
            }
            colClientAnchorDetail = new ClientAnchorDetail(colNumber,
                    colNumber, pictureWidthCoordinates);
        }


        return colClientAnchorDetail;
    }

    private ClientAnchorDetail calculateColumnLocation(Sheet sheet, int startingColumn, double reqImageWidthMM) {
        ClientAnchorDetail anchorDetail;
        double totalWidthMM = 0.0D;
        double colWidthMM = 0.0D;
        double overlapMM;
        double coordinatePositionsPerMM;
        int toColumn = startingColumn;
        int inset;

        // Calculate how many columns the image will have to
        // span in order to be presented at the required size.
        while (totalWidthMM < reqImageWidthMM) {
            colWidthMM = ConvertImageUnits.widthUnits2Millimetres(
                    (short) (sheet.getColumnWidth(toColumn)));
            // Note use of the cell border width constant. Testing with an image
            // declared to fit exactly into one column demonstrated that its
            // width was greater than the width of the column the POI returned.
            // Further, this difference was a constant value that I am assuming
            // related to the cell's borders. Either way, that difference needs
            // to be allowed for in this calculation.
            totalWidthMM += (colWidthMM + ConvertImageUnits.CELL_BORDER_WIDTH_MILLIMETRES);
            toColumn++;
        }
        // De-rement by one the last column value.
        toColumn--;
        // Highly unlikely that this will be true but, if the width of a series
        // of columns is exactly equal to the required width of the image, then
        // simply build a ClientAnchorDetail object with an inset equal to the
        // total number of co-ordinate positions available in a column, a
        // from column co-ordinate (top left hand corner) equal to the value
        // of the startingColumn parameter and a to column co-ordinate equal
        // to the toColumn variable.
        //
        // Convert both values to ints to perform the test.
        if ((int) totalWidthMM == (int) reqImageWidthMM) {
            // A problem could occur if the image is sized to fit into one or
            // more columns. If that occurs, the value in the toColumn variable
            // will be in error. To overcome this, there are two options, to
            // ibcrement the toColumn variable's value by one or to pass the
            // total number of co-ordinate positions to the third paramater
            // of the ClientAnchorDetail constructor. For no sepcific reason,
            // the latter option is used below.
            if (sheet instanceof HSSFSheet) {
                anchorDetail = new ClientAnchorDetail(startingColumn,
                        toColumn, ConvertImageUnits.TOTAL_COLUMN_COORDINATE_POSITIONS);
            } else {
                anchorDetail = new ClientAnchorDetail(startingColumn,
                        toColumn, (int) reqImageWidthMM * AddDimensionedImage.EMU_PER_MM);
            }
        }
        // In this case, the image will overlap part of another column and it is
        // necessary to calculate just how much - this will become the inset
        // for the ClientAnchorDetail object.
        else {
            // Firstly, claculate how much of the image should overlap into
            // the next column.
            overlapMM = reqImageWidthMM - (totalWidthMM - colWidthMM);

            // When the required size is very close indded to the column size,
            // the calcaulation above can produce a negative value. To prevent
            // problems occuring in later caculations, this is simply removed
            // be setting the overlapMM value to zero.
            if (overlapMM < 0) {
                overlapMM = 0.0D;
            }

            if (sheet instanceof HSSFSheet) {
                // Next, from the columns width, calculate how many co-ordinate
                // positons there are per millimetre
                coordinatePositionsPerMM = (colWidthMM == 0) ? 0
                        : ConvertImageUnits.TOTAL_COLUMN_COORDINATE_POSITIONS / colWidthMM;
                // From this figure, determine how many co-ordinat positions to
                // inset the left hand or bottom edge of the image.
                inset = (int) (coordinatePositionsPerMM * overlapMM);
            } else {
                inset = (int) overlapMM * AddDimensionedImage.EMU_PER_MM;
            }

            // Now create the ClientAnchorDetail object, setting the from and to
            // columns and the inset.
            anchorDetail = new ClientAnchorDetail(startingColumn, toColumn, inset);
        }
        return anchorDetail;
    }

}
