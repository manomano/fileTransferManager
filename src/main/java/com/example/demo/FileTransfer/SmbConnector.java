package com.example.demo.FileTransfer;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmbConnector {

    @Value("${source_smb_address}")
    private String url;
    @Value("${source_smb_user}")
    private String user;
    @Value("${source_smb_pass}")
    private String pass;

    NtlmPasswordAuthentication auth;

   /* public SmbConnector(){
        this.auth = new NtlmPasswordAuthentication(null, this.user, this.pass);
    }*/

    public NtlmPasswordAuthentication authorize(String user,String pass)throws Exception{
        return new NtlmPasswordAuthentication(null, user, pass);
    }


    public NtlmPasswordAuthentication authorize()throws Exception{
        return new NtlmPasswordAuthentication(null, this.user, this.pass);
    }




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

    public SmbFile getMainDirectory()throws Exception{
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, this.user, this.pass);
        SmbFile dir = new SmbFile(this.url, auth);
        return dir;
    }
}
