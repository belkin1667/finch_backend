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

    private final ImageMetadataDAO imageMetadataDAO;

    @Autowired
    public ImageService(@Qualifier("database_image") ImageMetadataDAO imageMetadataDAO) {
        this.imageMetadataDAO = imageMetadataDAO;
    }

    public Base62 uploadGenerateId(MultipartFile file, String uploaderUsername) {

        Base62 id;
        do {
            id = Base62.randomBase62(ImageMetadata.ID_LENGTH);
        } while (imageMetadataDAO.existsById(id));

        String originalFileName = file.getOriginalFilename();
        String ext = originalFileName == null ? "" : originalFileName.substring(originalFileName.lastIndexOf('.'));

        return upload(id, uploaderUsername, ext, file);
    }

    public Base62 uploadNoId(MultipartFile file, String uploaderUsername) {
        String originalFileName = file.getOriginalFilename();
        String ext = originalFileName == null ? "" : originalFileName.substring(originalFileName.lastIndexOf('.'));
        String id = originalFileName == null ? "" : originalFileName.substring(0, originalFileName.lastIndexOf('.'));
        return upload(new Base62(id), uploaderUsername, ext, file);
    }

    private Base62 upload(Base62 id, String  uploaderUsername, String ext, MultipartFile file) {
        ImageMetadata imageMetadata = new ImageMetadata(uploaderUsername, id, ext);
        imageMetadataDAO.save(imageMetadata);
        try {
            Path copyLocation = Paths.get(StringUtils.cleanPath(imageMetadata.getPath()));
            if (Files.exists(copyLocation))
                throw new IOException();
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            imageMetadataDAO.deleteById(id);
            throw new ImageStorageException(file.getOriginalFilename());
        }
        return id;
    }

    public Resource download(Base62 id) {
        String path = ImageMetadata.BASE_PATH + File.separator + id + ".jpg";
        String finalPath = path;
        ImageMetadata imageMetadata = imageMetadataDAO.findById(id)
                .or(() -> {
                    ImageMetadata imd = null;
                    if (Files.exists(Path.of(finalPath))) {
                        imd = new ImageMetadata(null, id, ".jpg");
                        imageMetadataDAO.save(imd);
                    }
                    return Optional.ofNullable(imd);
                }).orElseThrow(() -> new ImageMetadataNotFoundException(id));
        path = imageMetadata.getPath();
        return new FileSystemResource(path);
    }

    public boolean delete(Base62 id, String myUsername) {
        ImageMetadata imageMetadata = imageMetadataDAO.findById(id).orElseThrow(() -> new ImageMetadataNotFoundException(id));
        if (!Files.exists(Path.of(imageMetadata.getPath()))) {
            throw new ImageMetadataNotFoundException(id);
        }

        if (imageMetadata.getUploaderUsername().equals(myUsername)) {
            try {
                Files.delete(Path.of(imageMetadata.getPath()));
            } catch (IOException e) {
                return false;
            }
            imageMetadataDAO.deleteById(id);
            return true;
        } else {
            throw new AccessDeniedException(myUsername);
        }
    }
}
