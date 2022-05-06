package edu.ucsd.cse110.zooseeker_team35;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<ZooData.VertexInfo> exhibits;
    private ExhibitsAdapter adapter;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //TODO: get the list of exhibits that have been selected, ie. Vertex with isClicked=true
        //      and display them in a recyclerView
        exhibits = ZooInfoProvider.getSelectedExhibits(getApplicationContext());
        ExhibitListViewModel viewModel = new ViewModelProvider(this)
                .get(ExhibitListViewModel.class);
        adapter = new ExhibitsAdapter();
        //viewModel.getExhibits().observe(this, adapter::setExhibits);
        adapter.setHasStableIds(true);
        recyclerView = findViewById(R.id.added_exhibits_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        tv = (TextView) findViewById(R.id.no_exhibit);
        //trying to display the message: "No Exhibit is added when the recyclerview is empty but this crashes the app"
        updateDisplay();
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

    @Override
    protected void onResume() {
        super.onResume();
        exhibits = ZooInfoProvider.getSelectedExhibits(getApplicationContext());
        updateDisplay();
        System.out.println("resumed: " + exhibits.size());
    }

    public void updateDisplay() {
        if (exhibits.isEmpty()) {
            tv.setVisibility(View.VISIBLE);
        }
        else {
            tv.setVisibility(View.INVISIBLE);
        }
        adapter.setExhibits(exhibits);
    }

}