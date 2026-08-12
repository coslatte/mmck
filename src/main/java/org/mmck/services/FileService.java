package org.mmck.services;

// because I wanted to use some sort of spring proxy for intercepting parameters like annotations do

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mmck.annotations.ValidDirectory;

public interface FileService {
    void createFile(@Nullable String filename, @NotNull String path, @Nullable String content);
    void exportDirectoryListing(@Nullable String outputFilename, @NotNull @ValidDirectory String directory);
    void writeContentFile(@NotNull String path, @NotNull String content);
}
