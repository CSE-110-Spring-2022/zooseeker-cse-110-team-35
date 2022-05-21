package edu.ucsd.cse110.zooseeker_team35.location_tracking;

import java.util.List;

import edu.ucsd.cse110.zooseeker_team35.location_tracking.LocationProvider;
import edu.ucsd.cse110.zooseeker_team35.path_finding.ZooData;

//TODO: implement the ZooLiveMap class
public class ZooLiveMap{
    LocationProvider userLocationProvider;

    public ZooLiveMap(LocationProvider userLocationProvider) {
        this.userLocationProvider = userLocationProvider;
    }

    //TODO: go through the vertex data and find the closest vertex
    public ZooData.VertexInfo getClosestVertexToUser(List<ZooData.VertexInfo> vertexes) {
        return null;
    }

    public boolean hasLiveData() {
        return false;
    }
}
