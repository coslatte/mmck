package org.mmck;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.mmck.annotations.ValidDirectory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.random.RandomGenerator;

@NoArgsConstructor
public class FileManager {
    static String FILE_EXTENSION = ".txt";

    public static void writeFile(
            @NotNull Path path,
            @NotNull String content
    ) {
        try {
            Files.writeString(path, content, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing content on file: "
                    + path.resolve(path.getFileName() + FILE_EXTENSION)
                    + " >>> "
                    + e.getMessage() + Arrays.toString(e.getStackTrace())
            );
        }
    }

    public static String generateHashCode() {
        String hash = Integer.toHexString(RandomGenerator.getDefault().nextInt());
        String date = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"));
        return String.join("-", date, hash);
    }

    public void createFile(
            @Nullable String filename,
            @NotNull("path is null") @ValidDirectory String path,
            @Nullable String content
    ) {
        // filename
        String finalFilename = Optional.ofNullable(filename)
                .filter(name -> !name.isBlank())
                .orElseGet(() -> "file-" + generateHashCode());

        // path
        Path targetBasePath = (path.isBlank()) ? Path.of(System.getProperty("user.dir")) : Paths.get(path);
        Path fullFilePath = targetBasePath.resolve(finalFilename + FILE_EXTENSION);

        // building
        try {
            Files.createFile(fullFilePath);
        } catch (IOException e) {
            String errorType = e.getClass().getSimpleName();
            String reason = (e.getMessage() != null && !e.getMessage().equals(fullFilePath.toString()))
                    ? e.getMessage()
                    : "File already exists or path have an invalid format. (" + errorType + ")";

            throw new RuntimeException(
                    """
                            Error creating file: %s
                            Reason: %s
                            """.formatted(fullFilePath.getFileName(), reason)
            );
        }

        // content
        if (content != null) {
            writeFile(fullFilePath, content);
        }
    }
}
