package edu.ucsd.cse110.zooseeker_team35.location_tracking;

import androidx.annotation.VisibleForTesting;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.direction_display.BriefDirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DetailedDirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;
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
        return DirectionTracker.getDirectionsToCurrentExhibit(new DetailedDirectionCreator());
    }

    public static List<String> getDirectionsToCurrentExhibit(DirectionCreator directionCreator){
        GraphPath<String, IdentifiedWeightedEdge> path = pathList.get(currentExhibit);
        return directionCreator.createDirections(path, vertexInfo, edgeInfo, graph);
    }

    public static String getCurrentExhibit() {
        return ZooInfoProvider.getVertexWithId(pathList.get(currentExhibit).getEndVertex()).name;
    }

    public static int getCurrentExhibitIndex() {
        return currentExhibit;
    }

    public static GraphPath<String, IdentifiedWeightedEdge> reCalculatedDirection(ZooData.VertexInfo closestExhibit) {
        String lastVertex = pathList.get(currentExhibit).getEndVertex();
        GraphPath<String, IdentifiedWeightedEdge> newPath = DijkstraShortestPath.findPathBetween(graph, closestExhibit.id, lastVertex);
        return newPath;
    }

    public static List<String> getDirectionsFromClosestExhibit(DirectionCreator directionCreator, ZooData.VertexInfo closestExhibit){
        GraphPath<String, IdentifiedWeightedEdge> path = reCalculatedDirection(closestExhibit);
        return directionCreator.createDirections(path, vertexInfo, edgeInfo, graph);
    }
}
