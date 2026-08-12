package org.mmck.service;

// because I wanted to use some sort of spring proxy for intercepting parameters like annotations do

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mmck.annotations.ValidDirectory;

import java.nio.file.Path;

public interface FileService {
    void createFile(@Nullable String filename, @NotNull String path, @Nullable String content);
    void writeContent(@NotNull Path filePath, @NotNull String fileContent);
    void exportDirectoryListing(@Nullable String outputFilename, @NotNull @ValidDirectory String directory);
    void compareModFiles(@NotNull Path baseFilePath, @NotNull Path targetFilePath);
}
