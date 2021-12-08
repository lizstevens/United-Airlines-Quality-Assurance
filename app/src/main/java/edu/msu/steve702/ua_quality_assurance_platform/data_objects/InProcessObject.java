package edu.msu.steve702.ua_quality_assurance_platform.data_objects;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

/**
 * InProcessObject Class
 * Serializable Object that represents the data for an in process table within an Audit.
 * Used for database management.
 */
public class InProcessObject implements Serializable {

    @Exclude
    private String id;

    /** Data Variables **/
    private String employeeNameObj;
    private String partNumberObj;
    private String serialNumberObj;
    private String nomenclatureObj;
    private String taskObj;
    private String techSpecificationsObj;
    private String toolingObj;
    private String shelfLifeObj;
    private String traceObj;
    private String reqTrainingObj;
    private String trainingDateObj;

    /** Empty Constructor **/
    public InProcessObject() {
    }

    /** Constructor **/
    public InProcessObject(final String employeeNameObj, final String partNumberObj,
                           final String serialNumberObj, final String nomenclatureObj,
                           final String taskObj, final String techSpecificationsObj,
                           final String toolingObj, final String shelfLifeObj,
                           final String traceObj, final String reqTrainingObj,
                           final String trainingDateObj) {
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

    /** Respective Getters and Setters **/

    public String getId() { return this.id; }

    public void setId(final String id) { this.id = id; }

    public String getEmployeeNameObj() { return this.employeeNameObj; }

    public String getPartNumberObj() {
        return this.partNumberObj;
    }

    public String getSerialNumberObj() { return this.serialNumberObj; }

    public String getNomenclatureObj() { return this.nomenclatureObj; }

    public String getTaskObj() { return this.taskObj; }

    public String getTechSpecificationsObj() { return this.techSpecificationsObj; }

    public String getToolingObj() { return this.toolingObj; }

    public String getShelfLifeObj() { return this.shelfLifeObj; }

    public String getTraceObj() { return this.traceObj; }

    public String getReqTrainingObj() { return this.reqTrainingObj; }

    public String getTrainingDateObj() { return this.trainingDateObj; }

}
