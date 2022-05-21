package edu.ucsd.cse110.zooseeker_team35;

import java.util.List;

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
