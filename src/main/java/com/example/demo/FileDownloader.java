package com.example.demo;


import com.example.demo.FileInfo.FileInfo;
import com.example.demo.FileTransfer.SmbConnector;
import com.example.demo.FileTransfer.XLSXReader;
import jcifs.smb.SmbFile;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
        Map<String,List<String>> dictionaryMap = new HashMap();

        for(FileInfo row:imageList) {
            if (dictionaryMap.containsKey(row.getTrajectory())){
                dictionaryMap.get(row.getTrajectory()).add(row.getFileName());
            }else{
                List<String> emptyList = new ArrayList<>();
                dictionaryMap.put(row.getTrajectory(), emptyList);
            }
        }


        try {
            SmbFile dir = smbConnector.getMainDirectory();

          /*  for(String folderNameFromExcel : dictionaryMap.keySet()){
                for (SmbFile folder : dir.listFiles((SmbFileFilter) (dir1, name) -> name.startsWith(folderNameFromExcel+"_"))){


                }
            }
*/

            for (SmbFile folder : dir.listFiles()) {
                String folderName = folder.getName().split("_")[0];
                if(dictionaryMap.containsKey(folderName)){
                    List<String> folderNameList = dictionaryMap.get(folderName);

                    for(SmbFile preFolder : folder.listFiles()){
                        //String[] arr = preFolder.getName().split("/");
                        //String exactImageFolderName = arr[arr.length-1];
                        String exactImageFolderName =  this.getDirName(preFolder);

                        if(exactImageFolderName.equals(folderName+"_ExportPanorama")){

                            for(SmbFile fileObj : preFolder.listFiles()){
                                if(folderNameList.contains(this.getImageName(fileObj))){

                                    String destFolderPath = path+"/"+folderName;
                                    File theDir = new File(destFolderPath);
                                    if (!theDir.exists()) {
                                        theDir.mkdir();
                                    }

                                    //IOUtils.copy(fileObj.getInputStream(), new FileOutputStream(new File(theDir, file.getName())));
                                    IOUtils.copy(fileObj.getInputStream(), new FileOutputStream(new File(theDir, this.getDirName(fileObj))));
                                }
                            }

                        }

                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }



        return null;
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


}
