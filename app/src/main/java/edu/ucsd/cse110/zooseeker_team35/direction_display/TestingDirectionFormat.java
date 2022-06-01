package edu.ucsd.cse110.zooseeker_team35.direction_display;

public class TestingDirectionFormat implements DirectionFormatStrategy{
    @Override
    public String buildDirection(int directionNumber, String startNode, String endNode, String streetName, double edgeWeight) {
        String pathInfo = startNode + "->" + endNode + ":" + streetName;
        return pathInfo;
    }
}
