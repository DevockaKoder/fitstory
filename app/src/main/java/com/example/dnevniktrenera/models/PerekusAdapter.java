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

public class PerekusAdapter extends RecyclerView.Adapter<PerekusAdapter.MyViewHolder> {

    Context context;
    ArrayList<MenuPerekus> list_perekus;


    public PerekusAdapter(Context context, ArrayList<MenuPerekus> list) {
        this.context = context;
        this.list_perekus = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.menu_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MenuPerekus menu = list_perekus.get(position);
        holder.type.setText(menu.getType());

        holder.perekusName.setText(menu.getName());
        holder.perekusCalories.setText(menu.getCalories());
        holder.perekusRecipe.setText(menu.getRecipe());
    }

    @Override
    public int getItemCount() {
        return list_perekus.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView type, perekusName, perekusCalories, perekusRecipe;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.type);

            perekusName = itemView.findViewById(R.id.name);
            perekusCalories = itemView.findViewById(R.id.calories);
            perekusRecipe = itemView.findViewById(R.id.recipe);

        }
    }
}
