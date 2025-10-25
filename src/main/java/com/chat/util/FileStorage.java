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
            Path target = baseDir.resolve(fileName);
            Files.copy(source.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Saved: " + target.toAbsolutePath());
        }catch(IOException e){
            throw new RuntimeException("Failed to save file: " + fileName, e);
        }
    }

}
