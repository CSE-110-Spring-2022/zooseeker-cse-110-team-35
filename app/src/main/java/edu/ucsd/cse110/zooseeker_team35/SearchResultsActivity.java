package edu.ucsd.cse110.zooseeker_team35;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Bundle extra = getIntent().getExtras();
        String searchTerm = extra.getString("searchTerm");
    }
}