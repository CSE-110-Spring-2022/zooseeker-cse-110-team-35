package edu.ucsd.cse110.zooseeker_team35;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchResultsActivity extends AppCompatActivity {
    private Map<String, ZooData.VertexInfo> exhibits;
    private List<ZooData.VertexInfo> exhibitResults;
    private Button searchBtn2;
    private EditText searchBar;
    private TextView backButton;
    private SearchListViewModel viewModel;
    public RecyclerView recyclerView;
    public SearchListAdapter adapter;

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

        //initialize recycler/adapter
        this.adapter = new SearchListAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.search_item_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Initializes ArrayList which will hold the searched exhibits that are to be displayed
        this.exhibitResults = new ArrayList<>();

        //Grabs the search term, and normalizes it to all lower case
        Bundle extra = getIntent().getExtras();
        String searchTerm = extra.getString("searchTerm").toLowerCase();

        this.exhibits = ZooData.loadVertexInfoJSON(this, ZooInfoProvider.nodeInfoJSON);

        displaySearchResult(searchTerm);
    }

    void onSearchButton2Clicked(View view){

        //sets no result message to invisible in case the search result pulls something
        TextView noResultsFound = this.findViewById(R.id.no_results_msg);
        noResultsFound.setVisibility(View.INVISIBLE);

        //searches for new search result
        String searchTerm = searchBar.getText().toString();
        //sets no result back to visible if not found
        getIntent().putExtra("searchTerm", searchTerm);
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    void onBackButtonClicked(View view){
        finish();
    }

    void displaySearchResult(String searchTerm) {
        //remove other search results first
        exhibitResults.clear();


        for (ZooData.VertexInfo exhibit : exhibits.values()) {
            boolean isExhibit = (exhibit.kind == ZooData.VertexInfo.Kind.EXHIBIT);
            if (exhibit.name.toLowerCase().contains(searchTerm) && isExhibit) {
                exhibitResults.add(exhibit);
            }
            else {
                for(String tag : exhibit.tags) {
                    if(tag.toLowerCase().contains(searchTerm) && isExhibit) {
                        exhibitResults.add(exhibit);
                        break;
                    }
                }
            }
        }

        if(exhibitResults.isEmpty()) {
            searchFail();
        }
        else {
            adapter.setSearchItems(exhibitResults);
            searchBar.setText(searchTerm);
        }
    }

    void searchFail(){
        exhibitResults.clear();
        adapter.notifyDataSetChanged();
        TextView no_result = (TextView)findViewById(R.id.no_results_msg);
        no_result.setVisibility(View.VISIBLE);
    }
}