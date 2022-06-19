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

public class DinnerAdapter extends RecyclerView.Adapter<DinnerAdapter.MyViewHolder> {

    Context context;
    ArrayList<MenuDinner> list_dinner;


    public DinnerAdapter(Context context, ArrayList<MenuDinner> list) {
        this.context = context;
        this.list_dinner = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.menu_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MenuDinner menu = list_dinner.get(position);
        holder.type.setText(menu.getType());

        holder.dinnerName.setText(menu.getName());
        holder.dinnerCalories.setText(menu.getCalories());
        holder.dinnerRecipe.setText(menu.getRecipe());
    }

    @Override
    public int getItemCount() {
        return list_dinner.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView type, dinnerName, dinnerCalories, dinnerRecipe;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.type);

            dinnerName = itemView.findViewById(R.id.name);
            dinnerCalories = itemView.findViewById(R.id.calories);
            dinnerRecipe = itemView.findViewById(R.id.recipe);

        }
    }
}