package edu.ucsd.cse110.zooseeker_team35.activities;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatus;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDao;
import edu.ucsd.cse110.zooseeker_team35.database.ExhibitStatusDatabase;
import edu.ucsd.cse110.zooseeker_team35.direction_display.BriefDirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DetailedDirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.direction_display.DirectionCreator;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.Coord;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.DirectionTracker;
import edu.ucsd.cse110.zooseeker_team35.adapters.DirectionsAdapter;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.LocationModel;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.FindClosestExhibitHelper;
import edu.ucsd.cse110.zooseeker_team35.R;
import edu.ucsd.cse110.zooseeker_team35.location_tracking.PermissionChecker;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;

public class DirectionsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Location currentLocation;
    DirectionsAdapter adapter;
    TextView exhibitName;
    private DirectionCreator currentDirectionCreator;
    Switch directionToggle;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ExhibitStatusDao dao;
    LocationModel model;
    private boolean rerouteOffered;
    private boolean useLocationService;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences("shared", MODE_PRIVATE);
        editor = preferences.edit();
        dao = ExhibitStatusDatabase.getSingleton(this).exhibitStatusDao();
        var locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        var permissionChecker = new PermissionChecker(this);
        permissionChecker.ensurePermissions();
        var provider = LocationManager.GPS_PROVIDER;
        currentLocation = new Location(provider);

        useLocationService = getIntent().getBooleanExtra("use_location_updated", false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        model = new ViewModelProvider(this).get(LocationModel.class);

        if (useLocationService) {
            model.addLocationProviderSource(locationManager, provider);
        }

        model.getLastKnownCoords().observe(this, (coord) -> {
            Log.i("Zookeeper Location", String.format("Observing location model update to %s", coord));
            currentLocation.setLatitude(coord.lat);
            currentLocation.setLongitude(coord.lng);
            updateDisplay();
        });

        if(!useLocationService) {
            String mockRoute = getIntent().getStringExtra("mockRoute");
            List<Coord> coords = ZooData.loadRouteJson(this, mockRoute);
            this.mockRoute(coords, 1000, TimeUnit.MILLISECONDS);
        }
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
        updateDisplay();
    }

    public void onPrevButtonClicked(View view) {
        DirectionTracker.prevExhibit();
        updateDisplay();
    }

    public void onNextButtonClicked(View view) {
        //Marks the "soon to be previous" exhibit as being visited
        String currentExhibit = DirectionTracker.getCurrentExhibit();
        //ExhibitStatus currentExhibitStatus = dao.get(currentExhibit);

        if (DirectionTracker.getCurrentExhibitId().equals("entrance_exit_gate")){
            return;
        }

        ExhibitStatus currentExhibitStatus = dao.get(DirectionTracker.getCurrentExhibitId());
        currentExhibitStatus.setIsVisited(true);
        dao.update(currentExhibitStatus);

        DirectionTracker.nextExhibit();
        editor.putInt("currentExhibit", DirectionTracker.getCurrentExhibitIndex());
        editor.apply();

        if (ZooInfoProvider.getVertexWithId(DirectionTracker.getCurrentExhibitId()).group_id != null){
            rerouteOffered = true;
        } else {
            rerouteOffered = false;
        }
        updateDisplay();
    }

    //update the display to the current exhibit and directions to current exhibit
    private void updateDisplay() {
        List<String> directions;
        if (currentLocation != null) {
            String currentId = DirectionTracker.getCurrentExhibitId();
            List<ZooData.VertexInfo> unvisitedNodes = DirectionTracker.getRemainingVertexes();
            ZooData.VertexInfo closestExhibit;
            if (!unvisitedNodes.isEmpty()) {
                closestExhibit = FindClosestExhibitHelper.closestExhibitPathwise(DirectionTracker.getGraph(),currentLocation, unvisitedNodes);
                //check if the two exhibits belong to same group
                System.out.println("closest unvisited: " + closestExhibit.id);
                ZooData.VertexInfo curExhibit = ZooInfoProvider.getVertexWithId(currentId);
                System.out.println("current exhibit: " + curExhibit.id);
                if (!checkNodeEquality(curExhibit, closestExhibit)) {
                    promptReroute(closestExhibit.id);
                }
            }
            closestExhibit = FindClosestExhibitHelper.closestExhibit(currentLocation);
            directions = DirectionTracker.getDirectionsToCurrentExhibit(currentDirectionCreator ,closestExhibit);
        } else {
            directions = DirectionTracker.getDirectionsToCurrentExhibit(currentDirectionCreator);
        }
        adapter.setExhibits(directions);
        exhibitName.setText(DirectionTracker.getCurrentExhibit());
    }

    private boolean checkNodeEquality (ZooData.VertexInfo node1, ZooData.VertexInfo node2) {
        if (node1.id.equals(node2.id)){
            return true;
        }
        if (node1.group_id != null && node1.group_id.equals(node2.id)){
            return true;
        }
        if (node2.group_id != null && node2.group_id.equals(node1.id)){
            return true;
        }
        if (node2.group_id != null && node1.group_id != null && node1.group_id.equals(node2.group_id)){
            return true;
        }
        return false;
    }

    private void promptReroute(String closestVertex) {
        if (rerouteOffered) {
            return;
        }
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        reroute(closestVertex);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You are closer to " + ZooInfoProvider.getVertexWithId(closestVertex).name + " than your current destination. Do You Want To Reroute?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

        rerouteOffered = true;
    }

    private void reroute(String closestVertex){
        List<String> remainingExhibits = DirectionTracker.getRemainingVertexes().stream().map(vertex -> vertex.id).collect(Collectors.toList());
        remainingExhibits.remove(closestVertex);
        DirectionTracker.updatePathList(closestVertex, remainingExhibits);
        updateDisplay();
    }


    public void onResetButtonClicked(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @VisibleForTesting
    public void mockLocation(Coord coords) {
        model.mockLocation(coords);
    }

    @VisibleForTesting
    public Future<?> mockRoute(List<Coord> route, long delay, TimeUnit unit) {
        return model.mockRoute(route, delay, unit);
    }

    public void onMockButtonPressed(View view) {
        TextView mockRouteTv = this.findViewById(R.id.coord_text);
        String route = mockRouteTv.getText().toString();
        model.removeLocationProviderSource();
        if(route.contains(".json")) {
            List<Coord> coords = ZooData.loadRouteJson(this, route);
            this.mockRoute(coords, 5000, TimeUnit.MILLISECONDS);
        }
        else {
            String[] coord = route.split(",");
            for(String s : coord) {
                s = s.trim();
            }
            try {
                double lat = Double.parseDouble(coord[0]);
                double lng = Double.parseDouble(coord[1]);

                Coord mockCoord = new Coord(lat, lng);
                currentLocation.setLatitude(lat);
                currentLocation.setLongitude(lng);

                this.mockLocation(mockCoord);
            } catch (NumberFormatException e)
            {
                e.printStackTrace();
                Log.d("Exceptions", "Input was not a valid double pair");
            }
        }
    }

    public void onEnableGPSClicked(View view) {
        var locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        var provider = LocationManager.GPS_PROVIDER;
        model.addLocationProviderSource(locationManager, provider);
    }

    public void onSkipButtonClicked(View view) {
        String currentExhibitId = DirectionTracker.getCurrentExhibitId();
        if (currentExhibitId.equals("entrance_exit_gate")){
            return;
        }
        if (ZooInfoProvider.getVertexWithId(currentExhibitId).group_id != null) {
            return;
        }
        List<ZooData.VertexInfo> targetExhibits = DirectionTracker.getRemainingVertexes();
        System.out.println("important: " + targetExhibits.stream().map(vertex -> vertex.id).collect(Collectors.toList()));
        while (!targetExhibits.isEmpty() && targetExhibits.get(0).group_id != null) {
            targetExhibits.remove(0);
        }
        if (targetExhibits.isEmpty()){
            targetExhibits.add(ZooInfoProvider.getVertexWithId("entrance_exit_gate"));
        }
        ZooData.VertexInfo closestExhibit = FindClosestExhibitHelper.closestExhibit(currentLocation, targetExhibits);
        targetExhibits.remove(closestExhibit);
        List<String> remainingExhibits = targetExhibits.stream().map(vertex -> vertex.id).collect(Collectors.toList());
        DirectionTracker.updatePathList(closestExhibit.id, remainingExhibits);
        updateDisplay();
    }
}