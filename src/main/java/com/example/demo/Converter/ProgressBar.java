package com.example.demo.Converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressBar {


    private int downloaded;
    private int total;

    public ProgressBar(int downloaded, int total) {
        this.downloaded = downloaded;
        this.total = total;
    }

    public int getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(int downloaded) {
        this.downloaded = downloaded;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public synchronized void inc() {
        this.downloaded++;
    }
}
