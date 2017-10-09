package com.example.roman.vocabulary.adapter;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.roman.vocabulary.R;
import com.example.roman.vocabulary.data.Words;
import com.example.roman.vocabulary.utilities.Circle;

import java.util.List;

import butterknife.BindView;

/**
 * Created by roman on 30.08.2017.
 */

public class LearnWordsAdapter extends BaseAdapter<Words, LearnWordsAdapter.ViewHolder> {

    public SparseBooleanArray selectedItems;
    public SparseBooleanArray selectedItemsBd;
    private String lang = "";

    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.list_item_words;
    }

    protected onCheckedListener onCheckedListener;

    public interface onCheckedListener {
        void getItemChecked(int position,boolean checked);
    }

    public void setOnCheckedListener(onCheckedListener onCheckedListener){
        this.onCheckedListener = onCheckedListener;
    }

    public LearnWordsAdapter(List<Words> items) {
        super(items);
        selectedItems = new SparseBooleanArray(items.size());
        selectedItemsBd = new SparseBooleanArray(items.size());
    }

    @Override
    public void setItems(List<Words> items) {
        selectedItems = new SparseBooleanArray(items.size());
        selectedItemsBd = new SparseBooleanArray(items.size());
        super.setItems(items);
    }

    public void setItems(List<Words> items,String lang) {
        this.lang = lang;
        selectedItems = new SparseBooleanArray(items.size());
        selectedItemsBd = new SparseBooleanArray(items.size());
        super.setItems(items);
    }

    public LearnWordsAdapter() {

    }

    public LearnWordsAdapter(String lang, List<Words> items) {
        super(items);
        this.lang = lang;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Words word = items.get(position);

        if (selectedItems != null) {
            if (selectedItems.get(position)) {
                holder.itemView.setBackgroundColor(Color.parseColor("#e74c3c"));
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            selectedItemsBd.put((int) word.getId() - 1, selectedItems.get(position));
        }

        if (lang.equals("")) {
            holder.tvTranslated.setText(word.getWordRu());
            holder.tvTranslatable.setText(word.getWordEn());
        }

        if (lang.equals("RU")) {
            holder.tvTranslated.setText(word.getWordRu());
            holder.tvTranslatable.setText("");
        }

        if (lang.equals("EN")) {
            holder.tvTranslatable.setText(word.getWordEn());
            holder.tvTranslated.setText("");
        }
        //holder.tvTranslated.setText(word.getWordRu());
        //holder.tvTranslatable.setText(word.getWordEn());
        holder.itemView.setOnClickListener(v -> {
            //setPosition(holder.getAdapterPosition());
            if (selectedItems.get(position)) {
                selectedItems.put(position, false);
            } else {
                selectedItems.put(position, true);
            }
            notifyDataSetChanged();
        });
        holder.circle.setAngle(items.get(position).getReveal() * 90);

        holder.itemView.setOnLongClickListener(v -> {
            setPosition(holder.getAdapterPosition());
            return false;
        });
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvTranslatable)
        TextView tvTranslatable;
        @BindView(R.id.tvTranslated)
        TextView tvTranslated;
        @BindView(R.id.circle)
        Circle circle;

        public ViewHolder(OnViewHolderEventListener listener, View itemView) {
            super(listener, itemView);
            itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
                menu.setHeaderTitle(R.string.select_action);
                menu.add(Menu.NONE, R.id.menu_edit, Menu.NONE, R.string.edit);
                menu.add(Menu.NONE, R.id.menu_delete, Menu.NONE, R.string.delete);
                menu.add(Menu.NONE, R.id.menu_forgot, Menu.NONE, R.string.forgot);
            });
            itemView.setOnClickListener(view -> onCheckedListener.getItemChecked(getAdapterPosition(),selectedItems.get(getAdapterPosition())));
        }
    }
}
