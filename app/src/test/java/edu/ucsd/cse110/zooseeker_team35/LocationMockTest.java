package edu.ucsd.cse110.zooseeker_team35;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.location.Location;

import androidx.activity.ComponentActivity;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LocationMockTest {
    private ComponentActivity context;

    @Before
    public void createMockActivity(){
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void injectLatitudeLongitude(){
        LocationProvider locPro = new LocationProvider(context);
        locPro.mockLocation(117,120);

        assertEquals(117.0, locPro.getCurrentLocation().getLatitude(),0);
        assertEquals(120.0, locPro.getCurrentLocation().getLongitude(),0);

    }

    @Test
    public void injectLocation(){
        LocationProvider locPro = new LocationProvider(context);
        Location location = new Location("test");
        location.setLatitude(117.0);
        location.setLongitude(120.0);

        locPro.mockLocation(location);

        assertEquals(117.0, locPro.getCurrentLocation().getLatitude(),0);
        assertEquals(120.0, locPro.getCurrentLocation().getLongitude(),0);
    }

}
