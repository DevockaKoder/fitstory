package com.example.dnevniktrenera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class KlientsList extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> listOfKlients = new ArrayList<>();
    private String TrenerId;
    private DatabaseReference KlientsRef;
    private FloatingActionButton newKlient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klients_list);
        TrenerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        KlientsRef = FirebaseDatabase.getInstance().getReference("Users").child(TrenerId).child("Klients");

        listView = findViewById(R.id.list_klient);
        arrayAdapter = new ArrayAdapter<String>(KlientsList.this, R.layout.list_klient_item, listOfKlients);
        listView.setAdapter(arrayAdapter);
        newKlient = findViewById(R.id.new_klient);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String currentKlientName = adapterView.getItemAtPosition(position).toString();
                Intent cardKlientIntent = new Intent(KlientsList.this, CardKlientActivity.class);
                cardKlientIntent.putExtra("klientName", currentKlientName);
                startActivity(cardKlientIntent);
            }
        });

        RetrieveAndShowKlients();
        OnBtnClick();

    }

    private void OnBtnClick() {
        newKlient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(KlientsList.this, NewKlientActivity.class);
                startActivity(intent);
            }
        });
    }

    private void RetrieveAndShowKlients() {

        KlientsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set = new HashSet<>();

                Iterator iterator = snapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }
                listOfKlients.clear();
                listOfKlients.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}