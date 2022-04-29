package edu.ucsd.cse110.zooseeker_team35;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZooInfoProvider{

    private static Map<String, Vertex> idVertexMap;
    private static List<Vertex> vertexes;
    private static List<Vertex> exhibits;

    public static Vertex getVertexWithId(String id) {
        return idVertexMap.get(id);
    }

    public static List<Vertex> getExhibits() {
        return exhibits;
    }

    public static void setExhibits(List<Vertex> exhibits) {
        ZooInfoProvider.exhibits = exhibits;
    }

    public static List<Vertex> getVertexes() {
        return vertexes;
    }

    public static void setVertexes(List<Vertex> vertexes) {
        ZooInfoProvider.vertexes = vertexes;
    }

    public static List<Vertex> getSelectedExhibits() {
        //TODO: get the list of ExhibitStatus with isAdded=true
        List<String> selectedIDs = new ArrayList<>();
        List<Vertex> selectedExhibits = new ArrayList<>();
        for (String id : selectedIDs){
            selectedExhibits.add(getVertexWithId(id));
        }
        return selectedExhibits;
    }

}
