package edu.msu.steve702.ua_quality_assurance_platform.data_objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class AuditObject implements Serializable {

    @Exclude String id;

    private String auditNameObj;
    private String auditDateObj;
    private String locationObj;
    private String auditTitleObj;
    private String auditNumberObj;
    private String vendorNameObj;
    private String vendorNumObj;
    private String auditDescripObj;
    private String status;

    public AuditObject() {}

    public AuditObject(final String auditNameObj, final String auditDateObj, final String locationObj, final String auditTitleObj, final String auditNumberObj, final String vendorNameObj, final String vendorNumObj, final String auditDescripObj, final String status) {
        this.auditNameObj = auditNameObj;
        this.auditDateObj = auditDateObj;
        this.locationObj = locationObj;
        this.auditTitleObj = auditTitleObj;
        this.auditNumberObj = auditNumberObj;
        this.vendorNameObj = vendorNameObj;
        this.vendorNumObj = vendorNumObj;
        this.auditDescripObj = auditDescripObj;
        this.status = status;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getAuditNameObj() {
        return this.auditNameObj;
    }

    public void setAuditNameObj(final String auditNameObj) {
        this.auditNameObj = auditNameObj;
    }

    public String getAuditDateObj() {
        return this.auditDateObj;
    }

    public void setAuditDateObj(final String auditDateObj) {
        this.auditDateObj = auditDateObj;
    }

    public String getLocationObj() {
        return this.locationObj;
    }

    public void setLocationObj(final String locationObj) {
        this.locationObj = locationObj;
    }

    public String getAuditTitleObj() {
        return this.auditTitleObj;
    }

    public void setAuditTitleObj(final String auditTitleObj) {
        this.auditTitleObj = auditTitleObj;
    }

    public String getAuditNumberObj() {
        return this.auditNumberObj;
    }

    public void setAuditNumberObj(final String auditNumberObj) {
        this.auditNumberObj = auditNumberObj;
    }

    public String getVendorNameObj() {
        return this.vendorNameObj;
    }

    public void setVendorNameObj(final String vendorNameObj) {
        this.vendorNameObj = vendorNameObj;
    }

    public String getVendorNumObj() {
        return this.vendorNumObj;
    }

    public void setVendorNumObj(final String vendorNumObj) {
        this.vendorNumObj = vendorNumObj;
    }

    public String getAuditDescripObj() {
        return this.auditDescripObj;
    }

    public void setAuditDescripObj(final String auditDescripObj) {
        this.auditDescripObj = auditDescripObj;
    }

    public String getStatus() { return this.status; }

    public void setStatus(final String status) { this.status = status; }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(auditNameObj);
//        parcel.writeString(auditDateObj);
//        parcel.writeString(locationObj);
//        parcel.writeString(auditTitleObj);
//        parcel.writeString(auditNumberObj);
//        parcel.writeString(vendorNumObj);
//        parcel.writeString(auditDescripObj);
//
//
//    }
}
