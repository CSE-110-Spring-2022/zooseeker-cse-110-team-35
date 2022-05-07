package edu.ucsd.cse110.zooseeker_team35;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.traverse.ClosestFirstIterator;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ZooMap {
    //TODO: initialize the Graph using vertex information from the database
    Graph<String, IdentifiedWeightedEdge> zooGraph;

    ZooMap(Graph<String, IdentifiedWeightedEdge> zooGraph) {
        this.zooGraph = zooGraph;
    }

    PathAlgorithms pathAlgorithm = new ZooPathFinder();
    public List<GraphPath<String, IdentifiedWeightedEdge>> calculatePath(String start, String end, List<String> selectedExhibits) {
        return pathAlgorithm.findShortestWeightedPath(zooGraph, start, end, selectedExhibits);
    }
}
