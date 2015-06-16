package org.satorysoft.cotton.core.model;

/**
 * Created by viacheslavokolitiy on 16.06.2015.
 */
public class CallLogData {
    private String id;
    private String phoneNumber;
    private String contactName;

    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
