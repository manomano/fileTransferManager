package com.example.demo;


import com.example.demo.FileInfo.FileInfo;
import com.example.demo.FileTransfer.XLSXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class FileDownloader {

    @Autowired
    private XLSXReader xlsxReader;


    public FileInfo download(MultipartFile file, String path) throws IOException {

        File rawFile = toFile(file);
        List<FileInfo> imageList =  xlsxReader.readXLSX(rawFile);


        return null;
    }


    private File toFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }


}
