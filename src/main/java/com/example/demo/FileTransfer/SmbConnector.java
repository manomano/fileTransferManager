package com.example.demo.FileTransfer;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import org.springframework.stereotype.Service;

@Service
public class SmbConnector {


    public void testConnection() {
        try {
            String url = "smb://10.10.82.100/backup_wmsstor/";

            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "gisbackup", "Baegu4an");
            SmbFile dir = new SmbFile(url, auth);
            for (SmbFile f : dir.listFiles()) {
                System.out.println(f.getName());
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
