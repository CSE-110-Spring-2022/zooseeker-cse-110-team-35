package edu.ucsd.cse110.zooseeker_team35.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.ucsd.cse110.zooseeker_team35.direction_display.BriefDirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DetailedDirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionFormatStrategy;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.DirectionTracker;
import edu.ucsd.cse110.zooseeker_team35.adapters.DirectionsAdapter;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.LocationProvider;
import edu.ucsd.cse110.zooseeker_team35.R;

public class DirectionsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;
    private Location currentLocation;
    private LocationProvider subject;
    private static boolean activeThread;
    DirectionsAdapter adapter;
    TextView exhibitName;
    DirectionCreator currentDirectionCreator;
    Switch directionToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        LocationProvider userLocationProvider = new LocationProvider(this);
        setup();
    }

    private void setup() {
        subject = new LocationProvider(this);
        exhibitName = findViewById(R.id.exhibit_name);
        exhibitName.setText(DirectionTracker.getCurrentExhibit());
        adapter = new DirectionsAdapter();
        adapter.setHasStableIds(true);
        recyclerView = findViewById(R.id.directions_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        this.currentDirectionCreator = new DetailedDirectionCreator();
        directionToggle = (Switch)  findViewById(R.id.direction_toggle);
        directionToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    currentDirectionCreator = new BriefDirectionCreator();
                } else {
                    currentDirectionCreator = new DetailedDirectionCreator();
                }
                updateDisplay();
            }
        });
        periodicUpDateLocation();
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
        adapter.setExhibits(DirectionTracker.getDirectionsToCurrentExhibit(currentDirectionCreator));
    }

    protected void onStart() {
        super.onStart();
        activeThread = true;
    }

    protected void onRestart() {
        super.onRestart();
        activeThread = true;
    }

    protected void onPause() {
        super.onPause();
        activeThread = false;
    }

    protected void onStop() {
        super.onStop();
        activeThread = false;
    }

    private void periodicUpDateLocation() {
        this.future = backgroundThreadExecutor.submit(() -> {
            do {
                currentLocation = subject.getCurrentLocation();
                Thread.sleep(5000);
            } while (activeThread);
            return null;
        });
    }
}