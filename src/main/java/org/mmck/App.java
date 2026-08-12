package org.mmck;

import org.mmck.annotations.ValidationProxy;
import org.mmck.services.FileService;

import java.lang.reflect.Proxy;

public class App {
    public static void main(String[] args) {
        FileService fileService = (FileService) Proxy.newProxyInstance(
                FileManager.class.getClassLoader(),
                new Class<?>[]{FileService.class},
                new ValidationProxy(new FileManager())
        );
    }
}
