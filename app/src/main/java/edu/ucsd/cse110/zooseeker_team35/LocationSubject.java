package edu.ucsd.cse110.zooseeker_team35;

public interface LocationSubject {
    void registerObserver(LocationObserver observer);
    void removeObserver(LocationObserver observer);
    void notifyObservsers();
}
