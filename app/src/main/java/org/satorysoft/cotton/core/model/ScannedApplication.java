package org.satorysoft.cotton.core.model;

import java.io.Serializable;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class ScannedApplication implements Serializable {
    private InstalledApplication mInstalledApplication;
    private long mScanDate;

    public ScannedApplication(){

    }

    public InstalledApplication getInstalledApplication() {
        return mInstalledApplication;
    }

    public long getScanDate() {
        return mScanDate;
    }

    public void setInstalledApplication(InstalledApplication mInstalledApplication) {
        this.mInstalledApplication = mInstalledApplication;
    }

    public void setScanDate(long mScanDate) {
        this.mScanDate = mScanDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScannedApplication)) return false;

        ScannedApplication that = (ScannedApplication) o;

        if (mScanDate != that.mScanDate) return false;
        if (!mInstalledApplication.equals(that.mInstalledApplication)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mInstalledApplication.hashCode();
        result = 31 * result + (int) (mScanDate ^ (mScanDate >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ScannedApplication{" +
                "mInstalledApplication=" + mInstalledApplication +
                ", mScanDate=" + mScanDate +
                '}';
    }
}
