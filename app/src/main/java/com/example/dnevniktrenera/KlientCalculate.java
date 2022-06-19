package com.example.dnevniktrenera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class KlientCalculate extends AppCompatActivity {

    /*Пароль, для получения данных, на какую карточку переходить. По имени клиента*/
    private String currentKlientName;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klient_calculate);
        auth = FirebaseAuth.getInstance();

        currentKlientName = getIntent().getExtras().get("klientName").toString();

        init();
    }


    @SuppressLint("NonConstantResourceId")
    public void FragmentsChanges(@NonNull View v) {
        FragmentManager myFm = getSupportFragmentManager();
        @SuppressLint("CommitTransaction") FragmentTransaction myFt = myFm.beginTransaction();

        switch (v.getId()) {
            case R.id.btn_imt:
                /*Передача строки с именем клиента из этой активити во фрагмент (у фрагмента нет интента)*/
                Bundle bundle = new Bundle();
                bundle.putString("klientName", currentKlientName);
                CalculaterIMT activity_calculater_imt = new CalculaterIMT();
                activity_calculater_imt.setArguments(bundle);
                myFt.replace(R.id.fragment, activity_calculater_imt);
                break;
            case R.id.btn_calories:
                Bundle bundleTwo = new Bundle();
                bundleTwo.putString("klientName", currentKlientName);
                CalculaterCalories activity_calculater_calories = new CalculaterCalories();
                activity_calculater_calories.setArguments(bundleTwo);
                myFt.replace(R.id.fragment, activity_calculater_calories);
                break;
            case R.id.btn_tb:
                Bundle bundleThree = new Bundle();
                bundleThree.putString("klientName", currentKlientName);
                CalculaterTB activity_calculater_tb = new CalculaterTB();
                activity_calculater_tb.setArguments(bundleThree);
                myFt.replace(R.id.fragment, activity_calculater_tb);
                break;

        }
        myFt.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.menu_get_photo){
            Intent PhotoIntent = new Intent(KlientCalculate.this, PhotoKlient.class);
            PhotoIntent.putExtra("klientName", currentKlientName);
            startActivity(PhotoIntent);
        }
        if(item.getItemId() == R.id.menu_spisok){
            Intent settingsIntent = new Intent(KlientCalculate.this, KlientsList.class);
            startActivity(settingsIntent);
        }
        if(item.getItemId() == R.id.menu_exit){
            auth.signOut();
            Intent loginIntent = new Intent(KlientCalculate.this, MainActivity.class);
            startActivity(loginIntent);
        }

        return true;
    }

    private void init(){
        Toolbar toolbar = findViewById(R.id.klient_card_bar_layout_calc);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Клиент " + currentKlientName);
    }
}