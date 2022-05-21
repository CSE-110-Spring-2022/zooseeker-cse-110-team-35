package edu.ucsd.cse110.zooseeker_team35.location_tracking;

import edu.ucsd.cse110.zooseeker_team35.location_tracking.LocationObserver;

public interface LocationSubject {
    void registerObserver(LocationObserver observer);
    void removeObserver(LocationObserver observer);
    void notifyObservsers();
}
