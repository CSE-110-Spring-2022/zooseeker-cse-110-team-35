package edu.ucsd.cse110.zooseeker_team35;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Map;

public class SearchResultsActivity extends AppCompatActivity {
    private Map<String, ZooData.VertexInfo> exhibits;
    private Button searchBtn2;
    private EditText searchBar;
    private TextView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        //initialize buttons/search bar
        this.searchBtn2 = this.findViewById(R.id.search_btn_2);
        searchBtn2.setOnClickListener(this::onSearchButton2Clicked);
        this.searchBar = this.findViewById(R.id.search_bar_2);
        this.backButton = this.findViewById(R.id.back_btn);
        backButton.setOnClickListener(this::onBackButtonClicked);


        //TODO: use the search term as a query and get the list of exhibits
        //      and display the exhibits in a recyler view
        Bundle extra = getIntent().getExtras();
        String searchTerm = extra.getString("searchTerm");

        this.exhibits = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");

        ZooData.VertexInfo searchResult = exhibits.get(searchTerm);
        Log.d("searchTerm", "The Search Term is: " + searchTerm);
        searchBar.setText(searchTerm);
        if(searchResult == null) {
            TextView no_result =(TextView)findViewById(R.id.no_results_msg);
            no_result.setVisibility(View.VISIBLE);
        }

    }

    void onSearchButton2Clicked(View view){
        //sets no result message to invisible in case the search result pulls something
        TextView noResultsFound = this.findViewById(R.id.no_results_msg);
        noResultsFound.setVisibility(View.INVISIBLE);

        //searches for new search result
        //TODO grab search from bar
        String searchTerm = searchBar.getText().toString();
        ZooData.VertexInfo searchResult = exhibits.get(searchTerm);
        Log.d("searchTerm", "The Search Term is: " + searchTerm);
        //sets no result back to visible if not found
        if(searchResult == null) {
            TextView no_result =(TextView)findViewById(R.id.no_results_msg);
            no_result.setVisibility(View.VISIBLE);
        }
        else{
            this.displaySearchResult(searchResult);
        }
    }

    void onBackButtonClicked(View view){
        finish();
    }

    void displaySearchResult(ZooData.VertexInfo exhibit){
        //TODO: Implement search result display
    }
}