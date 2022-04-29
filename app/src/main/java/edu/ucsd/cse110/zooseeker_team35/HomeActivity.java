package edu.ucsd.cse110.zooseeker_team35;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }

    public void onPlanButtonClicked(View view) {
        Intent intent = new Intent(this, PlanResultsActivity.class);
        startActivity(intent);
    }

    public void onSearchButtonClicked(View view) {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        String searchTerm = "";
        intent.putExtra("searchTerm", searchTerm);
        startActivity(intent);
    }
}