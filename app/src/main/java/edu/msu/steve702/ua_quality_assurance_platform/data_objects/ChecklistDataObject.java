package edu.msu.steve702.ua_quality_assurance_platform.data_objects;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
//import java.util.Map;

public class ChecklistDataObject implements Serializable {



    @Exclude private String id;

    // Checklist number
    private Integer checklist_id;

    // Mapping between
    //<section number, <question number, [question, answer]>
    private Map<Integer, Map<Integer, String[]>> dataMap;

    public ChecklistDataObject(){}

    public ChecklistDataObject(Integer id, Map<Integer, Map<Integer, String[]>> map) {
        this.checklist_id = id;
        this.dataMap = map;
    }



    public void setChecklistId(int id){
        checklist_id = id;
    }

    public int getChecklistId(){return checklist_id;}

    public boolean hasKey(int key) {return dataMap.containsKey(key);}


    public String[] getQuestion(int category, int question){
        return dataMap.get(category).get(question);
    }


    public void add(int key){dataMap.put(key, new HashMap()); }

    public Map<Integer, String[]> get(int key){
        return dataMap.get(key);
    }




}
