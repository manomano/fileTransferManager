package com.example.demo.FileCheck;

import com.example.demo.FileInfo.FileInfo;
import com.example.demo.FileTransfer.XLSXReader;
import jcifs.smb.SmbFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

import static com.example.demo.Converter.FileConverter.toFile;

@Service
public class FileChecker {

    @Autowired
    private XLSXReader xlsxReader;



    public Map<String, List<String>> check(MultipartFile excel, String dir) throws Exception{
        List<FileInfo> fileInfo = xlsxReader.readXLSX(toFile(excel));
        Map<String, List<String>> map = getMap(fileInfo);
        Set<String> keySet = map.keySet();
        Map<String, List<String>> missingPhotos = new HashMap<>();

        for (String key : keySet) {
            List<String> photos = new ArrayList<>();
            try {
                File subdir = new File(dir + "/" + key);
                photos = getPhotoNamesFromFile(subdir);
            }catch (Exception e) {
                e.printStackTrace();
            }

            List<String> missing = new ArrayList<>();

            for (String value : map.get(key)) {
                if (!photos.contains(value))
                    missing.add(value);
            }
            missingPhotos.put(key, missing);
        }

        return missingPhotos;
    }

    public Map<String, List<String>> reverseCheck(MultipartFile excel, String dir) throws Exception {
        File directory = new File(dir);
        List<FileInfo> fileInfo = xlsxReader.readXLSX(toFile(excel));
        Map<String, List<String>> map = getMap(fileInfo);
        Map<String, List<String>> missing = new HashMap<>();

        for (File f : directory.listFiles()) {
            List<String> curMissing = new ArrayList<>();
            for (File f2 : f.listFiles()) {
                String photoName = f2.getName().split("\\.")[0];
                if (!map.get(f.getName()).contains(photoName))
                    curMissing.add(photoName);
            }
            missing.put(f.getName(), curMissing);
        }
        return missing;
    }




    private List<String> getPhotoNamesFromFile(File file) {
        List<String> photos = new ArrayList<>();
        for (File f : file.listFiles()) {
            photos.add(f.getName().split("\\.")[0]);
        }
        return photos;
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

    private String getImageName(SmbFile dir){
        String[] arr = dir.getName().split("/");
        return arr[arr.length-1].split("\\.")[0];
    }

}
