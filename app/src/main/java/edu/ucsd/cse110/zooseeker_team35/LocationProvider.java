package edu.ucsd.cse110.zooseeker_team35;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;

/*
USAGE (IMPORTANT):
Before creating a locationProvider object, you must ensure permissions with PermissionChecker.
This should be used in the same way that it is in the lab, where we return based on the
"ensurePermissions" boolean, within the activity.
 */

public class LocationProvider {

    private Context context;
    private LocationListener locationListener;
    private Location currentLocation;

    public LocationProvider(Context context){
        this.context = context;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location){
                Log.d("ZooSeeker", String.format("Location changed: %s", location));

                currentLocation = location;
            }
        };
    }

    public void mockLocation(double latitude, double longitude){
        currentLocation.setLongitude(longitude);
        currentLocation.setLatitude(latitude);
    }

    public void mockLocation(Location location){
        currentLocation = location;
    }

    public Location getCurrentLocation(){
        return currentLocation;
    }
}
