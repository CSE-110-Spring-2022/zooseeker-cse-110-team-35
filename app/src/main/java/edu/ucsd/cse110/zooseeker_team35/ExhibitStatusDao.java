package edu.ucsd.cse110.zooseeker_team35;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExhibitStatusDao {

    @Insert
    long insert(ExhibitStatus exhibitStatus);

    @Insert
    List<Long> insertAll(List<ExhibitStatus> exhibitStatus);

    //Queries all elements in table
    @Query("SELECT * FROM exhibitStatuses")
    List<ExhibitStatus> getAll();

    //Queries element based off of its id
    @Query("SELECT * FROM exhibitStatuses WHERE id=:id")
    ExhibitStatus get(String id);

    //Returns all elements that have been added to the list or not added to the list
    @Query("SELECT * FROM exhibitStatuses WHERE isAdded=:isAdded")
    List<ExhibitStatus> getAdded(boolean isAdded);

    @Update
    int update(ExhibitStatus exhibitStatus);

    @Delete
    int delete(ExhibitStatus exhibitStatus);
}
