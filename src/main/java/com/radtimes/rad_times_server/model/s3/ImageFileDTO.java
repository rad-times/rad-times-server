package com.radtimes.rad_times_server.model.s3;

public class ImageFileDTO {

    private String fileName;

    private String base64;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
