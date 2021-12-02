package edu.msu.steve702.ua_quality_assurance_platform.data_objects;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.Map;

public class ChecklistDataObject implements Serializable {



    @Exclude private String id;

    private String mapString;

    // Checklist number
    private Integer checklist_id;





    // Mapping between
    //<section number, <question number, [question, answer]>
    // List<Map<questionNum, question>>
    //List<Map<questionNum, answer>>
    private List<Map<Integer, String[]>> datamap1;
    private Map<Integer, Map<Integer, String[]>> dataMap;



    public ChecklistDataObject(){}

    public ChecklistDataObject(Integer id, Map<Integer, Map<Integer, String[]>> map) {
        checklist_id = id;
        dataMap = map;
//        this.datamap1 = newlist;
    }



    public void setChecklistId(int id){
        checklist_id = id;
    }

    public int getChecklistId(){return checklist_id;}

    public boolean hasKey(int key) {
        return dataMap.containsKey(key);
    }


    public String[] getQuestion(int category, int question){
        return dataMap.get(category).get(question);
    }


    public void add(int key){
        dataMap.put(key, new HashMap());
//        datamap1.add(key-1, new HashMap());
    }

    public Map<Integer, String[]> get(int key){
//        return datamap1.get(key-1);
        return dataMap.get(key);
    }

    public void put(int key, Map<Integer,String[]> newMap) {
        dataMap.put(key, newMap);
    }

    public String getId() {
        return id;
    }
    public void setId(final String id) {
        this.id = id;
    }

    public int size(){
        return dataMap.size();
    }

    public void setMapString(String str){mapString = str;}






}
