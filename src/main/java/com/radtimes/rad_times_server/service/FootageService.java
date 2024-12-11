package com.radtimes.rad_times_server.service;

import com.radtimes.rad_times_server.model.footage.ClipModel;
import com.radtimes.rad_times_server.model.s3.NewClipDTO;
import com.radtimes.rad_times_server.model.s3.SavedClipDTO;
import com.radtimes.rad_times_server.repository.FootageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class FootageService {
    private final FootageRepository footageRepository;
    private final ImageManagerService imageManager;

    public FootageService(FootageRepository footageRepository, ImageManagerService imageManager) {
        this.footageRepository = footageRepository;
        this.imageManager = imageManager;
    }

    public String getFileMediaType(String fileName) {
        String mediaType;
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
        switch (fileExtension.toLowerCase()) {
            case "pdf":
                mediaType = MediaType.APPLICATION_PDF_VALUE;
                break;
            case "png":
                mediaType = MediaType.IMAGE_PNG_VALUE;
                break;
            case "jpeg":
                mediaType = MediaType.IMAGE_JPEG_VALUE;
                break;
            default:
                mediaType = MediaType.TEXT_PLAIN_VALUE;
        }
        return mediaType;
    }

    /**
     * Load a set of clips for the user. Pulls <count> number of clips starting from <startDateTime> and moving back in time
     */
    public Set<ClipModel> loadClipSet(Long startDateTime, Integer count, List<String> filters) {
        try {
            Limit limit = Limit.of(count);
            Optional<Set<ClipModel>> clips = footageRepository.findByCreateDateBeforeOrderByCreateDateDesc(startDateTime, limit);

            return clips.orElseGet(Set::of);

        } catch (Exception err) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to get set of clips from request: " + err.getMessage());
        }

    }
    /**
     * Creates a new clip from user upload
     */
    public ClipModel createNewClip(NewClipDTO newClipRaw, Integer userId) {
        try {
            SavedClipDTO savedFileResp = imageManager.uploadFile(newClipRaw);

            ClipModel newClip = new ClipModel();
            newClip.setLocation_id(newClipRaw.getSpotId());
            newClip.setCreateDate(newClipRaw.getCreateDate());
            newClip.setLikeCount(0);
            newClip.setPerson_id(userId);
            newClip.setS3FileName(savedFileResp.getGeneratedFileName());

            return footageRepository.save(newClip);

        } catch (Exception err) {
            // Handle exception or log the error
            throw new RuntimeException("Failed to save new clip: " + err.getMessage());
        }
    }
}
