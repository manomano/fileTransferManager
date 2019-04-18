package com.example.demo;
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
    public String fileProgress(String ragaca) throws Exception {
        FireSending fSending = new FireSending(this);
        fSending.run();
        return ""+FileDownloader.FilesToDownload+"-"+FileDownloader.FileCounter;
        //return FileDownloader.FileCounter;
    }


    public void updateInfo (){
            this.template.convertAndSend("/download/copyingFromServer", ""+FileDownloader.FilesToDownload+"-"+FileDownloader.FileCounter);
    }

}
