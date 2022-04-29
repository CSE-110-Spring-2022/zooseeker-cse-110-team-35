package edu.ucsd.cse110.zooseeker_team35;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VertexDao {

    @Insert
    long insert(Vertex vertex);

    @Insert
    List<Long> insertAll(List<Vertex> vertex);

    @Update
    int update(Vertex vertex);

    @Query("SELECT * FROM `sample_node_info.json` WHERE `id`=:id")
    Vertex get(String id);

    @Query("SELECT * FROM `sample_node_info.json`")
    List<Vertex> getAll();

    @Query("SELECT * FROM `sample_node_info.json` WHERE `kind`=:kind")
    List<Vertex> getAllOfType(String kind);

    @Delete
    int delete(Vertex vertex);

}
