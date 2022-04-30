package edu.ucsd.cse110.zooseeker_team35;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.jgrapht.Graph;

import java.util.LinkedList;
import java.util.List;

public class PlanResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_results);

        Graph<String, IdentifiedWeightedEdge> zooGraph = ZooData.loadZooGraphJSON(this.getApplicationContext(), "sample_zoo_graph.json");
        ZooMap zooMap = new ZooMap(zooGraph);
        List<ZooData.VertexInfo> selectedExhibits = ZooInfoProvider.getSelectedExhibits();
        List<String> targetExhibits = new LinkedList<>();
        for (ZooData.VertexInfo vertex : selectedExhibits){
            targetExhibits.add(vertex.id);
        }
       zooMap.calculatePath("entrance_exit_gate", "entrance_exit_gate", targetExhibits);

        //TODO: display the results of the plan in a recyclerView
    }

    //
    public void onDirectionsButtonClicked(View view) {
        Intent intent = new Intent(this, DirectionsActivity.class);
        //TODO: either pass in the DirectionTracker to DirectionsActivity or use singleton pattern
        startActivity(intent);
    }
}