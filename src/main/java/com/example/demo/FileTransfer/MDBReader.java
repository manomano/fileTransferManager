package com.example.demo.FileTransfer;

import com.example.demo.FileInfo.FileInfo;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class MDBReader {

    public List<FileInfo> readMdb(File file) throws IOException {

        List<FileInfo> fileInfos = new ArrayList<>();
        Database db = DatabaseBuilder.open(file);
        Table table = db.getTable("All_Panoramas");
        for (Row row : table) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setTrajectory(row.get("TRAJECTORY").toString());
            fileInfo.setFileName(row.get("FILE_NAME").toString());
            fileInfos.add(fileInfo);
        }


        return fileInfos;
    }
}
