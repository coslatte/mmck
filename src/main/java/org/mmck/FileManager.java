package org.mmck;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.random.RandomGenerator;

public class FileManager {
    public static void createFile(
            @NotNull String path,
            @NotNull String filename,
            @Nullable String content
    ) {
        Objects.requireNonNull(path, "path is null");
        Objects.requireNonNull(filename, "filename is null");

        Path targetPath = (path.isBlank()) ? Path.of(System.getProperty("user.dir")) : Paths.get(path);

        try {
            Files.createFile(targetPath.resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (content != null) {
            try {
                Files.writeString(targetPath, content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String generateHashCode() {
        String hash = Integer.toHexString(RandomGenerator.getDefault().nextInt());
        String date = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm"));
        return String.join("-", date, hash);
    }
}
