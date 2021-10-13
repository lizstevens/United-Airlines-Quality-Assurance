package edu.msu.steve702.ua_quality_assurance_platform;

import android.widget.EditText;

public class DataObject {
    long auditNumber;
    String employeeNameObj;
    String partNumberObj;
    String serialNumberObj;
    String nomenclatureObj;
    String taskObj;
    String techSpecificationsObj;
    String toolingObj;
    String shelfLifeObj;
    String traceObj;
    String reqTrainingObj;

    public DataObject() {
    }

    public long getAuditNumber() {
        return this.auditNumber;
    }

    public void setAuditNumber(final long auditNumber) {
        this.auditNumber = auditNumber;
    }

    public String getEmployeeNameObj() {
        return this.employeeNameObj;
    }

    public void setEmployeeNameObj(final String employeeNameObj) {
        this.employeeNameObj = employeeNameObj;
    }

    public String getPartNumberObj() {
        return this.partNumberObj;
    }

    public void setPartNumberObj(final String partNumberObj) {
        this.partNumberObj = partNumberObj;
    }

    public String getSerialNumberObj() {
        return this.serialNumberObj;
    }

    public void setSerialNumberObj(final String serialNumberObj) {
        this.serialNumberObj = serialNumberObj;
    }

    public String getNomenclatureObj() {
        return this.nomenclatureObj;
    }

    public void setNomenclatureObj(final String nomenclatureObj) {
        this.nomenclatureObj = nomenclatureObj;
    }

    public String getTaskObj() {
        return this.taskObj;
    }

    public void setTaskObj(final String taskObj) {
        this.taskObj = taskObj;
    }

    public String getTechSpecificationsObj() {
        return this.techSpecificationsObj;
    }

    public void setTechSpecificationsObj(final String techSpecificationsObj) {
        this.techSpecificationsObj = techSpecificationsObj;
    }

    public String getToolingObj() {
        return this.toolingObj;
    }

    public void setToolingObj(final String toolingObj) {
        this.toolingObj = toolingObj;
    }

    public String getShelfLifeObj() {
        return this.shelfLifeObj;
    }

    public void setShelfLifeObj(final String shelfLifeObj) {
        this.shelfLifeObj = shelfLifeObj;
    }

    public String getTraceObj() {
        return this.traceObj;
    }

    public void setTraceObj(final String traceObj) {
        this.traceObj = traceObj;
    }

    public String getReqTrainingObj() {
        return this.reqTrainingObj;
    }

    public void setReqTrainingObj(final String reqTrainingObj) {
        this.reqTrainingObj = reqTrainingObj;
    }

    public String getTrainingDateObj() {
        return this.trainingDateObj;
    }

    public void setTrainingDateObj(final String trainingDateObj) {
        this.trainingDateObj = trainingDateObj;
    }

    String trainingDateObj;
}
