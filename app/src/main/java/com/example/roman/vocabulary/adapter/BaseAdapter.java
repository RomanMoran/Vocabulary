package com.example.roman.vocabulary.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 23.08.2017.
 */

public abstract class BaseAdapter<T,VH extends BaseViewHolder> extends RecyclerView.Adapter<VH>
        implements BaseViewHolder.OnViewHolderEventListener {

    public interface OnItemClickListener<T>{
        void onItemClick(View itemView, int position, T item);
    }

    public void setContext(Context mContext){
        this.mContext = mContext;
    }

    protected Context mContext;

    protected List<T> items;

    protected OnItemClickListener<T> onItemClickListener;

    public BaseAdapter(){

    }

    private int position;

    public int getPosition() {return position;}

    public void setPosition(int position) {this.position = position;}

    public BaseAdapter(Context context){
        this.mContext = context;
    }

    public BaseAdapter(List<T>items){
        this.items = items;
    }

    public BaseAdapter(Context context,List<T>items){
        this.mContext = context;
        this.items = items;
    }

    public List<T>getItems(){return items;}

    public void setItems(List<T>items){
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItems(List<T>items){
        if (this.items == null) setItems(items);
        else {
            int startPosition = this.items.size();
            this.items.addAll(items);
            notifyItemRangeInserted(startPosition, items.size());
        }
    }

    public void addItem(T item) {
        if (items == null) items = new ArrayList<T>();
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public boolean updateItem(int position, T newItem) {
        if (position < 0 || position >= getItemCount()) return false;
        items.set(position, newItem);
        notifyItemChanged(position);
        return true;
    }

    public boolean removeItem(T item) {
        return this.items != null && removeItem(items.indexOf(item));
    }

    public boolean removeItem(int position) {
        if (items != null && 0 <= position && position < items.size()) {
            T item = items.remove(position);
            if (item != null) {
                notifyItemRemoved(position);
                return true;
            }
        }
        return false;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressWarnings("unchecked")
        Class<VH> vhClass = (Class<VH>) getParameterType(1, RecyclerView.ViewHolder.class);
        try {
            Constructor<VH> constructor = vhClass.getDeclaredConstructor(this.getClass(),
                    BaseViewHolder.OnViewHolderEventListener.class, View.class);
            constructor.setAccessible(true);
            return constructor.newInstance(this, this, LayoutInflater.from(parent.getContext())
                    .inflate(getItemLayout(viewType), parent, false));
        } catch (Exception e) {
            try {
                Constructor<VH> constructor = vhClass.getDeclaredConstructor(
                        BaseViewHolder.OnViewHolderEventListener.class, View.class);
                constructor.setAccessible(true);
                return constructor.newInstance(this, LayoutInflater.from(parent.getContext())
                        .inflate(getItemLayout(viewType), parent, false));
            } catch (Exception e1) {
                e.printStackTrace();
                e1.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    /*@Override
    public void onBindViewHolder(VH holder, int position) {

    }*/

    @Override
    public void onViewHolderClick(View itemView, int position) {
        if (onItemClickListener == null) return;
        T item = getItem(position);
        if (item != null) onItemClickListener.onItemClick(itemView,position, item);
    }

    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Nullable
    public T getItem(int position) {
        return position < 0 || position >= getItemCount() ? null : items.get(position);
    }

    protected abstract int getItemLayout(int viewType);


    private Type getParameterType(int index, Type baseType) {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            if (index < types.length) return types[index];
        }
        return baseType;
    }
}
