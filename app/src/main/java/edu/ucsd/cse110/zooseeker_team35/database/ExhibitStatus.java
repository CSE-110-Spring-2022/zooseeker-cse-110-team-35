package edu.ucsd.cse110.zooseeker_team35.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exhibitStatuses")
public class ExhibitStatus {

    @PrimaryKey
    @NonNull String id;

    boolean isAdded;

    public ExhibitStatus(@NonNull String id, boolean isAdded) {
        this.id = id;
        this.isAdded = isAdded;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public boolean getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(boolean isAdded) {
        this.isAdded = isAdded;
    }
}
