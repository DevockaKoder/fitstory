package com.example.dnevniktrenera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SpisokActivity extends AppCompatActivity {

    private AppCompatImageButton to_new_client, spisok_klients, to_rasp, list_klients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spisok);
        OnBtnClick();
    }

    public void OnBtnClick() {
        this.to_new_client = findViewById(R.id.toNewClientBtn);
        this.spisok_klients = findViewById(R.id.toSpisokKlientsBtn);
        this.to_rasp = findViewById(R.id.toRasp);
        this.list_klients = findViewById(R.id.list_of_klients);

        to_new_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(SpisokActivity.this, NewKlientActivity.class);
                startActivity(intent);
            }
        });

        spisok_klients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(SpisokActivity.this, ScrollingKlientsActivity.class);
                startActivity(intent);
            }
        });

        to_rasp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(SpisokActivity.this, KalendarActivity.class);
                startActivity(intent);
            }
        });

        list_klients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(SpisokActivity.this, KlientsList.class);
                startActivity(intent);
            }
        });

    }
}