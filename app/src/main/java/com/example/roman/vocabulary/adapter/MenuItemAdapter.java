package com.example.roman.vocabulary.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.roman.vocabulary.R;
import com.example.roman.vocabulary.data.MenuItem;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by roman on 23.08.2017.
 */

public class MenuItemAdapter extends BaseAdapter<MenuItem, MenuItemAdapter.ViewHolder> {


    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.list_item_menu;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvMenuDescr.setText(items.get(position).getIdTitleString());
        Glide.with(holder.context).load(items.get(position).getIdImageDrawable()).into(holder.imgMenuIcon);
        //holder.imgMenuIcon.setBackgroundResource(items.get(position).getIdImageDrawable());
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.imgMenuIcon)
        ImageView imgMenuIcon;
        @BindView(R.id.tvMenuDescr)
        TextView tvMenuDescr;

        ViewHolder(OnViewHolderEventListener listener, View itemView) {
            super(listener, itemView);
        }
    }
}
