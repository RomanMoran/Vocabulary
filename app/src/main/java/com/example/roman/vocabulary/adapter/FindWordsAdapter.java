package com.example.roman.vocabulary.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;

import com.example.roman.vocabulary.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by roman on 16.09.2017.
 */

public class FindWordsAdapter extends BaseAdapter<String,FindWordsAdapter.ViewHolder> {

    public SparseBooleanArray selectedItems;
    public List<String> strings ;

    public FindWordsAdapter(List<String> words) {
        this.items = words;
        selectedItems = new SparseBooleanArray(items.size());
        strings = new ArrayList<>();
    }

    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.list_items_found;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (selectedItems.get(position)){
            holder.itemView.setBackgroundColor(Color.parseColor("#e74c3c"));
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            //strings.remove(position);
        }

        holder.itemView.setOnClickListener(v ->{
            //setPosition(holder.getAdapterPosition());
            if (selectedItems.get(position)) {
                selectedItems.put(position,false);
            }else {
                selectedItems.put(position,true);
            }
            notifyDataSetChanged();
        });
        holder.tvFound.setText(items.get(position));
    }

    public class ViewHolder extends BaseViewHolder{
        @BindView(R.id.tvFound)
        TextView tvFound;
        public ViewHolder(OnViewHolderEventListener listener, View itemView) {
            super(listener, itemView);
        }
    }
}
