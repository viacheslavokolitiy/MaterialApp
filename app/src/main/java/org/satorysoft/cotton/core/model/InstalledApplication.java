package org.satorysoft.cotton.core.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by viacheslavokolitiy on 08.06.2015.
 */
public class InstalledApplication implements Serializable {
    private String mApplicationName;
    private String mPackageName;
    private byte[] mApplicationIconBytes;
    private double mApplicationRiskRate;
    private String[] mApplicationPermissions;

    public InstalledApplication(){

    }

    public String getApplicationName() {
        return mApplicationName;
    }

    public void setApplicationName(String mApplicationName) {
        this.mApplicationName = mApplicationName;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public byte[] getApplicationIconBytes() {
        return mApplicationIconBytes;
    }

    public void setApplicationIconBytes(byte[] mApplicationIconBytes) {
        this.mApplicationIconBytes = mApplicationIconBytes;
    }

    public double getApplicationRiskRate() {
        return mApplicationRiskRate;
    }

    public void setApplicationRiskRate(double mApplicationRiskRate) {
        this.mApplicationRiskRate = mApplicationRiskRate;
    }

    public String[] getApplicationPermissions() {
        return mApplicationPermissions;
    }

    public void setApplicationPermissions(String[] mApplicationPermissions) {
        this.mApplicationPermissions = mApplicationPermissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstalledApplication)) return false;

        InstalledApplication that = (InstalledApplication) o;

        if (Double.compare(that.mApplicationRiskRate, mApplicationRiskRate) != 0) return false;
        if (!Arrays.equals(mApplicationIconBytes, that.mApplicationIconBytes)) return false;
        if (!mApplicationName.equals(that.mApplicationName)) return false;
        if (!Arrays.equals(mApplicationPermissions, that.mApplicationPermissions))
            return false;
        if (!mPackageName.equals(that.mPackageName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = mApplicationName.hashCode();
        result = 31 * result + mPackageName.hashCode();
        result = 31 * result + Arrays.hashCode(mApplicationIconBytes);
        temp = Double.doubleToLongBits(mApplicationRiskRate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + Arrays.hashCode(mApplicationPermissions);
        return result;
    }

    @Override
    public String toString() {
        return "InstalledApplication{" +
                "mApplicationName='" + mApplicationName + '\'' +
                ", mPackageName='" + mPackageName + '\'' +
                ", mApplicationIconBytes=" + Arrays.toString(mApplicationIconBytes) +
                ", mApplicationRiskRate=" + mApplicationRiskRate +
                ", mApplicationPermissions=" + Arrays.toString(mApplicationPermissions) +
                '}';
    }
}
