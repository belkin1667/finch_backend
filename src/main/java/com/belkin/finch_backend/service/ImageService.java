package com.belkin.finch_backend.service;

import com.belkin.finch_backend.dao.interfaces.ImageMetadataDAO;
import com.belkin.finch_backend.exception.AccessDeniedException;
import com.belkin.finch_backend.exception.ImageStorageException;
import com.belkin.finch_backend.exception.notfound.ImageMetadataNotFoundException;
import com.belkin.finch_backend.model.ImageMetadata;
import com.belkin.finch_backend.util.Base62;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class ImageService {

    private ImageMetadataDAO imageMetadataDAO;

    @Autowired
    public ImageService(@Qualifier("image_fake") ImageMetadataDAO imageMetadataDAO) {
        this.imageMetadataDAO = imageMetadataDAO;
    }

    public Base62 upload(MultipartFile file, String uploaderUsername) {

        Base62 id;
        do {
            id = Base62.randomBase62(ImageMetadata.ID_LENGTH);
        } while (imageMetadataDAO.isPresent(id));
        String originalFileName = file.getOriginalFilename();
        String ext = originalFileName == null ? "" : originalFileName.substring(originalFileName.lastIndexOf('.'));
        ImageMetadata imageMetadata = new ImageMetadata(uploaderUsername, id, ext);
        imageMetadataDAO.addImage(imageMetadata);
        try {
            Path copyLocation = Paths.get(StringUtils.cleanPath(imageMetadata.getPath()));
            if (Files.exists(copyLocation))
                throw new IOException();
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            imageMetadataDAO.deleteImage(id);
            throw new ImageStorageException(file.getOriginalFilename());
        }
        return id;
    }

    public Resource download(Base62 id) {
        String path = ImageMetadata.BASE_PATH + File.separator + id + ".jpg";
        String finalPath = path;
        ImageMetadata imageMetadata = imageMetadataDAO.getImageById(id)
                .or(() -> {
                    ImageMetadata imd = null;
                    if (Files.exists(Path.of(finalPath))) {
                        imd = new ImageMetadata(null, id, ".jpg");
                        imageMetadataDAO.addImage(imd);
                    }
                    return Optional.ofNullable(imd);
                }).orElseThrow(() -> new ImageMetadataNotFoundException(id));
        path = imageMetadata.getPath();
        return new FileSystemResource(path);
    }

    public boolean delete(Base62 id, String myUsername) {
        ImageMetadata imageMetadata = imageMetadataDAO.getImageById(id).orElseThrow(() -> new ImageMetadataNotFoundException(id));
        if (!Files.exists(Path.of(imageMetadata.getPath()))) {
            throw new ImageMetadataNotFoundException(id);
        }

        if (imageMetadata.getUploaderUsername().equals(myUsername)) {
            try {
                Files.delete(Path.of(imageMetadata.getPath()));
            } catch (IOException e) {
                return false;
            }
            return imageMetadataDAO.deleteImage(id);
        } else {
            throw new AccessDeniedException(myUsername);
        }
    }
}
