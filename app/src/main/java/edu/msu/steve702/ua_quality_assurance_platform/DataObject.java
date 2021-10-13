package edu.msu.steve702.ua_quality_assurance_platform;

import android.widget.EditText;

import java.io.Serializable;

public class DataObject implements Serializable {
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
    String trainingDateObj;

    public DataObject() {

    }
    public DataObject(final String employeeNameObj, final String partNumberObj, final String serialNumberObj, final String nomenclatureObj, final String taskObj, final String techSpecificationsObj, final String toolingObj, final String shelfLifeObj, final String traceObj, final String reqTrainingObj, final String trainingDateObj) {
        this.employeeNameObj = employeeNameObj;
        this.partNumberObj = partNumberObj;
        this.serialNumberObj = serialNumberObj;
        this.nomenclatureObj = nomenclatureObj;
        this.taskObj = taskObj;
        this.techSpecificationsObj = techSpecificationsObj;
        this.toolingObj = toolingObj;
        this.shelfLifeObj = shelfLifeObj;
        this.traceObj = traceObj;
        this.reqTrainingObj = reqTrainingObj;
        this.trainingDateObj = trainingDateObj;
    }

    public String getEmployeeNameObj() {
        return this.employeeNameObj;
    }

    public String getPartNumberObj() {
        return this.partNumberObj;
    }

    public String getSerialNumberObj() {
        return this.serialNumberObj;
    }

    public String getNomenclatureObj() {
        return this.nomenclatureObj;
    }

    public String getTaskObj() {
        return this.taskObj;
    }

    public String getTechSpecificationsObj() {
        return this.techSpecificationsObj;
    }

    public String getToolingObj() {
        return this.toolingObj;
    }

    public String getShelfLifeObj() {
        return this.shelfLifeObj;
    }

    public String getTraceObj() {
        return this.traceObj;
    }

    public String getReqTrainingObj() {
        return this.reqTrainingObj;
    }

    public String getTrainingDateObj() {
        return this.trainingDateObj;
    }


}
