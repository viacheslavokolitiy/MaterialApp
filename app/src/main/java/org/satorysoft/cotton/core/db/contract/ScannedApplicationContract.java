package org.satorysoft.cotton.core.db.contract;

import android.net.Uri;

import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.ContentUri;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public interface ScannedApplicationContract extends ProviGenBaseContract {
    @Column(Column.Type.TEXT)
    public static final String APPLICATION_NAME = "application_name";
    @Column(Column.Type.TEXT)
    public static final String PACKAGE_NAME = "package_name";
    @Column(Column.Type.BLOB)
    public static final String APPLICATION_ICON = "application_icon";
    @Column(Column.Type.TEXT)
    public static final String APPLICATION_RISK_RATE = "application_risk_rate";
    @Column(Column.Type.TEXT)
    public static final String APPLICATION_PERMISSIONS = "application_permissions";
    @Column(Column.Type.INTEGER)
    public static final String SCAN_DATE = "scan_date";

    @ContentUri
    public static final Uri CONTENT_URI = Uri.parse("content://org.satorysoft.cotton/scannedApplications");
}
