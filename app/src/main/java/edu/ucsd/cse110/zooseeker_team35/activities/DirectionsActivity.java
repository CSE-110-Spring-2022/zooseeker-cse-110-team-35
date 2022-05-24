package edu.ucsd.cse110.zooseeker_team35.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionFormatStrategy;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.DirectionTracker;
import edu.ucsd.cse110.zooseeker_team35.adapters.DirectionsAdapter;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.LocationProvider;
import edu.ucsd.cse110.zooseeker_team35.R;

public class DirectionsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    DirectionsAdapter adapter;
    TextView exhibitName;
    DirectionFormatStrategy formatStrategy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        LocationProvider userLocationProvider = new LocationProvider(this);
        setup();
    }

    private void setup() {
        exhibitName = findViewById(R.id.exhibit_name);
        exhibitName.setText(DirectionTracker.getCurrentExhibit());
        adapter = new DirectionsAdapter();
        adapter.setHasStableIds(true);
        recyclerView = findViewById(R.id.directions_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        updateDisplay();
    }

    public void onPrevButtonClicked(View view) {
        DirectionTracker.prevExhibit();
        updateDisplay();
    }

    public void onNextButtonClicked(View view) {
        DirectionTracker.nextExhibit();
        updateDisplay();
    }

    //update the display to the current exhibit and directions to current exhibit
    private void updateDisplay() {
        exhibitName.setText(DirectionTracker.getCurrentExhibit());
        adapter.setExhibits(DirectionTracker.getDirectionsToCurrentExhibit());
    }

}