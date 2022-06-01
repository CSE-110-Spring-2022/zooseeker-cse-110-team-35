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
    LocationManager locationManager;
    String provider;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Sets up SharedPreferences access for this activity
        preferences = getSharedPreferences("shared", MODE_PRIVATE);
        editor = preferences.edit();
        editor.putInt("currentExhibit", DirectionTracker.getCurrentExhibitIndex());

        dao = ExhibitStatusDatabase.getSingleton(this).exhibitStatusDao();

        //Sets locationManager/provider and grabs an "empty" location for mocking
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        var permissionChecker = new PermissionChecker(this);
        permissionChecker.ensurePermissions();
        provider = LocationManager.GPS_PROVIDER;
        currentLocation = new Location(provider);

        //useLocationService = getIntent().getBooleanExtra("use_location_updated", false);

        //Sets us Directions RecyclerView
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        //initiates ViewModelProvider
        model = new ViewModelProvider(this).get(LocationModel.class);
        model.addLocationProviderSource(locationManager, provider);

        //Defines the listener for whenever the GPS location changed
        model.getLastKnownCoords().observe(this, (coord) -> {
            Log.i("Zookeeper Location", String.format("Observing location model update to %s", coord));
            currentLocation.setLatitude(coord.lat);
            currentLocation.setLongitude(coord.lng);
            updateDisplay();
        });

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

        if (DirectionTracker.getCurrentExhibitId().equals("entrance_exit_gate")){
            return;
        }

        //Makes sure the node is marked as visited in the database
        ExhibitStatus currentExhibitStatus = dao.get(DirectionTracker.getCurrentExhibitId());
        currentExhibitStatus.setIsVisited(true);
        dao.update(currentExhibitStatus);

        //Moves direction along and Keeps track of current exhibit
        DirectionTracker.nextExhibit();
        editor.putInt("currentExhibit", DirectionTracker.getCurrentExhibitIndex());
        editor.apply();

        //Check if reroute should be offered or not
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

            //Grabs current exhibit and checks which nodes are left in the route
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

        //Updates recyclerView with the new Directions from currentLocation
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

    //Reroute occurs if there is a new optimal route through the rest of the plan due to a change
    //in the user's current location
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

    //This method is called if user choose to reroute when prompted
    private void reroute(String closestVertex){
        List<String> remainingExhibits = DirectionTracker.getRemainingVertexes().stream().map(vertex -> vertex.id).collect(Collectors.toList());
        remainingExhibits.remove(closestVertex);
        DirectionTracker.updatePathList(closestVertex, remainingExhibits);
        updateDisplay();
    }

    //The reset button takes the user back to the HomeActivity where they can restart their list
    //or start a new list
    public void onResetButtonClicked(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("reset", true);
        startActivity(intent);
    }

    //Mocks a location update for the Locationmodel
    @VisibleForTesting
    public void mockLocation(Coord coords) {
        model.mockLocation(coords);
    }

    //Mocks a sequence of positions from a json with a time delay between updates
    @VisibleForTesting
    public Future<?> mockRoute(List<Coord> route, long delay, TimeUnit unit) {
        return model.mockRoute(route, delay, unit);
    }

    //Button which mocks either a route or single location depending on input
    public void onMockButtonPressed(View view) {
        TextView mockRouteTv = this.findViewById(R.id.coord_text);
        String route = mockRouteTv.getText().toString();

        //Removes location source so that app is in "mocking mode" and won't check gps
        model.removeLocationProviderSource();

        //If string is address of json file
        if(route.contains(".json")) {
            List<Coord> coords = ZooData.loadRouteJson(this, route);
            this.mockRoute(coords, 5000, TimeUnit.MILLISECONDS);
        }
        //If not a json then attempts to read input as a comma separated latitude,longitude pair
        else {
            //Splits on comma
            String[] coord = route.split(",");

            //trims whitespace for formatting
            for(String s : coord) {
                s = s.trim();
            }

            //attempts to parse the two doubles and logs an exception if error is thrown
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
        model.addLocationProviderSource(locationManager, provider);
    }

    //Skips the next exhibit in the plan
    public void onSkipButtonClicked(View view) {
        String currentExhibitId = DirectionTracker.getCurrentExhibitId();
        if (currentExhibitId.equals("entrance_exit_gate")){
            return;
        }
        if (ZooInfoProvider.getVertexWithId(currentExhibitId).group_id != null) {
            return;
        }
        List<ZooData.VertexInfo> targetExhibits = DirectionTracker.getRemainingVertexes();
        targetExhibits.remove(0);
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

        //Updates the current index so that on restart the app remembers where the user was going
        int index = DirectionTracker.getCurrentExhibitIndex() + 1;
        editor.putInt("currentExhibit", index);
        editor.apply();

        updateDisplay();
    }
}