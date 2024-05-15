package com.example;
/**
 * The HSSFClientAnchor class accepts eight arguments. In order, these are;
 * <p>
 * * How far the left hand edge of the image is inset from the left hand
 * edge of the cell
 * * How far the top edge of the image is inset from the top of the cell
 * * How far the right hand edge of the image is inset from the left
 * hand edge of the cell
 * * How far the bottom edge of the image is inset from the top of the
 * cell.
 * * Together, arguments five and six determine the column and row
 * coordinates of the cell whose top left hand corner will be aligned
 * with the images top left hand corner.
 * * Together, arguments seven and eight determine the column and row
 * coordinates of the cell whose top left hand corner will be aligned
 * with the images bottom right hand corner.
 * <p>
 * An instance of the ClientAnchorDetail class provides three of the eight
 * parameters, one of the coordinates for the images top left hand corner,
 * one of the coordinates for the images bottom right hand corner and
 * either how far the image should be inset from the top or the left hand
 * edge of the cell.
 *
 * @version 1.00 5th August 2009.
 */
public class ClientAnchorDetail {
    private int fromIndex;
    private int toIndex;
    private int inset;

    /**
     *
     * @param fromIndex A int that contains one of the coordinates (row or column index) for the top left hand corner
     *                 of the image.
     * @param toIndex   A int that contain one of the coordinates for the bottom right hand corner of the image
     * @param inset     A int that contains a value which indicated how far the image should be inset from the top
     *                  or the left hand edge of a cell
     */
    public ClientAnchorDetail(int fromIndex, int toIndex, int inset) {
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.inset = inset;
    }

    public int getFromIndex() {
        return fromIndex;
    }

    public int getToIndex() {
        return toIndex;
    }

    public int getInset() {
        return inset;
    }
}
