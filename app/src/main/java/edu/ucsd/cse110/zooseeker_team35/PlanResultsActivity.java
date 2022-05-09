package edu.ucsd.cse110.zooseeker_team35;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
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
        //TODO: either pass in the DirectionTracker to DirectionsActivity or use singleton pattern
        startActivity(intent);
    }
}