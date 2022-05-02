package edu.ucsd.cse110.zooseeker_team35;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.LinkedList;
import java.util.List;

public class PlanResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_results);

        Graph<String, IdentifiedWeightedEdge> zooGraph = ZooData.loadZooGraphJSON(this.getApplicationContext(), "sample_zoo_graph.json");
        ZooMap zooMap = new ZooMap(zooGraph);
        List<ZooData.VertexInfo> selectedExhibits = ZooInfoProvider.getSelectedExhibits(this.getApplicationContext());
        List<String> targetExhibits = new LinkedList<>();
        for (ZooData.VertexInfo vertex : selectedExhibits){
            targetExhibits.add(vertex.id);
        }
        List<GraphPath<String, IdentifiedWeightedEdge>> pathList = zooMap.calculatePath("entrance_exit_gate", "entrance_exit_gate", targetExhibits);

        //TODO: display the results of the plan in a recyclerView
        StringBuilder results = new StringBuilder();
        results.append(pathList.get(0).getStartVertex()).append("\n\n");
        double distance = 0;
        for (GraphPath<String, IdentifiedWeightedEdge> path : pathList){
            distance += path.getWeight();
            results.append(path.getEndVertex()).append(" - ").append(distance).append(" feet away \n\n");
        }
        TextView textView = findViewById(R.id.plan_results);
        textView.setText(results.toString());
    }

    //
    public void onDirectionsButtonClicked(View view) {
        Intent intent = new Intent(this, DirectionsActivity.class);
        //TODO: either pass in the DirectionTracker to DirectionsActivity or use singleton pattern
        startActivity(intent);
    }
}