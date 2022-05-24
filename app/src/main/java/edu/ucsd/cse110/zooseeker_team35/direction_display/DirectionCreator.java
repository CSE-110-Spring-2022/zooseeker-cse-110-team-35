package edu.ucsd.cse110.zooseeker_team35.direction_display;

import org.jgrapht.GraphPath;

import java.util.List;

import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;

public interface DirectionCreator {
    List<String> createDirections(GraphPath<String, IdentifiedWeightedEdge> path, List<IdentifiedWeightedEdge> edges, List<String> vertexes);
}
