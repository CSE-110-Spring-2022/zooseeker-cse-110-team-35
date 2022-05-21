package edu.ucsd.cse110.zooseeker_team35;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.List;

public class ZooPathFinder {
    //TODO: initialize the Graph using vertex information from the database
    Graph<String, IdentifiedWeightedEdge> zooGraph;
    PathAlgorithmStrategy pathAlgorithm;

    ZooPathFinder(Graph<String, IdentifiedWeightedEdge> zooGraph) {
        pathAlgorithm = new ShortestPathTSAlgorithm();
        this.zooGraph = zooGraph;
    }

    ZooPathFinder(Graph<String, IdentifiedWeightedEdge> zooGraph, PathAlgorithmStrategy pathAlgorithm){
        this.pathAlgorithm = pathAlgorithm;
        this.zooGraph = zooGraph;
    }

    public List<GraphPath<String, IdentifiedWeightedEdge>> calculatePath(String start, String end, List<String> selectedExhibits) {
        return pathAlgorithm.findShortestWeightedPath(zooGraph, start, end, selectedExhibits);
    }
}
