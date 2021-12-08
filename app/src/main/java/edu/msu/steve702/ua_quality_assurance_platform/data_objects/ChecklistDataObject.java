package edu.msu.steve702.ua_quality_assurance_platform.data_objects;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ChecklistDataObject Class
 * Serializable Object that represents the data for the checklist within an Audit.
 * Used for database management.
 */
public class ChecklistDataObject implements Serializable {

    @Exclude
    private String id;

    /** Data Variables **/
    private String mapString;
    private Integer checklist_id;
    private List<Map<Integer, String[]>> datamap1;
    private Map<Integer, Map<Integer, String[]>> dataMap;

    /** Empty Constructor **/
    public ChecklistDataObject() {
    }

    /** Constructor **/
    public ChecklistDataObject(final Integer id, final Map<Integer, Map<Integer, String[]>> map) {
        this.checklist_id = id;
        this.dataMap = map;
    }

    /** Respective Getters and Setters **/

    public int getChecklistId() { return this.checklist_id; }

    public void setChecklistId(final int id) { this.checklist_id = id; }

    public boolean hasKey(final int key) { return this.dataMap.containsKey(key); }

    public String[] getQuestion(final int category, final int question) {
        return this.dataMap.get(category).get(question);
    }

    public void add(final int key) { this.dataMap.put(key, new HashMap()); }

    public Map<Integer, String[]> get(final int key) { return this.dataMap.get(key); }

    public void put(final int key, final Map<Integer, String[]> newMap) {
        this.dataMap.put(key, newMap);
    }

    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    public int size() { return this.dataMap.size(); }

    public void setMapString(final String str) { this.mapString = str; }

}
