package com.belkin.finch_backend.dao.interfaces;

import com.belkin.finch_backend.model.ImageMetadata;
import com.belkin.finch_backend.util.Base62;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("database_image")
public interface ImageMetadataDAO extends CrudRepository<ImageMetadata, Base62> {

}


