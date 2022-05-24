package edu.ucsd.cse110.zooseeker_team35.direction_display;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;

public class DetailedDirectionCreator implements DirectionCreator{

    @Override
    public List<String> createDirections(GraphPath<String, IdentifiedWeightedEdge> path, Map<String, ZooData.VertexInfo> vertexInfo, Map<String, ZooData.EdgeInfo> edgeInfo, Graph<String, IdentifiedWeightedEdge> graph) {
        DirectionFormatStrategy directionFormatter = new ProceedDirectionFormat();
        List<IdentifiedWeightedEdge> edges = path.getEdgeList();
        List<String> vertexes = path.getVertexList();
        List<String> directionList = new ArrayList<String>();
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
}
