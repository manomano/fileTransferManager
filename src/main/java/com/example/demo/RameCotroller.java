package com.example.demo;

import com.example.demo.FileTransfer.SmbConnector;
import com.example.demo.FileTransfer.XLSXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class RameCotroller {

    @Autowired
    private SmbConnector smbConnector;

    @Autowired
    private XLSXReader xlsxReader;

    @GetMapping("/")
    public String hello(){
        return "heloo!";
    }

    @GetMapping("/test")
    public String bla() {
        smbConnector.testConnection();
        return "worked";
    }

    @GetMapping("/readFile")
    public String rfile() {
        xlsxReader.readXLSX("C:\\Users\\gpataraia\\Downloads\\Test.xlsx");
        return "file read";
    }

}
