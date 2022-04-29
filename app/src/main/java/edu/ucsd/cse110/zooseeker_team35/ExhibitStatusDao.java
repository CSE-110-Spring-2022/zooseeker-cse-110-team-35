package edu.ucsd.cse110.zooseeker_team35;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExhibitStatusDao {
    @Insert
    long insert(ExhibitStatus exhibitStatus);

    @Insert
    List<Long> insertAll(List<ExhibitStatus> exhibitStatus);

    @Update
    int update(ExhibitStatus exhibitStatus);

    @Delete
    int delete(ExhibitStatus exhibitStatus);
}
