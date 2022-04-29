package edu.ucsd.cse110.zooseeker_team35;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Vertex {
    public static enum Kind {
        // The SerializedName annotation tells GSON how to convert
        // from the strings in our JSON to this Enum.
        @SerializedName("gate") GATE,
        @SerializedName("exhibit") EXHIBIT,
        @SerializedName("intersection") INTERSECTION
    }

    public String id;
    public Kind kind;
    public String name;
    public List<String> tags;
}
