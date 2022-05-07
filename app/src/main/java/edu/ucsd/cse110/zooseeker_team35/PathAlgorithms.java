package edu.ucsd.cse110.zooseeker_team35;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.traverse.ClosestFirstIterator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PathAlgorithms {
    List<GraphPath<String, IdentifiedWeightedEdge>> findShortestWeightedPath(Graph<String, IdentifiedWeightedEdge> zooGraph, String start, String end, List<String> selectedExhibits);
}
