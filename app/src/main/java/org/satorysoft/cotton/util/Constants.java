package org.satorysoft.cotton.util;

import android.content.IntentFilter;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public final class Constants {
    public static final String SCAN_ON_FIRST_RUN_DONE = "scan_on_first_run_done";
    public static final int STATUS_RUNNING = 100500;
    public static final int STATUS_FINISHED = 100501;

    public static final String RECEIVER = "app.result.receiver";
    public static final String RECEIVER_DATA = RECEIVER + ".data";

    public static final int SERVICE_DELAY = 6000;
    public static final String SCAN_RESULT_PROGRESS = "scan_result_progress";
    public static final int SCAN_FINISHED = 100;
    public static final int COMPRESS_QUALITY = 90;
    public static final int OFFSET = 0;
    public static final double HIGH_RISK = 0.7;
    public static final double MODERATE_RISK = 0.5;
    public static final double MODERATE_RISK_LOWER = 0.45;
    public static final double LOW_RISK = 0.1;
    public static final String SCANNED_APPLICATION = "scanned_application";
    public static final String ADDED_PACKAGE_NAME = "added_package_name";
    public static final String GOOGLE_AUTH_VALID = "google_auth_valid";
    public static final String APPFOLDER_DRIVE_ID = "appfolder_drive_id";
    public static final int PHOTO_GRID_WIDTH = 180;
    public static final int PHOTO_GRID_HEIGHT = 180;
    public static final int IMAGE_VIEW_PADDING_TOP = 1;
    public static final int IMAGE_VIEW_PADDING_LEFT = 8;
    public static final int IMAGE_VIEW_PADDING_RIGHT = 18;
    public static final int IMAGE_VIEW_PADDING_BOTTOM = 1;
    public static final int REQUIRED_WIDTH = 180;
    public static final int REQUIRED_HEIGHT = 180;
    public static final int BUFFER_SIZE = 1024;
    public static final int IMAGE_NEW_WIDTH = 96;
    public static final int IMAGE_NEW_HEIGHT = 96;
    public static final int LATCH_COUNT = 1;
    public static final String PHOTO_FOLDER_ID = "photo_folder_id";
    public static final String CALL_LOG_FOLDER_ID = "call_log_folder_id";
    public static final String MUSIC_FILE_NAME_LIST = "music_file_name_list";
    public static final String MUSIC_FILE_PATH_LIST = "music_file_path_list";
    public static final String MUSIC_FOLDER_DRIVE_ID = "music_folder_id";


    private Constants(){}

    public static class IntentActions {
        public static final String ACTION_UPLOAD_PHOTOS = "org.satorysoft.cotton.intent.upload_photos_to_google_drive";

        private IntentActions(){}
        public static final String SCAN_APPS_INTENT = "org.satorysoft.cotton.intent.scan_apps_intent";
        public static final String INTENT_REMOVE_APP = "org.satorysoft.cotton.intent.DELETE_APP";
        public static final String INTENT_SCAN_APPS = "org.satorysoft.cotton.intent.scan_new_app";

    }

    public class IntentExtras {
        public static final String PHOTOS_FOR_BACKUP_EXTRA = "photos_for_backup";

        private IntentExtras(){}
    }
}
