package edu.ucsd.cse110.zooseeker_team35;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.*;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import static org.junit.Assert.*;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class SearchTester {

    //Checks when user searches up an exhibit that doesn't exist
    @Test
    public void invalidSearchTest() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(),
                SearchResultsActivity.class);
        intent.putExtra("searchResult", "Dinosaurs");

        try(ActivityScenario<SearchResultsActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                TextView searchText = activity.findViewById(R.id.no_results_msg);
                assertEquals(searchText.getVisibility(), 0);
                scenario.close();
            });
        }
    }

    @Test
    public void validSearchTest() {
        //Intent intent = new Intent(SearchResultsActivity.class);
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(),
                SearchResultsActivity.class);
        intent.putExtra("searchResult", "gorillas");
        try(ActivityScenario<SearchResultsActivity> scenario =
                    ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> {
                SearchListAdapter adapter = new SearchListAdapter();
                Map<String, ZooData.VertexInfo> exhibits =
                        ZooData.loadVertexInfoJSON(activity, "sample_node_info.json");
                ZooData.VertexInfo searchResult = exhibits.get("gorillas");
                List<ZooData.VertexInfo> exhibitResults = new ArrayList<>();
                exhibitResults.add(searchResult);
                adapter.setSearchItems(exhibitResults);
                assertEquals(1, adapter.getItemCount());
                assertEquals(adapter.getId(0), "gorillas");
            });
        }
    }

}
