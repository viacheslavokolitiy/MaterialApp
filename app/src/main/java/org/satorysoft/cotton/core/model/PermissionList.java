package org.satorysoft.cotton.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class PermissionList {
    public List<String> getHighRiskPermissions(){
        List<String> permissions = new ArrayList<>();
        permissions.add("android.permission.READ_SMS");
        permissions.add("android.permission.WRITE_SMS");
        permissions.add("android.permission.ACCESS_COARSE_LOCATION");
        permissions.add("android.permission.ACCESS_FINE_LOCATION");
        permissions.add("android.permission.ACCESS_MOCK_LOCATION");
        permissions.add("android.permission.AUTHENTICATE_ACCOUNTS");
        permissions.add("android.permission.BLUETOOTH");
        permissions.add("android.permission.BLUETOOTH_ADMIN");
        permissions.add("android.permission.CALL_PHONE");
        permissions.add("android.permission.CAMERA");
        permissions.add("android.permission.CHANGE_CONFIGURATION");
        permissions.add("android.permission.CHANGE_NETWORK_STATE");
        permissions.add("android.permission.CHANGE_WIFI_MULTICAST_STATE");
        permissions.add("android.permission.CHANGE_WIFI_STATE");
        permissions.add("android.permission.CLEAR_APP_CACHE");
        permissions.add("android.permission.DUMP");
        permissions.add("android.permission.GET_TASKS");
        permissions.add("android.permission.INTERNET");
        permissions.add("android.permission.MANAGE_ACCOUNTS");
        permissions.add("android.permission.MODIFY_AUDIO_SETTINGS");
        permissions.add("android.permission.MODIFY_PHONE_STATE");
        permissions.add("android.permission.MOUNT_FORMAT_FILESYSTEMS");
        permissions.add("android.permission.MOUNT_UNMOUNT_FILESYSTEMS");
        permissions.add("android.permission.PERSISTENT_ACTIVITY");
        permissions.add("android.permission.PROCESS_OUTGOING_CALLS");
        permissions.add("android.permission.READ_CALENDAR");
        permissions.add("android.permission.READ_CONTACTS");
        permissions.add("android.permission.READ_LOGS");
        permissions.add("android.permission.READ_OWNER_DATA");
        permissions.add("android.permission.READ_PHONE_STATE");
        permissions.add("android.permission.READ_USER_DICTIONARY");
        permissions.add("android.permission.RECEIVE_MMS");
        permissions.add("android.permission.RECEIVE_SMS");
        permissions.add("android.permission.RECEIVE_WAP_PUSH");
        permissions.add("android.permission.RECORD_AUDIO");
        permissions.add("android.permission.REORDER_TASKS");
        permissions.add("android.permission.SEND_SMS");
        permissions.add("android.permission.SET_ALWAYS_FINISH");
        permissions.add("android.permission.SET_ANIMATION_SCALE");
        permissions.add("android.permission.SET_DEBUG_APP");
        permissions.add("android.permission.SET_PROCESS_LIMIT");
        permissions.add("android.permission.SET_TIME_ZONE");
        permissions.add("android.permission.SIGNAL_PERSISTENT_PROCESSES");
        permissions.add("android.permission.SUBSCRIBED_FEEDS_WRITE");
        permissions.add("android.permission.SYSTEM_ALERT_WINDOW");
        permissions.add("android.permission.USE_CREDENTIALS");
        permissions.add("android.permission.WAKE_LOCK");
        permissions.add("android.permission.WRITE_APN_SETTINGS");
        permissions.add("android.permission.WRITE_CALENDAR");
        permissions.add("android.permission.WRITE_CONTACTS");
        permissions.add("android.permission.WRITE_EXTERNAL_STORAGE");
        permissions.add("android.permission.WRITE_OWNER_DATA");
        permissions.add("android.permission.WRITE_SETTINGS");
        permissions.add("android.permission.WRITE_SYNC_SETTINGS");
        permissions.add("com.android.browser.permission.READ_HISTORY_BOOKMARKS");
        permissions.add("com.android.browser.permission.WRITE_HISTORY_BOOKMARKS");

        return permissions;
    }
}
