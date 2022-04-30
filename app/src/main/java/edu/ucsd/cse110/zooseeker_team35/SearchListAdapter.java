package edu.ucsd.cse110.zooseeker_team35;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    private List<ZooData.VertexInfo> searchItems = Collections.emptyList();

    public void setSearchItems(List<ZooData.VertexInfo> newSearchItems) {
        this.searchItems.clear();
        this.searchItems = newSearchItems;
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.search_result_item, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setSearchItem(searchItems.get(position));
    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    public String getId(int position) { return searchItems.get(position).id; }

/*
    public void addExhibit(ZooData.VertexInfo exhibit) {
        searchItems.add(exhibit);
        notifyItemInserted(searchItems.size() - 1);
    }

 */

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private ZooData.VertexInfo searchItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.search_item_text);
        }

        public ZooData.VertexInfo getSearchItem() {return searchItem;}

        public void setSearchItem(ZooData.VertexInfo searchItem) {
            this.searchItem = searchItem;
            this.textView.setText(searchItem.name);
        }
    }

}
