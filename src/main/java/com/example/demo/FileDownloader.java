package com.example.demo;


import com.example.demo.FileInfo.FileInfo;
import com.example.demo.FileInfo.imageInfo;
import com.example.demo.FileTransfer.SmbConnector;
import com.example.demo.FileTransfer.XLSXReader;
import jcifs.smb.SmbFile;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.*;

import static com.example.demo.Converter.FileConverter.toFile;

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
        Map<String,List<String>> dictionaryMap = getMap(imageList);
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




    public void downloadFromLocal(String source, String dest, MultipartFile file) throws Exception {
        File rawFile = toFile(file);
        List<FileInfo> imageList =  xlsxReader.readXLSX(rawFile);
        Map<String,List<String>> dictionaryMap = getMap(imageList);

        for (String key: dictionaryMap.keySet()) {
            String s = source + "\\" + key + "\\";
            String d = dest + "\\" + key + "\\";
            for (String fileName : dictionaryMap.get(key)) {
                try {
                    File fileToDownload = new File(s + fileName + ".jpg");
                    File fileDest = new File(d) ;
                    if (!fileDest.exists()) {
                        fileDest.mkdir();
                    }

                    FileInputStream fileInputStream = new FileInputStream(fileToDownload);
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(d, fileName + ".jpg"));

                    IOUtils.copy(fileInputStream, fileOutputStream);

                    fileInputStream.close();
                    fileOutputStream.close();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
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

    private Map<String, List<String>> getMap(List<FileInfo> fileInfos) {
        Map<String, List<String>> result = new HashMap<>();
        for(FileInfo row:fileInfos) {
            if (result.containsKey(row.getTrajectory())){
                result.get(row.getTrajectory()).add(row.getFileName());
            }else{
                List<String> emptyList = new ArrayList<>();
                emptyList.add(row.getFileName());
                result.put(row.getTrajectory(), emptyList);
            }
        }
        return  result;
    }


    public void splitFolder(String parentPath, String PathTo, int chunckCount, String folderName) throws Exception {

        File Directory = new File(PathTo);
        File[] contents = Directory.listFiles();
        List<imageInfo> fileGrid = new ArrayList<>();;

        for(File obj: contents){
            imageInfo row  = new imageInfo();
            row.setName(obj.getName());
            row.setCreationDate(obj.lastModified());
            fileGrid.add(row);
        }

        fileGrid.sort(imageInfo::compareTo);

        int folderCount = fileGrid.size()/chunckCount;
        int nashti = fileGrid.size() - folderCount * chunckCount;
        if(nashti>0){
            folderCount++;
        }



        String curFolderName = "";
        int index = chunckCount;
        int k = 2;
        File firstDir = new File(parentPath+"\\"+folderName+"_2");
        if (!firstDir.exists()) {
            firstDir.mkdir();
        }


        for(int i = chunckCount; i <fileGrid.size() ; i++){
            File sourceFile = new File(PathTo+"/"+fileGrid.get(i).getName());
            if(sourceFile.renameTo(new File(parentPath+"\\"+folderName+"_"+k+"\\"+fileGrid.get(i).getName()))){
                System.out.println("File is moved successful!" + fileGrid.get(i).getName());
            }else{
                System.out.println("File is failed to move" + fileGrid.get(i).getName());
            }

            if(index==k*chunckCount-1){
                k++;
                File theDir = new File(parentPath+"\\"+folderName+"_"+k);
                if (!theDir.exists()) {
                    theDir.mkdir();
                }
            }
            index++;
        }

        //rename first dir
        File initialDir = new File(PathTo);
        initialDir.renameTo(new File(PathTo+"_1"));
    }


}
