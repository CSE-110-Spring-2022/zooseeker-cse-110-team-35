package edu.ucsd.cse110.zooseeker_team35.location_tracking;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.util.List;
import java.util.Map;


import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooInfoProvider;

public class FindClosestExhibitHelper {

    public static double euclideanDistance(Location latlng1, double latitude, double longitude) {
        return Math.sqrt(Math.pow((latlng1.getLatitude() - latitude), 2)
                + Math.pow((latlng1.getLongitude() - longitude), 2));
    }

    public static ZooData.VertexInfo closestExhibit(Location currentLoc) {
        return closestExhibit(currentLoc, ZooInfoProvider.getVisitableVertexList());
    }

    public static ZooData.VertexInfo closestExhibit(Location currentLoc, List<ZooData.VertexInfo> planedExhibits) {
        double minDistance = Double.MAX_VALUE;
        ZooData.VertexInfo nearestExhibit = null;
        System.out.println(currentLoc);
        for(int i = 0; i < planedExhibits.size(); i++) {
            ZooData.VertexInfo cur = planedExhibits.get(i);
            if (cur.group_id != null) {
                cur = ZooInfoProvider.getVertexWithId(cur.group_id);
            }
            double lat = cur.lat;
            double lng = cur.lng;
            double distance = euclideanDistance(currentLoc, lat, lng);
            if(minDistance >= distance) {
                minDistance = distance;
                nearestExhibit = cur;
            }
        }
        Log.i("Zoo-Seeker-nearest-exhibit", String.format("The closest exhibit is: %s", nearestExhibit.name));
        return nearestExhibit;
    }
}
