package com.belkin.finch_backend;

import com.belkin.finch_backend.model.ImageMetadata;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@Order(2)
public class StartupRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        emptyImageDirectory();
    }

    private void emptyImageDirectory() throws IOException {
        Files.list(Paths.get(ImageMetadata.BASE_PATH))
                .map(path -> {
                    try {
                        return Files.deleteIfExists(path);
                    } catch (IOException e) {
                        return false;
                    }
            });
    }
}
