package org.mmck.services;

// because I wanted to use some sort of spring proxy for intercepting parameters like annotations do

public interface FileService {
    void createFile(String filename, String path, String content);
}
