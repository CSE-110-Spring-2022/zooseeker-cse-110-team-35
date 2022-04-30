package edu.ucsd.cse110.zooseeker_team35;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Map;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        //TODO: use the search term as a query and get the list of exhibits
        //      and display the exhibits in a recyler view
        Bundle extra = getIntent().getExtras();
        String searchTerm = extra.getString("searchTerm");

        Map<String, ZooData.VertexInfo> exhibits =
                ZooData.loadVertexInfoJSON(this, "sample_node_info.json");

        ZooData.VertexInfo searchResult = exhibits.get(searchTerm);
        if(searchResult == null) {
            //Todo: Set "No results" view to visible
            return;
        }
    }
}