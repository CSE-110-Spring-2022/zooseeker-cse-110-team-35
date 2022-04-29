package edu.ucsd.cse110.zooseeker_team35;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity(tableName = "sample_node_info.json")
public class Vertex {

    @PrimaryKey @NonNull
    String id;

    @NonNull
    String kind;
    String name;
    ArrayList<String> tags;
    boolean isAdded;

    public Vertex(@NonNull String id, String kind, String name, ArrayList<String> tags) {
        this.id = id;
        this.kind = kind;
        this.name = name;
        this.tags = tags;
        this.isAdded = false;
    }

    String getId() {
        return id;
    }

    String getKind() {
        return kind;
    }

    String getName() {
        return name;
    }

    ArrayList<String> getTags() {
        return tags;
    }

    boolean getIsAdded() {
        return isAdded;
    }

    void setId(String id) {
        this.id = id;
    }

    void setKind(String kind) {
        this.kind = kind;
    }

    void setName(String name) {
        this.name = name;
    }

    void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    void setIsAdded(boolean isAdded) {
        this.isAdded = isAdded;
    }

    public static List<Vertex> loadJSON(Context context, String path) {
        try{
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Vertex>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
