package edu.ucsd.cse110.zooseeker_team35;

public class Exhibit {
    String id;
    String kind;
    String name;
    String[] tags;
    boolean isAdded;

    public Exhibit(String id, String kind, String name, String[] tags, boolean isAdded) {
        this.id = id;
        this.kind = kind;
        this.name = name;
        this.tags = tags;
        this.isAdded = isAdded;
    }

    boolean getIsAdded() {
        return isAdded;
    }

    void setIsAdded(boolean isAdded) {
        this.isAdded = isAdded;
    }
}
