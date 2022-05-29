package edu.ucsd.cse110.zooseeker_team35.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatus;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDao;
import edu.ucsd.cse110.zooseeker_team35.direction_display.BriefDirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DetailedDirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionFormatStrategy;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionFormatStrategy;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.DirectionTracker;
import edu.ucsd.cse110.zooseeker_team35.adapters.DirectionsAdapter;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.LocationProvider;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.FindClosestExhibitHelper;
import edu.ucsd.cse110.zooseeker_team35.R;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.ZooLiveMap;

public class DirectionsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Location currentLocation;
    private LocationProvider subject;
    DirectionsAdapter adapter;
    TextView exhibitName;
    ZooLiveMap zooLiveMap;
    private DirectionCreator currentDirectionCreator;
    Switch directionToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        subject = new LocationProvider(this);
//        periodicUpDateLocation();
        LocationProvider userLocationProvider = new LocationProvider(this);
        zooLiveMap = new ZooLiveMap(userLocationProvider);


        var locationManger = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("location changed to: :" + location);
                currentLocation = location;
                updateDisplay();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                10000,     //10 second intervals
                10,
                locationListener);


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


//        int newExhibit = currentExhibit(closestExhibit);
//        if(newExhibit >= 0) {
//            DirectionTracker.setCurrentExhibit(newExhibit);
//            updateDisplay();
//        }
//        else {
//            exhibitName.setText(closestExhibit.name);
//            adapter.setExhibits(DirectionTracker.getDirectionsFromClosestExhibit(currentDirectionCreator, closestExhibit));
//        }
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
        List<String> directions;
        if (currentLocation != null) {
            ZooData.VertexInfo closestExhibit = FindClosestExhibitHelper.
                    closestExhibit(this, currentLocation, ZooInfoProvider.getVisitableVertexList());
            System.out.println(closestExhibit);
            directions = DirectionTracker.getDirectionsToCurrentExhibit(closestExhibit);
        } else {
            directions = DirectionTracker.getDirectionsToCurrentExhibit();
        }
        adapter.setExhibits(directions);
        exhibitName.setText(DirectionTracker.getCurrentExhibit());
    }
//
//    protected void onStart() {
//        super.onStart();
//        activeThread = true;
//    }
//
//    protected void onRestart() {
//        super.onRestart();
//        activeThread = true;
//    }
//
//    protected void onPause() {
//        super.onPause();
//        activeThread = false;
//    }
//
//    protected void onStop() {
//        super.onStop();
//        activeThread = false;
//    }

//    private void periodicUpDateLocation() {
//        this.future = backgroundThreadExecutor.submit(() -> {
//            do {
//                currentLocation = subject.getCurrentLocation();
//                Thread.sleep(5000);
//            } while (activeThread);
//            return null;
//        });
//    }

    public int currentExhibit(ZooData.VertexInfo nearestExhibit) {
        int index = -1;
        List<ZooData.VertexInfo> addedExhibits = ZooInfoProvider.getSelectedExhibits(this);
        if (addedExhibits.contains(nearestExhibit)) {
            index = addedExhibits.indexOf(nearestExhibit);
        }
        return index;
    }

    public void onResetButtonClicked(View view) {
        finish();
    }
}