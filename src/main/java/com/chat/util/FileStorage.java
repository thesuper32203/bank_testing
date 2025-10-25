package com.chat.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class FileStorage {

    private final Path baseDir;

    public FileStorage(String dir){
        this.baseDir = Path.of(dir);
    }

    public void save(File source, String fileName){
        try{
            if(!Files.exists(baseDir)) Files.createDirectories(baseDir);
            Files.copy(source.toPath(), baseDir.resolve(fileName));
            System.out.println("File created");
        }catch(IOException e){
            throw new RuntimeException("Failed to save file: " + fileName, e);
        }
    }

}
