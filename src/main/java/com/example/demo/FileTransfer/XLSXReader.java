package com.example.demo.FileTransfer;

import com.example.demo.FileInfo.FileInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
public class XLSXReader {


    public List<FileInfo> readXLSX(File excel) {
        List<FileInfo> result = new ArrayList<>();
        try {

            FileInputStream fis = new FileInputStream(excel);
            XSSFWorkbook book = new XSSFWorkbook(fis);
            XSSFSheet sheet = book.getSheetAt(0);
            Iterator<Row> itr = sheet.iterator();

            while (itr.hasNext()) {
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while(cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    try {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setTrajectory(cell.getStringCellValue());
                        fileInfo.setFileName(cellIterator.next().getStringCellValue());
                        result.add(fileInfo);
                    }catch (Exception b) {

                    }
                }
                System.out.println("");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
