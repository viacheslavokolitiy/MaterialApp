package org.satorysoft.cotton.core.model;

import java.io.Serializable;

/**
 * Created by viacheslavokolitiy on 09.06.2015.
 */
public class SelectedApplication implements Serializable {
    private byte[] mIcon;
    private String mTitle;
    private String[] mPermissions;

    public byte[] getIcon() {
        return mIcon;
    }

    public String getTitle() {
        return mTitle;
    }

    public String[] getPermissions() {
        return mPermissions;
    }

    public void setIcon(byte[] mIcon) {
        this.mIcon = mIcon;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setPermissions(String[] mPermissions) {
        this.mPermissions = mPermissions;
    }
}
