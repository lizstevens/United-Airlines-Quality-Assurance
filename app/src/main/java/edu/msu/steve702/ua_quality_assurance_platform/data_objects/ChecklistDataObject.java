package edu.msu.steve702.ua_quality_assurance_platform.data_objects;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.HashMap;
//import java.util.Map;

public class ChecklistDataObject implements Serializable {


    // Checklist number
    private int checklist_id;

    // Mapping between

    transient private HashMap<Integer, HashMap<Integer, Pair<String, Boolean>>> dataMap;


    public ChecklistDataObject(){

        dataMap = new HashMap<>();

    }

    public void setId(int id){
        checklist_id = id;
    }

    public int getId(){return checklist_id;}

    public boolean hasKey(int key) {return dataMap.containsKey(key);}


    public Pair<String, Boolean> getQuestion(int category, int question){
        return dataMap.get(category).get(question);
    }








    public void add(int key){dataMap.put(key, new HashMap()); }

    public HashMap<Integer, Pair<String, Boolean>> get(int key){
        return dataMap.get(key);
    }




}
