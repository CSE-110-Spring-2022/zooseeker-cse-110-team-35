package edu.ucsd.cse110.zooseeker_team35;

public interface DirectionFormatStrategy {
    String buildDirection (int directionNumber, String startNode, String endNode, String streetName, double edgeWeight);
}
