package org.satorysoft.cotton.util;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by viacheslavokolitiy on 26.06.2015.
 */
public class FileUtils {
    private static final int OFFSET = 0;

    public static void zip(ArrayList<String> files, String zipFilePath){
        BufferedInputStream inputStream = null;
        try {
            ZipOutputStream outputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFilePath)));
            byte[] data = new byte[Constants.BUFFER_SIZE];

            for(String filePath : files){
                FileInputStream fileInputStream = new FileInputStream(filePath);
                inputStream = new BufferedInputStream(fileInputStream, Constants.BUFFER_SIZE);
                ZipEntry entry = new ZipEntry(filePath.substring(filePath.lastIndexOf("/") + 1));
                try {
                    outputStream.putNextEntry(entry);
                    int count;
                    while ((count = inputStream.read(data, 0, Constants.BUFFER_SIZE)) != -1){
                        outputStream.write(data, OFFSET, count);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getFileExtensionList(){
        List<String> extensionList = new ArrayList<>();
        extensionList.add("mp3");
        extensionList.add("wav");
        extensionList.add("wave");
        extensionList.add("flac");

        return extensionList;
    }

    public static List<String> mediaFormats(){
        List<String> mediaFormats = new ArrayList<>();
        mediaFormats.add("3gp");
        mediaFormats.add("mp4");
        mediaFormats.add("m4a");
        mediaFormats.add("aac");
        mediaFormats.add("ts");
        mediaFormats.add(".webm");

        return mediaFormats;
    }
}
