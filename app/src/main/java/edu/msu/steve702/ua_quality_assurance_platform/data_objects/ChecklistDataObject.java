package edu.msu.steve702.ua_quality_assurance_platform.data_objects;

import java.io.Serializable;
import java.util.*;

public class ChecklistDataObject implements Serializable {


    // Checklist number
    private int checklist_id;

    // Mapping between
    //private Map<String, >


    public ChecklistDataObject(){

    }

    public void setId(int id){
        this.checklist_id = id;
    }

}
