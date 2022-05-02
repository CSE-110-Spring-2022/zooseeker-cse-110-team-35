package edu.ucsd.cse110.zooseeker_team35;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExhibitListViewModel extends AndroidViewModel {
    private LiveData<List<ZooData.VertexInfo>> exhibits;
    //private final ExhibitStatusDao exhibitStatusDao;
    //private Map<String, ZooData.VertexInfo> vertices;

    public ExhibitListViewModel(@NonNull Application application) {
        super(application);
        Context context = application.getApplicationContext();
        /*//List<ZooData.VertexInfo> exhibits = ZooInfoProvider.getSelectedExhibits(context);
        ExhibitStatusDatabase db = ExhibitStatusDatabase.getSingleton(context);
        exhibitStatusDao = db.exhibitStatusDao();
        List<ExhibitStatus> added = exhibitStatusDao.getAdded(true);
        List<ZooData.VertexInfo> addedVertices = new ArrayList<>();
        vertices = ZooData.loadVertexInfoJSON(context,"sample_node_info.json");
        for(ExhibitStatus e : added) {
           ZooData.VertexInfo v = vertices.get(e.getId());
            addedVertices.add(v);
        }
        exhibits = new MutableLiveData<List<ZooData.VertexInfo>>(); {
        }*/
    }

    public LiveData<List<ZooData.VertexInfo>> getExhibits() {
        return exhibits;
    }
}

