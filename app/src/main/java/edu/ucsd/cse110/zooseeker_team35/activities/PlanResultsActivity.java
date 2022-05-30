package edu.ucsd.cse110.zooseeker_team35.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.ucsd.cse110.zooseeker_team35.location_tracking.DirectionTracker;
import edu.ucsd.cse110.zooseeker_team35.path_finding.IdentifiedWeightedEdge;
import edu.ucsd.cse110.zooseeker_team35.R;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooPathFinder;


public class PlanResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_results);

        Graph<String, IdentifiedWeightedEdge> zooGraph = ZooData.loadZooGraphJSON(this.getApplicationContext(), ZooInfoProvider.zooGraphJSON);
        ZooPathFinder zooPathFinder = new ZooPathFinder(zooGraph);
        List<ZooData.VertexInfo> selectedExhibits = ZooInfoProvider.getSelectedExhibits(this.getApplicationContext());
        List<String> targetExhibits = new LinkedList<>();
        for (ZooData.VertexInfo vertex : selectedExhibits){
            targetExhibits.add(vertex.id);
        }
        List<GraphPath<String, IdentifiedWeightedEdge>> pathList = zooPathFinder.calculatePath("entrance_exit_gate", "entrance_exit_gate", targetExhibits);
        DirectionTracker.initialize(zooGraph, pathList);

        List<String> planSummary = new ArrayList<>();
        planSummary.add(ZooInfoProvider.getVertexWithId(pathList.get(0).getStartVertex()).name + "\n\n");
        double distance = 0;
        for (GraphPath<String, IdentifiedWeightedEdge> path : pathList){
            distance += path.getWeight();
            planSummary.add( ZooInfoProvider.getVertexWithId(path.getEndVertex()).name + " - " + distance + " feet away \n\n");
        }
        ListView listView = findViewById(R.id.plan_summary);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                planSummary);
        listView.setAdapter(adapter);
    }

    //
    public void onDirectionsButtonClicked(View view) {
        Intent intent = new Intent(this, DirectionsActivity.class);
        startActivity(intent);
    }

    public void onBackButtonClicked(View view) {
        finish();
    }
}