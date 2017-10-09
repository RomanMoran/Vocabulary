package com.example.roman.vocabulary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.roman.vocabulary.base_mvp.BaseMvp;

import butterknife.ButterKnife;

/**
 * Created by roman on 23.08.2017.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements BaseMvp.View {

    protected final Context context;

    public BaseViewHolder(OnViewHolderEventListener listener,View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        context = itemView.getContext();
        itemView.setOnClickListener(v -> {
        if (listener!=null) listener.onViewHolderClick(itemView,getAdapterPosition());
        });
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProgressIndicator(boolean active) {

    }

    public interface OnViewHolderEventListener{
        void onViewHolderClick(View itemView,int position);
    }

}
