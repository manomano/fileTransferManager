package com.example.demo;

import com.example.demo.FileTransfer.SmbConnector;
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
    @GetMapping("/")
    public String hello(){
        return "heloo!";
    }

    @GetMapping("/test")
    public String bla() {
        smbConnector.testConnection();
        return "worked";
    }

}
