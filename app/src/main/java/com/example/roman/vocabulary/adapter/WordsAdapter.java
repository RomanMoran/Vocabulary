package com.example.roman.vocabulary.adapter;

import android.view.View;
import android.widget.TextView;

import com.example.roman.vocabulary.R;
import com.example.roman.vocabulary.data.Words;

import java.util.List;

import butterknife.BindView;

/**
 * Created by roman on 23.08.2017.
 */

public class WordsAdapter extends BaseAdapter<Words,WordsAdapter.ViewHolder> {

    public WordsAdapter(List<Words>wordsList){
        super(wordsList);
    }


    @Override
    public void onBindViewHolder(WordsAdapter.ViewHolder holder, int position) {
        holder.tvTranslatable.setText(items.get(position).getWordTranslatable());
        holder.tvTranslated.setText(items.get(position).getWordTranslated());
    }

    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.list_item_words;
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvTranslatable)
        TextView tvTranslatable;
        @BindView(R.id.tvTranslated)
        TextView tvTranslated;

        ViewHolder(BaseViewHolder.OnViewHolderEventListener listener, View itemView) {
            super(listener, itemView);
        }
    }
}
