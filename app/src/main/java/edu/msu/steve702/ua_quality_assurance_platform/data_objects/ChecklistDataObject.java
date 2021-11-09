package edu.msu.steve702.ua_quality_assurance_platform.data_objects;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.*;

public class ChecklistDataObject implements Serializable {


    // Checklist number
    private int checklist_id;

    // Mapping between
    private Map<Integer, Map<Integer, Pair<String, Boolean>>> dataMap;


    public ChecklistDataObject(){

        dataMap = new HashMap<>();

    }

    public void setId(int id){
        checklist_id = id;
    }

    public boolean hasKey(int key) {return dataMap.containsKey(key);}








    public void add(int key){dataMap.put(key, new HashMap()); }

    public Map<Integer, Pair<String, Boolean>> get(int key){
        return dataMap.get(key);
    }




}
