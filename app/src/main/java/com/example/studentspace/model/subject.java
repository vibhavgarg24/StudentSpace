package com.example.studentspace.model;

public class subject {

    private int id;
    private String name;
    private int present = 0;
    private int absent = 0;
    private int criteria = 0;
    private int toPresent = 0;
    private int toAbsent = 0;
    private int scriteria = 0;

    //  Constructors
    public subject() {
    }

    //    Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAttendance() {
        return (present) + " / " + (absent + present);
    }

    public int getPresent() {
        return present;
    }

    public int getAbsent() {
        return absent;
    }

    public int getProgress() {
        if ((present + absent) == 0)
            return 0;
        return (int) ((float) present / (present + absent) * 100);
    }

    public int getCriteria() {
        return criteria;
    }

    public int getToPresent() {
        float x = (float) ( ( ((absent+present)*criteria) - (100*present) ) / ((float) (100-criteria)) );
        if (x - ((int) x ) == 0)
            return (int) x;
        return (int) x + 1;
    }

    public int getToAbsent() {
        float x = (float) ( ( (present*100) - (criteria*(absent+present)) ) / ((float) (criteria)) );
        if (x - ((int) x ) == 0)
            return (int) x;
        return (int) x;
    }

    public int getScriteria() {
        return scriteria;
    }

    //    Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public void setCriteria(int criteria) {
        this.criteria = criteria;
    }

    public void setScriteria(int scriteria) {
        this.scriteria = scriteria;
    }
}
