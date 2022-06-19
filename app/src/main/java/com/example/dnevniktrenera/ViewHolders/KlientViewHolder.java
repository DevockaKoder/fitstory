package com.example.dnevniktrenera.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dnevniktrenera.Interface.ItemClickListner;
import com.example.dnevniktrenera.R;

public class KlientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtKlientName, txtKlientAge, txtKlientWeight, txtKlientGrowth, txtKlientPhone;
    public ImageView imageViewOne, imageViewTwo, imageViewThree;
    public ItemClickListner listner;


    public KlientViewHolder(View itemView)
    {
        super(itemView);


        imageViewOne = itemView.findViewById(R.id.klient_image_one);
        imageViewTwo = itemView.findViewById(R.id.klient_image_two);
        imageViewThree = itemView.findViewById(R.id.klient_image_three);
        txtKlientName = itemView.findViewById(R.id.klient_items_name);
        txtKlientAge = itemView.findViewById(R.id.klient_items_age);
        txtKlientWeight = itemView.findViewById(R.id.klient_items_weight);
        txtKlientGrowth = itemView.findViewById(R.id.klient_items_growth);
        txtKlientPhone = itemView.findViewById(R.id.klient_items_phone);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}
