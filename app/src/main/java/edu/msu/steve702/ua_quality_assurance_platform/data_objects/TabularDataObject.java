package edu.msu.steve702.ua_quality_assurance_platform.data_objects;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class TabularDataObject implements Serializable {

    @Exclude
    private String id;

    private String tdPartNumObj;
    private String tdManufObj;
    private String tdAtaObj;
    private String tdRevLevelObj;
    private String tdRevDateObj;
    private String tdCommentsObj;


    public TabularDataObject() {

    }

    public TabularDataObject(final String tdPartNumObj, final String tdManufObj, final String tdAtaObj, final String tdRevLevelObj, final String tdRevDateObj, final String tdCommentsObj) {
        this.tdPartNumObj = tdPartNumObj;
        this.tdManufObj = tdManufObj;
        this.tdAtaObj = tdAtaObj;
        this.tdRevLevelObj = tdRevLevelObj;
        this.tdRevDateObj = tdRevDateObj;
        this.tdCommentsObj = tdCommentsObj;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTdPartNumObj() {
        return this.tdPartNumObj;
    }

    public void setTdPartNumObj(final String tdPartNumObj) {
        this.tdPartNumObj = tdPartNumObj;
    }

    public String getTdManufObj() {
        return this.tdManufObj;
    }

    public void setTdManufObj(final String tdManufObj) {
        this.tdManufObj = tdManufObj;
    }

    public String getTdAtaObj() {
        return this.tdAtaObj;
    }

    public void setTdAtaObj(final String tdAtaObj) {
        this.tdAtaObj = tdAtaObj;
    }

    public String getTdRevLevelObj() {
        return this.tdRevLevelObj;
    }

    public void setTdRevLevelObj(final String tdRevLevelObj) {
        this.tdRevLevelObj = tdRevLevelObj;
    }

    public String getTdRevDateObj() {
        return this.tdRevDateObj;
    }

    public void setTdRevDateObj(final String tdRevDateObj) {
        this.tdRevDateObj = tdRevDateObj;
    }

    public String getTdCommentsObj() {
        return this.tdCommentsObj;
    }

    public void setTdCommentsObj(final String tdCommentsObj) {
        this.tdCommentsObj = tdCommentsObj;
    }
}
