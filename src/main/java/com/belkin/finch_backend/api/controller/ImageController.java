package com.belkin.finch_backend.api.controller;

import com.belkin.finch_backend.security.jwt.JwtConfig;
import com.belkin.finch_backend.security.jwt.JwtTokenVerifier;
import com.belkin.finch_backend.service.ImageService;
import com.belkin.finch_backend.util.Base62;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/i")
public class ImageController {

    private final ImageService imageService;
    private final JwtTokenVerifier jwt;

    @Autowired
    public ImageController(ImageService imageService, JwtConfig jwtConfig, SecretKey secretKey) {
        this.imageService = imageService;
        this.jwt = new JwtTokenVerifier(jwtConfig, secretKey);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Resource> getImage(@PathVariable("id") Base62 id) {
        log.info("GET /i/{id}, where id='"+id.getId()+"'");

        Resource resource = imageService.download(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + ".jpg\"");
        headers.add(HttpHeaders.CONTENT_TYPE, "image/jpeg");
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public Base62 addImage(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("POST /i/upload with header Authorization = '" + authorizationHeader + "'");

        String username = jwt.getRequesterUsername(authorizationHeader);
        return imageService.upload(file, username);
    }

    @DeleteMapping(path ="/{id}")
    public String deleteImage(@PathVariable("id") Base62 id, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("GET /i/{id}, where id='"+id.getId() + "' with header Authorization = '" + authorizationHeader + "'");

        String myUsername = jwt.getRequesterUsername(authorizationHeader);
        boolean result = imageService.delete(id, myUsername);

        Optional<String> res = Optional.ofNullable(result ? "Success" : null);
        return res.orElseThrow(() -> new RuntimeException("Image delete failed"));
    }
}
