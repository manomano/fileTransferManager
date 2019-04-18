package com.example.demo;




public class FireSending implements Runnable {

    private ProcessDepicting listener;

    public FireSending(ProcessDepicting listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep( 2000 );
                listener.updateInfo();
                if(FileDownloader.FileCounter==FileDownloader.FilesToDownload){
                    break;
                }
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        }
    }
}
