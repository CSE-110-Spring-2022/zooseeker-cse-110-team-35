package edu.ucsd.cse110.zooseeker_team35;

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
    String getId() {
        return id;
    }

    boolean getIsAdded() {
        return isAdded;
    }

    void setIsAdded(boolean isAdded) {
        this.isAdded = isAdded;
    }
}
