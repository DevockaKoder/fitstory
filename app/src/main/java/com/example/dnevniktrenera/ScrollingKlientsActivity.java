package com.example.dnevniktrenera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dnevniktrenera.ViewHolders.KlientViewHolder;
import com.example.dnevniktrenera.models.Klients;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ScrollingKlientsActivity extends AppCompatActivity {

    DatabaseReference KlientsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String TrenerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_klients);

        TrenerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        KlientsRef = FirebaseDatabase.getInstance().getReference("Users").child(TrenerId).child("Klients");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(ScrollingKlientsActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        };

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Klients> options = new FirebaseRecyclerOptions.Builder<Klients>()
                .setQuery(KlientsRef, Klients.class).build();
        FirebaseRecyclerAdapter<Klients, KlientViewHolder> adapter = new FirebaseRecyclerAdapter<Klients, KlientViewHolder>(options){

            @NonNull
            @Override
            public KlientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.klient_items_layout, parent, false);
                KlientViewHolder holder = new KlientViewHolder(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull KlientViewHolder holder, int position, @NonNull Klients model) {
                holder.txtKlientName.setText(model.getName());
                holder.txtKlientAge.setText("Возраст: " + model.getAge());
                holder.txtKlientWeight.setText("Вес: " + model.getWeight() + " кг");
                holder.txtKlientGrowth.setText("Рост: " + model.getGrowth() + " см");
                holder.txtKlientPhone.setText("Номер: " + model.getPhone());

                Picasso.get().load(model.getImageOne()).into(holder.imageViewOne);
                Picasso.get().load(model.getImageTwo()).into(holder.imageViewTwo);
                Picasso.get().load(model.getImageThree()).into(holder.imageViewThree);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        };

}