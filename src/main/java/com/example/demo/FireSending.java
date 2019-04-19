package com.example.demo;


import com.example.demo.Converter.Progress;

public class FireSending implements Runnable {

    private ProcessDepicting listener;
    private Integer index;

    public FireSending(ProcessDepicting listener, Integer index) {
        this.listener = listener;
        this.index = index;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep( 2000 );
                listener.updateInfo(index);
                if(Progress.PROGRESS_BAR.get(index).getTotal() ==Progress.PROGRESS_BAR.get(index).getDownloaded()){
                    break;
                }
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        }
    }
}
