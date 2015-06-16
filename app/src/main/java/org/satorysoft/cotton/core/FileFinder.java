package org.satorysoft.cotton.core;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by viacheslavokolitiy on 16.06.2015.
 */
public class FileFinder {
    private static final String TAG = FileFinder.class.getSimpleName();
    private LinkedHashMap<String, String> foundedFile;

    public FileFinder(){
        this.foundedFile = new LinkedHashMap<>();
    }

    public void findFilesWithExtension(String extension){
        String rootSD = Environment.getExternalStorageDirectory().toString();

        File folderFile = new File(rootSD);
        LinkedHashMap<String,String> foundedMediaFiles = getAllFilesOfDir(folderFile, extension);

        if(foundedMediaFiles.size() > 0){
            EventBus.getDefault().post(new MusicFileFoundEvent(foundedMediaFiles));
        } else {
            EventBus.getDefault().post(new NoMusicFileFoundEvent());
        }
    }

    private LinkedHashMap<String, String> getAllFilesOfDir(File directory, String fileExtension) {
        Log.d(TAG, "Directory: " + directory.getAbsolutePath() + "\n");

        final File[] files = directory.listFiles();

        if ( files != null ) {
            for ( File file : files ) {
                if ( file != null ) {
                    if ( file.isDirectory() ) {
                        getAllFilesOfDir(file, fileExtension);
                    }
                    else {
                        if(file.getName().endsWith(fileExtension)){
                            if(!foundedFile.containsKey(file.getName())) {
                                foundedFile.put(file.getName(), file.getAbsolutePath());
                            }
                        }
                    }
                }
            }
        }

        return foundedFile;
    }

    public static class NoMusicFileFoundEvent {
    }

    public static class MusicFileFoundEvent {

        private final LinkedHashMap<String, String> foundedMediaFiles;

        public MusicFileFoundEvent(LinkedHashMap<String, String> foundedMediaFiles) {
            this.foundedMediaFiles = foundedMediaFiles;
        }

        public LinkedHashMap<String, String> getFoundedMediaFiles() {
            return foundedMediaFiles;
        }
    }
}
