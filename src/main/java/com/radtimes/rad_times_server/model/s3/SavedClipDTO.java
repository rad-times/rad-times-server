package com.radtimes.rad_times_server.model.s3;

import lombok.Data;
import java.util.Date;

@Data
public class SavedClipDTO {
    private String originalFileName;
    private String generatedFileName;
    private Date uploadedAt;
}
