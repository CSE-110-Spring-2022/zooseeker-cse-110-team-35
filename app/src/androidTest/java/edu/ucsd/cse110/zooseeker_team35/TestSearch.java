package edu.ucsd.cse110.zooseeker_team35;

import android.content.Intent;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class TestSearch {

    @Test
    public void testValidNameSearch() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(),
                SearchResultsActivity.class);
        intent.putExtra("searchTerm", "Gorillas");
        ActivityScenario<SearchResultsActivity> scenario = ActivityScenario.launch(intent);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.recyclerView;
            RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(0);
            assertNotNull(firstVH);

            TextView resultView = firstVH.itemView.findViewById(R.id.search_item_text);
            String resultText = resultView.getText().toString();
            assertEquals(resultText, "Gorillas");
        });
    }

    @Test
    public void testInvalidNameSearch() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(),
                SearchResultsActivity.class);
        intent.putExtra("searchTerm", "Dinosaurs");
        ActivityScenario<SearchResultsActivity> scenario = ActivityScenario.launch(intent);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.recyclerView;
            TextView noResultsView = activity.findViewById(R.id.no_results_msg);
            RecyclerView.ViewHolder firstVH = recyclerView.findViewHolderForAdapterPosition(0);
            assertEquals(noResultsView.getVisibility(), 0);

            assertNull(firstVH);
        });
    }
}
