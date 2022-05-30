package edu.ucsd.cse110.zooseeker_team35.location_tracking;

import android.location.Location;

import androidx.annotation.VisibleForTesting;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.ProceedDirectionFormat;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionFormatStrategy;
import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;

//      DirectionTracker has methods nextExhibit, prevExhibit such that nextExhibit moves to the next exhibit,
//      prevExhibit moves to the previous exhibit
//      DirectionTracker will be used to keep track of the current exhibit we are getting directions to and
//      the information about the Graph
public class DirectionTracker{
    private static int currentExhibit;
    private static List<GraphPath<String, IdentifiedWeightedEdge>> pathList;
    private static Map<String, ZooData.VertexInfo> vertexInfo;
    private static Map<String, ZooData.EdgeInfo> edgeInfo;
    private static Graph<String, IdentifiedWeightedEdge> graph;

    @VisibleForTesting
    public static void setCurrentExhibit(int currentExhibit) {
        DirectionTracker.currentExhibit = currentExhibit;
    }

    public static void initialize(Graph<String, IdentifiedWeightedEdge> newGraph, List<GraphPath<String, IdentifiedWeightedEdge>> newPathList) {
        pathList = newPathList;
        graph = newGraph;
        currentExhibit = 0;
        vertexInfo = ZooInfoProvider.getVertexMap();
        edgeInfo = ZooInfoProvider.getEdgeMap();
    }

    public static void nextExhibit(){
        if (currentExhibit < pathList.size() - 1) {
            currentExhibit++;
        }
    }
    public static void prevExhibit(){
        if (currentExhibit > 0) {
            currentExhibit--;
        }
    }

    public static List<String> getDirectionsToCurrentExhibit(){
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


    public static String getCurrentExhibit() {
        return ZooInfoProvider.getVertexWithId(pathList.get(currentExhibit).getEndVertex()).name;
    }

    public static List<String> getDirectionsToCurrentExhibit (DirectionCreator directionCreator){
        GraphPath<String, IdentifiedWeightedEdge> path = pathList.get(currentExhibit);
        return getDirectionsToCurrentExhibit(directionCreator, ZooInfoProvider.getVertexWithId(path.getStartVertex()));
    }

    public static List<String> getDirectionsToCurrentExhibit (DirectionCreator directionCreator, ZooData.VertexInfo closestExhibit){
        List<String> directionList = new ArrayList<String>();
        GraphPath<String, IdentifiedWeightedEdge> path = pathList.get(currentExhibit);
        if (path.getWeight() == 0){
            String id = path.getStartVertex();
            ZooData.VertexInfo vertex = ZooInfoProvider.getVertexWithId(id);
            ZooData.VertexInfo parent = ZooInfoProvider.getVertexWithId(vertex.parent_id);
            directionList.add("1. Find " + vertex.name + " inside " + parent.name);
            return directionList;
        }
        path = reCalculatedDirection(closestExhibit);
        return directionCreator.createDirections(path, vertexInfo, edgeInfo, graph);
    }

    public static GraphPath<String, IdentifiedWeightedEdge> reCalculatedDirection(ZooData.VertexInfo closestExhibit) {
        String lastVertex = pathList.get(currentExhibit).getEndVertex();
        GraphPath<String, IdentifiedWeightedEdge> newPath = DijkstraShortestPath.findPathBetween(graph, closestExhibit.id, lastVertex);
        return newPath;
    }

}
