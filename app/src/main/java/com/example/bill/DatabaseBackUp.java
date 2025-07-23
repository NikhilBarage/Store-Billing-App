package com.example.bill;

import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseBackUp {
    private client_page client;
    public boolean success = false;

    public DatabaseBackUp(client_page client){
        this.client = client;
    }

    public void performBackUp(final ProductDataSource db){
        Date today = new Date();
        SimpleDateFormat format_tanggal = new SimpleDateFormat("ddMMyy");
        SimpleDateFormat format_waktu = new SimpleDateFormat("hhmmss");
        String tanggal = format_tanggal.format(today);
        String waktu = format_waktu.format(today);

        Permission.verifyStoragePermission(client);

        File folder = new File(Environment.getExternalStorageDirectory()+ "/Documents/"+ client.getResources().getString(R.string.app_name));

        try{
            success = true;
            if (!folder.exists())
                success = folder.mkdirs();

            if (success){
               db.backUp(folder+"/"+"backup_"+tanggal+waktu+".db");
                Toast.makeText(client, "Able to Data BackUp...", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(client, "Unable to Create Directory...", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }


    }
}
