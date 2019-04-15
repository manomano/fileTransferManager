package com.example.demo;

import com.example.demo.FileCheck.FileChecker;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RameCotroller {

    @Autowired
    private SmbConnector smbConnector;

    @Autowired
    private FileChecker fileChecker;

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

    @Autowired
    private FileDownloader fileDownloader;

    @PostMapping("/excel")
    public ResponseEntity  <String> submitFileAndLocation(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) throws Exception{
        fileDownloader.readFile(file, path);
        return ResponseEntity.ok().body("" + path);

    }

    @PostMapping("/localDownload")
    public ResponseEntity  <String> submitFileAndLocation(@RequestParam("file") MultipartFile file, @RequestParam("dest") String dest, @RequestParam("src") String src) throws Exception{
        fileDownloader.downloadFromLocal(src,dest,file);
        return ResponseEntity.ok().body("" + dest);

    }


    @PostMapping("/splitFolder")
    public ResponseEntity <String> splitFolder(@RequestParam("folderPath") String folderPath, @RequestParam("chunckSize") int chunckSize)throws Exception{
        String[] arr =  folderPath.split("\\\\");

        fileDownloader.splitFolder(folderPath.replace("\\"+arr[arr.length-1],""), folderPath, chunckSize, arr[arr.length-1]);

        return null;
    }




    @PostMapping("/check")
    public ResponseEntity<Map<String,List<String>>> check(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) throws Exception {
        return ResponseEntity.ok().body(fileChecker.check(file,path));
    }

    @PostMapping("/reverseCheck")
    public ResponseEntity<Map<String,List<String>>> reverseCheck(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) throws Exception {
        return ResponseEntity.ok().body(fileChecker.reverseCheck(file,path));
    }



    @PostMapping("/checkboth")
    public ResponseEntity<Map<String, Map<String, List<String>>>> checkboth(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) throws Exception {
        return ResponseEntity.ok().body(fileChecker.checkboth(file,path));
    }


    @PostMapping("/checkbothtest")
    public String checkbothtest(@RequestParam("file") MultipartFile file, @RequestParam("path") String path, RedirectAttributes redirectAttributes) throws Exception {
        /*ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("tst", fileChecker.checkboth(file,path));
        modelAndView.setViewName("redirect:/checkbothtest");*/
        redirectAttributes.addFlashAttribute(fileChecker.checkboth(file,path));
        return "redirect:/checkbothtest";
    }

    @GetMapping("/checkbothtest")
    public String handleGetRequest(Model model) {
        Object bla = model.asMap().get("hashMapList");
//        Map<String,List<String>> myObj = model.asMap().get("hashMapList");
//        List<HashMap<String,String>> res = (List<Map>)model.asMap().get("hashMapList");
        return "haai";
    }










}
