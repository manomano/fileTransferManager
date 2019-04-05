package com.example.demo;

import com.example.demo.FileInfo.FileInfo;
import com.example.demo.FileTransfer.SmbConnector;
import com.example.demo.FileTransfer.XLSXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class RameCotroller {

    @Autowired
    private SmbConnector smbConnector;

    @Autowired
    private XLSXReader xlsxReader;

    @GetMapping("/ada")
    public String hello(){
        return "heloo!";
    }

    @GetMapping("/test")
    public String bla() {
        smbConnector.testConnection();
        return "worked";
    }

    /*@GetMapping("/readFile")
    public ResponseEntity<List<FileInfo>> rfile() {
        return ResponseEntity.ok().body(xlsxReader.readXLSX("C:\\Users\\\\mjaparidze\\Downloads\\Test.xlsx"));
    }*/

    @Autowired
    private FileDownloader fileDownloader;

    @PostMapping("/excel")
    public ResponseEntity  <String> submitFileAndLocation(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) throws Exception{
        fileDownloader.download(file, path);
        return ResponseEntity.ok().body("" + path);

    }





}
