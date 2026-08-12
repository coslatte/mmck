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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
public class FileManager {
    static String FILE_EXTENSION = ".txt";

    public static String generateHashCode() {
        String hash = Integer.toHexString(RandomGenerator.getDefault().nextInt());
        String date = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"));
        return String.join("-", date, hash);
    }

    public void writeContent(@NotNull Path filePath, @NotNull String fileContent) {
        try {
            Files.writeString(
                    filePath,
                    fileContent,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException("""
                    Error while writing content on file: %s
                    StackTrace: %s
                    """.formatted(filePath.getFileName() + FILE_EXTENSION, Arrays.toString(e.getStackTrace())));
        }
    }

    public void createFile(
            @Nullable String filename,
            @NotNull("Must specify a path. It's null.") @ValidDirectory String path,
            @Nullable String fileContent
    ) {
        // filename
        String finalFilename = Optional.ofNullable(filename)
                .filter(name -> !name.isBlank())
                .orElseGet(() -> "file-" + generateHashCode());

        // path
        Path filePath = Paths.get(path).resolve(finalFilename + FILE_EXTENSION);

        // building and writing
        try {
            Files.createFile(filePath);

            if (fileContent != null)
                writeContent(filePath, fileContent);

        } catch (IOException e) {
            String errorType = e.getClass().getSimpleName();
            String reason = (e.getMessage() != null && !e.getMessage().equals(filePath.toString()))
                    ? e.getMessage()
                    : "File already exists or path have an invalid format. (" + errorType + ")";
            throw new RuntimeException("""
                    Error creating file: %s
                    Reason: %s
                    """.formatted(filePath.getFileName(), reason)
            );
        }
    }

    public void exportDirectoryListing(@NotNull String outputFilename, @NotNull String directory) {
        Path targetDirectory = (directory.isBlank())
                // so this is for Minecraft purposes...
                ? Paths.get(System.getenv("APPDATA"), ".minecraft", "mods")
                : Paths.get(directory);

        try (Stream<Path> stream = Files.list(targetDirectory)) {
            String fileListContent = stream
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.joining(System.lineSeparator()));

            this.createFile(outputFilename, directory, fileListContent);

        } catch (IOException e) {
            throw new RuntimeException(">>>>> " + e);
        }
    }
}
