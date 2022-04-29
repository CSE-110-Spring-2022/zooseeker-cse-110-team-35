package edu.ucsd.cse110.zooseeker_team35;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/*
Converters class taken from https://stackoverflow.com/questions/44986626/
android-room-database-how-to-handle-arraylist-in-an-entity
*/
public class Converters {
    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArray(ArrayList<String> array) {
        Gson gson = new Gson();
        String json = gson.toJson(array);
        return json;
    }
}
