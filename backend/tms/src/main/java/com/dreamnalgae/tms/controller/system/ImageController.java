package com.dreamnalgae.tms.controller.system;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamnalgae.tms.model.system.ImageData;
import com.dreamnalgae.tms.service.system.FtpImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final FtpImageService ftpImageService;

    @GetMapping("/{kind}/{fileName}")
    public ResponseEntity<byte[]> getImage(
            @PathVariable String kind,
            @PathVariable String fileName) throws IOException {

        ImageData img = ftpImageService.fetch(kind, fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(img.getContentType()))
                .body(img.getBytes());
    }
    
}
