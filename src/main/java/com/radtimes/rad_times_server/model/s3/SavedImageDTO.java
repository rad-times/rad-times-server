package com.radtimes.rad_times_server.model.s3;

import java.util.Date;

public class SavedImageDTO {

    private String originalFileName;
    private String generatedFileName;
    private Date uploadedAt;

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getGeneratedFileName() {
        return generatedFileName;
    }

    public void setGeneratedFileName(String generatedFileName) {
        this.generatedFileName = generatedFileName;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
