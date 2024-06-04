package com.model.image;

public enum ImageFileType {
    PNG(".png"), JPG(".jpg"), JPEG(".jpeg"), GIF(".gif");

    private final String extension;

    ImageFileType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
