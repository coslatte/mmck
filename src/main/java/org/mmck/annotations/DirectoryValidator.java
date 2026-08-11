package org.mmck.annotations;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryValidator {
    public static Path validateAndResolve(String path, ValidDirectory annotation) {
        Path targetPath = (path == null || path.isBlank())
                ? Path.of(System.getProperty("user.dir"))
                : Paths.get(path);

        if (!Files.exists(targetPath) || !Files.isDirectory(targetPath)) {
            String message = (annotation != null) ? annotation.message() : "Invalid directory.";
            throw new IllegalArgumentException(message + " -> " + targetPath);
        }

        return targetPath;
    }
}
