package com.example;

public class ClientAnchorDetail {
    private int fromIndex;
    public int toIndex;
    private int inset;

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
