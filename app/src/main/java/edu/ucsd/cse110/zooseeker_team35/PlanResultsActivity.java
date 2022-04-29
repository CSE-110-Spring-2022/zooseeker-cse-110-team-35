package edu.ucsd.cse110.zooseeker_team35;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PlanResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_results);

        ZooMap zooMap = new ZooMap();
        zooMap.calculatePath();
        //TODO: display the results of the plan in a recyclerView
    }

    //
    public void onDirectionsButtonClicked(View view) {
        Intent intent = new Intent(this, DirectionsActivity.class);
        //TODO: either pass in the DirectionTracker to DirectionsActivity or use singleton pattern
        startActivity(intent);
    }
}