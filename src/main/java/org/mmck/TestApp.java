package org.mmck;

import org.mmck.annotations.ValidationProxy;
import org.mmck.service.FileManager;
import org.mmck.service.FileService;

import java.lang.reflect.Proxy;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestApp {
    public static void main(String[] args) {
        FileService fileService = (FileService) Proxy.newProxyInstance(
                FileManager.class.getClassLoader(),
                new Class<?>[]{FileService.class},
                new ValidationProxy(new FileManager())
        );

        fileService.exportDirectoryListing("cosa1", "");

        Path path1 = Paths.get(System.getProperty("user.dir"), "cosa1.txt");
        Path path2 = Paths.get(System.getProperty("user.dir"), "cosa2.txt");
        fileService.compareModFiles(path1, path2);
    }
}
