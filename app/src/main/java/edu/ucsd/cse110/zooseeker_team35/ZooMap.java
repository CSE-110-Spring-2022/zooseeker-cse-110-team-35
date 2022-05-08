package edu.ucsd.cse110.zooseeker_team35;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.nio.file.Path;
import java.util.List;

public class ZooMap {
    //TODO: initialize the Graph using vertex information from the database
    Graph<String, IdentifiedWeightedEdge> zooGraph;
    PathAlgorithmStrategy pathAlgorithm;

    ZooMap(Graph<String, IdentifiedWeightedEdge> zooGraph) {
        pathAlgorithm = new ShortestPathTSTAlgorithm();
        this.zooGraph = zooGraph;
    }

    ZooMap(Graph<String, IdentifiedWeightedEdge> zooGraph, PathAlgorithmStrategy pathAlgorithm){
        this.pathAlgorithm = pathAlgorithm;
        this.zooGraph = zooGraph;
    }

    public List<GraphPath<String, IdentifiedWeightedEdge>> calculatePath(String start, String end, List<String> selectedExhibits) {
        return pathAlgorithm.findShortestWeightedPath(zooGraph, start, end, selectedExhibits);
    }
}
