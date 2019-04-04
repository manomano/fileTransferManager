package com.example.demo.FileTransfer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;


@Service
public class XLSXReader {


    public void readXLSX(String path) {
        try {
            File excel = new File(path);
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
                        System.out.print(cell.getStringCellValue() + " -----");
                    }catch (Exception b) {

                    }
                }
                System.out.println("");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
