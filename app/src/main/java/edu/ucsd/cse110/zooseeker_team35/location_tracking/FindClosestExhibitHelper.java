package edu.ucsd.cse110.zooseeker_team35.location_tracking;

import android.location.Location;

import java.util.List;


import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;

public class FindClosestExhibitHelper {

    public static double euclideanDistance(Location latlng1, double latitude, double longitude) {
        return Math.sqrt(Math.pow((latlng1.getLatitude() - latitude), 2)
                + Math.pow((latlng1.getLongitude() - longitude), 2));
    }

    /*public static ZooData.VertexInfo closestExhibit(Location currentLoc, List<ZooData.VertexInfo> vertexes) {
        double minDistance = Double.MAX_VALUE;
        ZooData.VertexInfo closestVertex = null;
        for(int i = 0; i < vertexes.size(); i++) {
            double lat = vertexes.get(i).lat;
            double lng = vertexes.get(i).lng;
            double distance = euclideanDistance(currentLoc, lat, lng);
            if(minDistance >= distance) {
                minDistance = distance;
                closestVertex = vertexes.get(i);
            }
        }
        return closestVertex;
    }*/
}
