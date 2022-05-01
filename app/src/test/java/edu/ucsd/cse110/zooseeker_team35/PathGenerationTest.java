package edu.ucsd.cse110.zooseeker_team35;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class PathGenerationTest {
    @Test
    public void testPathCreation() {
        Context context = ApplicationProvider.getApplicationContext();

        String start = "entrance_exit_gate";
        String solutionExhibitOne = "gators";
        String solutionExhibitTwo = "gorillas";
        String solutionExhibitThree = "elephant_odyssey";
        String end = "entrance_exit_gate";

        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");

        ZooMap zooMap = new ZooMap(g);
        List<String> targetExhibits = new LinkedList<>();
        targetExhibits.add(solutionExhibitTwo);
        targetExhibits.add(solutionExhibitThree);
        targetExhibits.add(solutionExhibitOne);
        List<GraphPath<String, IdentifiedWeightedEdge>> paths = zooMap.calculatePath(start, end, targetExhibits);
        assertEquals( paths.size(), 4);
        assertEquals( paths.get(0), DijkstraShortestPath.findPathBetween(g, start, solutionExhibitOne));
        assertEquals( paths.get(1), DijkstraShortestPath.findPathBetween(g, solutionExhibitOne, solutionExhibitTwo));
        assertEquals( paths.get(2), DijkstraShortestPath.findPathBetween(g, solutionExhibitTwo, solutionExhibitThree));
        assertEquals( paths.get(3), DijkstraShortestPath.findPathBetween(g, solutionExhibitThree, end));
    }

    @Test
    public void testPathCreationSingleExhibit() {
        Context context = ApplicationProvider.getApplicationContext();

        String start = "entrance_exit_gate";
        String solutionExhibitOne = "lions";
        String end = "entrance_exit_gate";

        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");

        ZooMap zooMap = new ZooMap(g);
        List<String> targetExhibits = new LinkedList<>();
        targetExhibits.add(solutionExhibitOne);
        List<GraphPath<String, IdentifiedWeightedEdge>> paths = zooMap.calculatePath(start, end, targetExhibits);
        assertEquals( paths.size(), 2);
        assertEquals( paths.get(0), DijkstraShortestPath.findPathBetween(g, start, solutionExhibitOne));
        assertEquals( paths.get(1), DijkstraShortestPath.findPathBetween(g, solutionExhibitOne, end));
    }


}
