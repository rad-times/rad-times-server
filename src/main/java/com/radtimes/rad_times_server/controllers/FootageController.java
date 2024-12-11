package com.radtimes.rad_times_server.controllers;

import com.radtimes.rad_times_server.model.footage.ClipModel;
import com.radtimes.rad_times_server.model.s3.NewClipDTO;
import com.radtimes.rad_times_server.service.FootageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/footage")
public class FootageController {
    private final FootageService footageService;

    public FootageController(FootageService footageService) {
        this.footageService = footageService;
    }

    @GetMapping
    public Set<ClipModel> loadClipSet(@RequestParam Long startDateTime, @RequestParam(required = false, defaultValue="5") Integer count, @RequestParam(required = false) List<String> filters) {
        return footageService.loadClipSet(startDateTime, count, filters);
    }

    @PostMapping("/upload")
    public ClipModel uploadClip(@RequestBody NewClipDTO newClip, @RequestHeader Integer userId) {
        return footageService.createNewClip(newClip, userId);
    }

//    @GetMapping("/files/{fileName}/base64")
//    public ResponseEntity<String> getFileInBase64(@PathVariable("fileName") String fileName) {
//        return ResponseEntity.ok(imageManager.getFileInBase64(fileName));
//    }
//
//    @GetMapping("/files/{fileName}/download")
//    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) {
//        byte[] content = imageManager.getFileAsBytes(fileName);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_TYPE, footageService.getFileMediaType(fileName))
//                .header(HttpHeaders.CONTENT_DISPOSITION, MediaType.APPLICATION_OCTET_STREAM_VALUE)
//                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.length))
//                .body(new ByteArrayResource(content));
//    }
//
//    @DeleteMapping("/files/{fileName:.+}")
//    public ResponseEntity<Void> deleteFile(@PathVariable("fileName") String fileName) {
//        imageManager.deleteFile(fileName);
//        return ResponseEntity.ok().build();
//    }
}
