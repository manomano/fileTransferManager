package com.example.demo;


import com.example.demo.FileInfo.FileInfo;
import com.example.demo.FileTransfer.SmbConnector;
import com.example.demo.FileTransfer.XLSXReader;
import jcifs.smb.SmbFile;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileDownloader {

    @Autowired
    private XLSXReader xlsxReader;

    @Autowired
    private SmbConnector smbConnector;



    public List<FileInfo> readFile(MultipartFile file, String path) throws IOException {

        File rawFile = toFile(file);
        List<FileInfo> imageList =  xlsxReader.readXLSX(rawFile);

        int workerNum = 5;
        int partition = imageList.size() /workerNum;
        Worker[] workers = new Worker[workerNum];
        int nashti = 0;

       for (int i = 0; i < workerNum ; i++) {
            List<FileInfo> workerPart = new ArrayList<>();
            if(i==workerNum-1){
                nashti = imageList.size() - partition * workerNum;
            }
            for (int j = 0; j < (partition+nashti); j++) {
                workerPart.add(imageList.get(i*partition + j));
            }

           Worker worker = new Worker(workerPart,path);
           workers[i] = worker;
           worker.start();

           System.out.println("ulupa: "+workerPart.size());

        }


        for (int i = 0; i <workers.length ; i++) {
           try {
               workers[i].join();
           }catch (InterruptedException d){
               d.printStackTrace();
           }

        }

        return null;
    }

    public void download(String path, List<FileInfo> imageList) throws Exception {
        Map<String,List<String>> dictionaryMap = new HashMap();
        for(FileInfo row:imageList) {
            if (dictionaryMap.containsKey(row.getTrajectory())){
                dictionaryMap.get(row.getTrajectory()).add(row.getFileName());
            }else{
                List<String> emptyList = new ArrayList<>();
                emptyList.add(row.getFileName());
                dictionaryMap.put(row.getTrajectory(), emptyList);
            }
        }
        int counter = 0;
        try {
            SmbFile dir = smbConnector.getMainDirectory();
            for (SmbFile folder : dir.listFiles()) {
                String folderName = folder.getName().split("_")[0];
                if (dictionaryMap.containsKey(folderName)) {
                    List<String> folderNameList = dictionaryMap.get(folderName);


                    for (SmbFile preFolder : folder.listFiles()) {
                        //String[] arr = preFolder.getName().split("/");
                        //String exactImageFolderName = arr[arr.length-1];
                        String exactImageFolderName = this.getDirName(preFolder);

                        if (exactImageFolderName.equals(folderName + "_ExportPanorama")) {

                            for (SmbFile fileObj : preFolder.listFiles()) {
                                if (folderNameList.contains(this.getImageName(fileObj))) {

                                    String destFolderPath = path + "/" + folderName;
                                    File theDir = new File(destFolderPath);
                                    if (!theDir.exists()) {
                                        theDir.mkdir();
                                    }

                                    FileOutputStream outputStream  = new FileOutputStream(new File(theDir, this.getDirName(fileObj)));
                                    InputStream inputStream = fileObj.getInputStream();
                                    IOUtils.copy(fileObj.getInputStream(), outputStream);
                                    inputStream.close();
                                    outputStream.close();

                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e) {
            System.out.println("failed " + counter++);
            e.printStackTrace();
        }
    }








    private String getDirName(SmbFile dir){
        String[] arr = dir.getName().split("/");
        return arr[arr.length-1];
    }

    private String getImageName(SmbFile dir){
        String[] arr = dir.getName().split("/");
        return arr[arr.length-1].split("\\.")[0];
    }




    private File toFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }


    private class Worker extends Thread {
        private List<FileInfo> toDo;
        private String path;
        public Worker(List<FileInfo> toDo, String path){
            this.toDo = toDo;
            this.path = path;
        }

        public void run() {
            try {
                download(path,toDo);
            }catch (Exception e){

            }

        }
    }


    /*public String splitFolder(String PathTo, int chunckNum) throws Exception {

        File Directory = new File(PathTo);
        File[] contents = Directory.listFiles();
        List<String,String> files;

        for(File obj: contents){
            obj.getName()
        }


        return null;
    }*/


}
