package org.satorysoft.cotton.ui.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import org.apache.commons.io.FilenameUtils;
import org.satorysoft.cotton.R;
import org.satorysoft.cotton.core.gdrive.MovieFileUploadTask;
import org.satorysoft.cotton.core.gdrive.MusicFileUploadTask;
import org.satorysoft.cotton.util.Constants;
import org.satorysoft.cotton.util.FileUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by viacheslavokolitiy on 16.06.2015.
 */
public class MediaFileListDialog extends DialogFragment {

    private ArrayList<String> mSelectedItems;
    private boolean needBackupMovies = false;
    private boolean needBackupMusic = false;

    public static MediaFileListDialog newInstance(LinkedHashMap<String, String> mediaFiles) {
        ArrayList<String> fileNames = new ArrayList<>();
        ArrayList<String> filePaths = new ArrayList<>();
        Bundle bundle = new Bundle();
        for (Map.Entry<String, String> entries : mediaFiles.entrySet()) {
            String fileName = entries.getKey();
            String filePath = entries.getValue();
            fileNames.add(fileName);
            filePaths.add(filePath);
        }

        bundle.putStringArrayList(Constants.MUSIC_FILE_NAME_LIST, fileNames);
        bundle.putStringArrayList(Constants.MUSIC_FILE_PATH_LIST, filePaths);

        MediaFileListDialog musicFileListDialog = new MediaFileListDialog();
        musicFileListDialog.setArguments(bundle);

        return musicFileListDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSelectedItems = new ArrayList<>();
        Bundle arguments = getArguments();
        ArrayList<String> fileNames = arguments.getStringArrayList(Constants.MUSIC_FILE_NAME_LIST);
        final ArrayList<String> filePaths = arguments.getStringArrayList(Constants.MUSIC_FILE_PATH_LIST);

        assert fileNames != null;
        assert filePaths != null;
        String[] musicFileNames = new String[fileNames.size()];
        final String[] convertedData = fileNames.toArray(musicFileNames);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMultiChoiceItems(convertedData, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if (isChecked) {
                    // If the user checked the item, add it to the selected item
                    mSelectedItems.add(filePaths.get(which));
                } else if (mSelectedItems.contains(filePaths.get(which))) {
                    // Else, if the item is already in the array, remove it
                    mSelectedItems.remove(which);
                }
            }
        }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (mSelectedItems.size() > 0) {
                    List<String> movieExtensions = FileUtils.mediaFormats();
                    List<String> musicExtensions = FileUtils.getFileExtensionList();
                    for (String item : mSelectedItems) {
                        String extension = FilenameUtils.getExtension(item);
                        if (movieExtensions.contains(extension)) {
                            needBackupMovies = true;
                            needBackupMusic = false;
                        } else {
                            needBackupMusic = true;
                            needBackupMovies = false;
                        }
                    }
                }

                if (needBackupMovies && mSelectedItems.size() > 0) {
                    initiateBackupMovies(mSelectedItems);
                } else if (needBackupMusic && mSelectedItems.size() > 0) {
                    initiateBackupMusic(mSelectedItems);
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        return builder.create();
    }

    private void initiateBackupMovies(ArrayList<String> mSelectedItems) {
        new MovieFileUploadTask(getActivity(), mSelectedItems).execute();
    }

    private void initiateBackupMusic(ArrayList<String> mSelectedItems) {
        new MusicFileUploadTask(getActivity(), mSelectedItems).execute();
    }
}
