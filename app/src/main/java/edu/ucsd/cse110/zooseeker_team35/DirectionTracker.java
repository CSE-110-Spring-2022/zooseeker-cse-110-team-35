package edu.ucsd.cse110.zooseeker_team35;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//      DirectionTracker has methods nextExhibit, prevExhibit such that nextExhibit moves to the next exhibit,
//      prevExhibit moves to the previous exhibit, and getDirectionsToCurrentExhibit()
//      returns the list of edges for the current exhibit,
//      DirectionTracker will be used to keep track of the current exhibit we are getting directions to
public class DirectionTracker implements Serializable {
    static int currentExhibit;
    static List<GraphPath<String, IdentifiedWeightedEdge>> pathList;
    static Map<String, ZooData.VertexInfo> vertexInfo;
    static Map<String, ZooData.EdgeInfo> edgeInfo;
    static Graph<String, IdentifiedWeightedEdge> graph;

    static void initialize(Graph<String, IdentifiedWeightedEdge> newGraph, List<GraphPath<String, IdentifiedWeightedEdge>> newPathList) {
        pathList = newPathList;
        graph = newGraph;
        currentExhibit = 0;
        vertexInfo = ZooInfoProvider.getVertexMap();
        edgeInfo = ZooInfoProvider.getEdgeMap();
    }

    static void nextExhibit(){
        if (currentExhibit < pathList.size() - 1) {
            currentExhibit++;
        }
    }
    static void prevExhibit(){
        if (currentExhibit > 0) {
            currentExhibit--;
        }
    }

    static List<String> getDirectionsToCurrentExhibit(){
        List<String> directionList = new ArrayList<String>();
        GraphPath<String, IdentifiedWeightedEdge> path = pathList.get(currentExhibit);

        List<IdentifiedWeightedEdge> edges = path.getEdgeList();
        List<String> vertexes = path.getVertexList();

        DirectionFormatStrategy directionFormatter = new ProceedDirectionFormat();
        for (int i = 0; i < edges.size(); i++) {
            IdentifiedWeightedEdge e = edges.get(i);
            String startNode = vertexes.get(i);
            String endNode = vertexes.get(i + 1);
            String pathInfo = directionFormatter.buildDirection(
                    i+1,
                    vertexInfo.get(startNode).name,
                    vertexInfo.get(endNode).name,
                    edgeInfo.get(e.getId()).street,
                    graph.getEdgeWeight(e));
            directionList.add(pathInfo);
        }
        return directionList;
    }

    static String getCurrentExhibit() {
        return ZooInfoProvider.getVertexWithId(pathList.get(currentExhibit).getEndVertex()).name;
    }
}
