package com.example.demo;
import com.example.demo.Converter.Progress;
import com.example.demo.Converter.ProgressBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;

@Controller
public class ProcessDepicting {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/excel")
    @SendTo("/download/copyingFromServer")
    public String fileProgress(Integer index) throws Exception {
        FireSending fSending = new FireSending(this, index);
        fSending.run();
        ProgressBar p = Progress.PROGRESS_BAR.get(index);
        return ""+p.getDownloaded() +"-"+p.getTotal();
        //return FileDownloader.FileCounter;
    }

    @MessageMapping("/localDownload")
    @SendTo("/download/copyingFromServer")
    public String fileProgressLoc(Integer index) throws Exception {
        FireSending fSending = new FireSending(this, index);
        ProgressBar p = Progress.PROGRESS_BAR.get(index);
        return ""+p.getDownloaded() +"-"+p.getTotal();
        //return FileDownloader.FileCounter;
    }


    public void updateInfo (Integer index){
        ProgressBar p = Progress.PROGRESS_BAR.get(index);
            this.template.convertAndSend("/download/copyingFromServer", ""+ p.getDownloaded()+"-"+p.getTotal());
    }

}
