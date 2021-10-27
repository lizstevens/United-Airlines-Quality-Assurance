package edu.msu.steve702.ua_quality_assurance_platform;

public class AuditObject {
    private String auditNameObj;
    private String auditDateObj;
    private String vendorNameObj;
    private String vendorNumObj;
    private String auditDescripObj;

    public AuditObject() {}

    public AuditObject(final String auditNameObj, final String auditDateObj, final String vendorNameObj, final String vendorNumObj, final String auditDescripObj) {
        this.auditNameObj = auditNameObj;
        this.auditDateObj = auditDateObj;
        this.vendorNameObj = vendorNameObj;
        this.vendorNumObj = vendorNumObj;
        this.auditDescripObj = auditDescripObj;
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
}
