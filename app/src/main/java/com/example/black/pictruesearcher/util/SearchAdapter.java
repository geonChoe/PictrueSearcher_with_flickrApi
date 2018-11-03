package com.example.black.pictruesearcher.util;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.black.pictruesearcher.R;
import com.example.black.pictruesearcher.activity.DetailActivity;
import com.example.black.pictruesearcher.model.SearchItem;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context context;
    private List<SearchItem> list;

    public SearchAdapter(Context context, List<SearchItem> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.search_item_item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SearchItem searchItem = list.get(position);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("item", searchItem);
                context.startActivity(intent);
            }
        });
        Glide.with(context).load(searchItem.getUrl()).thumbnail(0.1f).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<SearchItem> list2) {
        list.addAll(list2);
    }

    public void clear() {
        list.clear();
    }
}