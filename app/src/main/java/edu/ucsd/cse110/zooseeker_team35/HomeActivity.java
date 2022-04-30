package edu.ucsd.cse110.zooseeker_team35;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //TODO: get the list of exhibits that have been selected, ie. Vertex with isClicked=true
        //      and display them in a recyclerView
        //List<Vertex> exhibits = ZooInfoProvider.getSelectedExhibits();

    }

    //functionality when the plan button is clicked
    public void onPlanButtonClicked(View view) {
        Intent intent = new Intent(this, PlanResultsActivity.class);
        startActivity(intent);
    }

    //functionality when the search button is clicked
    public void onSearchButtonClicked(View view) {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        //pass in the searchTerm as an extra to the SearchResultsActivity
        TextView searchTermView = (TextView)findViewById(R.id.search_text);
        String searchTerm = searchTermView.getText().toString();
        intent.putExtra("searchTerm", searchTerm);
        startActivity(intent);
        searchTermView.setText("");
    }
}