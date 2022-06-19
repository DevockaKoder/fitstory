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

public class ObedAdapter extends RecyclerView.Adapter<ObedAdapter.MyViewHolder> {

    Context context;
    ArrayList<MenuObed> list_obed;

    public ObedAdapter(Context context, ArrayList<MenuObed> list_obed) {
        this.context = context;
        this.list_obed = list_obed;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.menu_view, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MenuObed menu = list_obed.get(position);
        holder.type.setText(menu.getType());
        holder.obedName.setText(menu.getName());
        holder.obedCalories.setText(menu.getCalories());
        holder.obedRecipe.setText(menu.getRecipe());

    }

    @Override
    public int getItemCount() {
        return list_obed.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView type, obedName, obedCalories, obedRecipe;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.type);

            obedName = itemView.findViewById(R.id.name);
            obedCalories = itemView.findViewById(R.id.calories);
            obedRecipe = itemView.findViewById(R.id.recipe);

        }
    }
}
