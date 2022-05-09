package edu.ucsd.cse110.zooseeker_team35;
public class ProceedDirectionFormat implements DirectionFormatStrategy{

    @Override
    public String buildDirection(int directionNumber, String startNode, String endNode, String streetName, double edgeWeight) {
        String pathInfo = String.format("  %d. Proceed on %s %.0f ft from '%s' towards '%s'.\n",
                directionNumber,
                streetName,
                edgeWeight,
                startNode,
                endNode);
        return pathInfo;
    }
}
