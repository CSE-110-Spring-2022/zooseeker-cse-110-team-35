package edu.ucsd.cse110.zooseeker_team35.path_finding;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.traverse.ClosestFirstIterator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
Solves the Traveling Salesman problem by repeatedly finding the closeset next unvisited node
*/

public class ShortestPathTSAlgorithm implements PathAlgorithmStrategy {
    @Override
    public List<GraphPath<String, IdentifiedWeightedEdge>> findShortestWeightedPath(Graph<String, IdentifiedWeightedEdge> zooGraph, String start, String end, List<String> selectedExhibits) {
        List<GraphPath<String, IdentifiedWeightedEdge>> pathList = new ArrayList<GraphPath<String, IdentifiedWeightedEdge>>();

        Set<String> targetExhibits = new HashSet<String>(selectedExhibits);

        String pathStart = start;
        while (!targetExhibits.isEmpty()) {
            ClosestFirstIterator graphIterator = new ClosestFirstIterator(zooGraph, pathStart);
            graphIterator.next();
            Object closestExhibit = graphIterator.next();
            while (!targetExhibits.contains(closestExhibit.toString())) {
                closestExhibit = graphIterator.next();
            }
            String closestTargetExhibit = closestExhibit.toString();
            targetExhibits.remove(closestTargetExhibit);
            GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(zooGraph, pathStart, closestTargetExhibit);
            pathList.add(path);
            pathStart = closestTargetExhibit;
        }
        GraphPath<String, IdentifiedWeightedEdge> exitPath = DijkstraShortestPath.findPathBetween(zooGraph, pathStart, end);
        pathList.add(exitPath);
        return pathList;
    }
}

