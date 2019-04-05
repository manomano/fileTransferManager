package com.example.demo;
import org.springframework.web.multipart.MultipartFile;

public class FormData {

    private MultipartFile file;
    private String path;

    public MultipartFile getFile() {
        return file;
    }

    public void setId(MultipartFile File) {
        this.file = File;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
