package com.radtimes.rad_times_server.model.s3;

import lombok.Data;

@Data
public class NewClipDTO {
    private String photoData;
    private String photoName;
    private Long createDate;
    private Integer spotId;
}
