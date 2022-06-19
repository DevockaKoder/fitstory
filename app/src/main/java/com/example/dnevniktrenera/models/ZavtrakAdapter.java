package com.example.dnevniktrenera.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dnevniktrenera.R;

import java.util.ArrayList;

public class ZavtrakAdapter extends RecyclerView.Adapter<ZavtrakAdapter.MyViewHolder> {

    Context context;
    ArrayList<MenuZavtrak> list;

    public ZavtrakAdapter(Context context, ArrayList<MenuZavtrak> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.menu_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MenuZavtrak menu = list.get(position);
        holder.dayWeek.setVisibility(View.VISIBLE);
        holder.dayWeek.setText(menu.getDayWeek());
        holder.type.setText(menu.getType());
        holder.zavtrakName.setText(menu.getName());
        holder.zavtrakCalories.setText(menu.getCalories());
        holder.zavtrakRecipe.setText(menu.getRecipe());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView dayWeek, type, zavtrakName, zavtrakCalories, zavtrakRecipe;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dayWeek = itemView.findViewById(R.id.day_week);
            type = itemView.findViewById(R.id.type);
            zavtrakName = itemView.findViewById(R.id.name);
            zavtrakCalories = itemView.findViewById(R.id.calories);
            zavtrakRecipe = itemView.findViewById(R.id.recipe);
        }
    }
}
