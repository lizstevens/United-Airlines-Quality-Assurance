package edu.msu.steve702.ua_quality_assurance_platform.data_objects;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.List;

public class TrainingTableDataObject implements Serializable {
    @Exclude
    String id;

    private List<String> row1;
    private List<String> row2;
    private List<String> row3;
    private List<String> row4;
    private List<String> row5;
    private List<String> row6;
    private List<String> row7;
    private List<String> row8;
    private List<String> row9;
    private List<String> row10;


    public TrainingTableDataObject() {}

    public TrainingTableDataObject(List<String> row1,
                              List<String> row2,
                              List<String> row3,
                              List<String> row4,
                              List<String> row5,
                              List<String> row6,
                              List<String> row7,
                              List<String> row8,
                              List<String> row9,
                              List<String> row10) {
        this.row1 = row1;
        this.row2 = row2;
        this.row3 = row3;
        this.row4 = row4;
        this.row5 = row5;
        this.row6 = row6;
        this.row7 = row7;
        this.row8 = row8;
        this.row9 = row9;
        this.row10 = row10;
    }

    public String getId() {
        return this.id;
    }
    public void setId(final String id) {
        this.id = id;
    }

    public List<String> getRow1() {return this.row1; }
    public void setRow1(final List<String> row1) { this.row1 = row1; }

    public List<String> getRow2() {
        return this.row2;
    }

    public void setRow2(final List<String> row2) {
        this.row2 = row2;
    }

    public List<String> getRow3() {
        return this.row3;
    }

    public void setRow3(final List<String> row3) {
        this.row3 = row3;
    }

    public List<String> getRow4() {
        return this.row4;
    }

    public void setRow4(final List<String> row4) {
        this.row4 = row4;
    }

    public List<String> getRow5() {
        return this.row5;
    }

    public void setRow5(final List<String> row5) {
        this.row5 = row5;
    }

    public List<String> getRow6() {
        return this.row6;
    }

    public void setRow6(final List<String> row6) {
        this.row6 = row6;
    }

    public List<String> getRow7() {
        return this.row7;
    }

    public void setRow7(final List<String> row7) {
        this.row7 = row7;
    }

    public List<String> getRow8() {
        return this.row8;
    }

    public void setRow8(final List<String> row8) {
        this.row8 = row8;
    }

    public List<String> getRow9() {
        return this.row9;
    }

    public void setRow9(final List<String> row9) {
        this.row9 = row9;
    }

    public List<String> getRow10() {
        return this.row10;
    }

    public void setRow10(final List<String> row10) {
        this.row10 = row10;
    }
}
