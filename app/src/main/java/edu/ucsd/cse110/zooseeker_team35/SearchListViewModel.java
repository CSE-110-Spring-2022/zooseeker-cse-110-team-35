package edu.ucsd.cse110.zooseeker_team35;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SearchListViewModel extends AndroidViewModel {
    private LiveData<List<ZooData.VertexInfo>> searchItems;

    public SearchListViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
    }

    public LiveData<List<ZooData.VertexInfo>> getSearchItems() {
        return searchItems;
    }
}
