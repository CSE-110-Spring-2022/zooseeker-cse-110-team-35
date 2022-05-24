package edu.ucsd.cse110.zooseeker_team35;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;

import edu.ucsd.cse110.zooseeker_team35.direction_display.BriefDirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DetailedDirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooPathFinder;

public class DirectionCreatorTest {
    @Test
    public void checkBriefDirectionCreator() {
        String start = "entrance_exit_gate";
        String solutionExhibitOne = "gators";
        String solutionExhibitTwo = "gorillas";
        String solutionExhibitThree = "elephant_odyssey";
        String end = "entrance_exit_gate";

        List<String> targetExhibits = new LinkedList<>();
        targetExhibits.add(solutionExhibitTwo);
        targetExhibits.add(solutionExhibitThree);
        targetExhibits.add(solutionExhibitOne);

        Context context = ApplicationProvider.getApplicationContext();
        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");
        ZooPathFinder zooPathFinder = new ZooPathFinder(g);
        List<GraphPath<String, IdentifiedWeightedEdge>> paths = zooPathFinder.calculatePath(start, end, targetExhibits);

        DirectionCreator dc = new BriefDirectionCreator();

        List<String> briefDirections;

        final String tag = "brief direction: ";

        for(int i = 0; i < paths.size(); i++) {
            briefDirections = dc.createDirections(paths.get(i),/*argument*/, /*argument*/);
            for(int j = 0; j < briefDirections.size(); j++) {
                Log.i(tag, briefDirections.get(j));
            }
        }
    }

    @Test
    public void checkDetailedDirectionCreator() {
        String start = "entrance_exit_gate";
        String solutionExhibitOne = "gators";
        String solutionExhibitTwo = "gorillas";
        String solutionExhibitThree = "elephant_odyssey";
        String end = "entrance_exit_gate";

        List<String> targetExhibits = new LinkedList<>();
        targetExhibits.add(solutionExhibitTwo);
        targetExhibits.add(solutionExhibitThree);
        targetExhibits.add(solutionExhibitOne);

        Context context = ApplicationProvider.getApplicationContext();
        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(context,"sample_zoo_graph.json");
        ZooPathFinder zooPathFinder = new ZooPathFinder(g);
        List<GraphPath<String, IdentifiedWeightedEdge>> paths = zooPathFinder.calculatePath(start, end, targetExhibits);

        DirectionCreator dc = new DetailedDirectionCreator();

        List<String> detailedDirections;

        final String tag = "brief direction: ";

        for(int i = 0; i < paths.size(); i++) {
            detailedDirections = dc.createDirections(paths.get(i),/*argument*/, /*argument*/);
            for(int j = 0; j < detailedDirections.size(); j++) {
                Log.i(tag, detailedDirections.get(j));
            }
        }
    }
}
