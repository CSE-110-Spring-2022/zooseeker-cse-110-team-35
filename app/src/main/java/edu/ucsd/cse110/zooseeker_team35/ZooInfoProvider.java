package edu.ucsd.cse110.zooseeker_team35;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ZooInfoProvider{
    private static ExhibitStatusDao dao;

    private static Map<String, ZooData.VertexInfo> idVertexMap;
    private static Map<String, ZooData.EdgeInfo> idEdgeMap;
    private static List<ZooData.VertexInfo> vertexes;
    private static List<ZooData.VertexInfo> exhibits;

    public static void setIdVertexMap(Map<String, ZooData.VertexInfo> idVertexMap) {
        ZooInfoProvider.idVertexMap = idVertexMap;
        List<ZooData.VertexInfo> vertexes = new LinkedList<ZooData.VertexInfo>();
        List<ZooData.VertexInfo> exhibits = new LinkedList<ZooData.VertexInfo>();
        for (ZooData.VertexInfo vertex : idVertexMap.values()){
            vertexes.add(vertex);
            if (vertex.kind == ZooData.VertexInfo.Kind.EXHIBIT){
                exhibits.add(vertex);
            }
        }
        ZooInfoProvider.setVertexes(vertexes);
        ZooInfoProvider.setExhibits(exhibits);
    }
    public static void setIdEdgeMap(Map<String, ZooData.EdgeInfo> idEdgeMap) {
        ZooInfoProvider.idEdgeMap = idEdgeMap;
    }

    public static ZooData.VertexInfo getVertexWithId(String id) {
        return idVertexMap.get(id);
    }

    public static List<ZooData.VertexInfo> getExhibits() {
        return exhibits;
    }

    public static void setExhibits(List<ZooData.VertexInfo> exhibits) {
        ZooInfoProvider.exhibits = exhibits;
    }

    public static List<ZooData.VertexInfo> getVertexes() {
        return vertexes;
    }

    public static void setVertexes(List<ZooData.VertexInfo> vertexes) {
        ZooInfoProvider.vertexes = vertexes;
    }

    public static List<ZooData.VertexInfo> getSelectedExhibits() {
        //TODO: get the list of ExhibitStatus with isAdded=true
        List<ExhibitStatus> exhibitStatuses = new LinkedList<ExhibitStatus>();
        List<ZooData.VertexInfo> selectedExhibits = new ArrayList<>();
        for (ExhibitStatus exhibitStatus : exhibitStatuses){
            if (exhibitStatus.getIsAdded()){
                selectedExhibits.add(getVertexWithId(exhibitStatus.getId()));
            }
        }
        return selectedExhibits;
    }


}
