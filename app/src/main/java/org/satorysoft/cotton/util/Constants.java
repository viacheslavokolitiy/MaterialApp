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


    private Constants(){}

    public static class IntentActions {
        private IntentActions(){}
        public static final String SCAN_APPS_INTENT = "org.satorysoft.cotton.intent.scan_apps_intent";
        public static final String INTENT_REMOVE_APP = "org.satorysoft.cotton.intent.DELETE_APP";

    }
}
