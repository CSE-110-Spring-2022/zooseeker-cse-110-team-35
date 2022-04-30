package edu.ucsd.cse110.zooseeker_team35;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

@Database(entities = {ExhibitStatus.class}, version = 1)
public abstract class ExhibitStatusDatabase extends RoomDatabase {
    private static ExhibitStatusDatabase singleton = null;

    public abstract ExhibitStatusDao exhibitStatusDao();

    public synchronized static ExhibitStatusDatabase getSingleton(Context context) {
        if(singleton == null) {
            singleton = ExhibitStatusDatabase.makeDatabase(context);
        }
        return singleton;
    }

    private static ExhibitStatusDatabase makeDatabase(Context context) {
        //Loads all the vertices from the json and creates a list of statuses we will put in the db
        Map<String, ZooData.VertexInfo> vertices = ZooData.loadVertexInfoJSON(context,"sample_node_info.json");
        List<ExhibitStatus> exhibitStatuses = new ArrayList<>();

        //Goes through and adds all exhibits to the list, with default isAdded values false
        for(String id : vertices.keySet()) {
            if(vertices.get(id).kind == ZooData.VertexInfo.Kind.EXHIBIT) {
                exhibitStatuses.add(new ExhibitStatus(id, false));
            }
        }

        //Builds the database by inserting the list of ExhibitStatuses that we just made
        return Room.databaseBuilder(context, ExhibitStatusDatabase.class, "zooseeker_app.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            getSingleton(context).exhibitStatusDao().insertAll(exhibitStatuses);
                        });
                    }
                })
                .build();
    }
}
