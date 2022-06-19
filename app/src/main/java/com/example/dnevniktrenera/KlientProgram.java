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


public class KlientProgram extends AppCompatActivity {
    /*Пароль, для получения данных, на какую карточку переходить. По имени клиента*/
    private String currentKlientName;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klient_program);
        auth = FirebaseAuth.getInstance();

        currentKlientName = getIntent().getExtras().get("klientName").toString();

        init();
    }


    @SuppressLint("NonConstantResourceId")
    public void FragmentsChanges(@NonNull View v) {
        FragmentManager myFm = getSupportFragmentManager();
        @SuppressLint("CommitTransaction") FragmentTransaction myFt = myFm.beginTransaction();

        switch (v.getId()) {
            case R.id.btn_day1:
                /*Передача строки с именем клиента из этой активити во фрагмент (у фрагмента нет интента)*/
                Bundle bundle = new Bundle();
                bundle.putString("klientName", currentKlientName);
                ProgramFirstDay activity_program_first_day = new ProgramFirstDay();
                activity_program_first_day.setArguments(bundle);
                myFt.replace(R.id.fragment, activity_program_first_day );
                break;
            case R.id.btn_day2:
                Bundle bundleTwo = new Bundle();
                bundleTwo.putString("klientName", currentKlientName);
                ProgramTwoDay activity_program_two_day = new ProgramTwoDay();
                activity_program_two_day.setArguments(bundleTwo);
                myFt.replace(R.id.fragment, activity_program_two_day);
                break;
            case R.id.btn_day3:
                Bundle bundleThree = new Bundle();
                bundleThree.putString("klientName", currentKlientName);
                ProgramThreeDay activity_program_three_day = new ProgramThreeDay();
                activity_program_three_day.setArguments(bundleThree);
                myFt.replace(R.id.fragment, activity_program_three_day);
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
            Intent PhotoIntent = new Intent(KlientProgram.this, PhotoKlient.class);
            PhotoIntent.putExtra("klientName", currentKlientName);
            startActivity(PhotoIntent);
        }
        if(item.getItemId() == R.id.menu_spisok){
            Intent settingsIntent = new Intent(KlientProgram.this, KlientsList.class);
            startActivity(settingsIntent);
        }
        if(item.getItemId() == R.id.menu_do_program){
            Intent ProgramIntent = new Intent(KlientProgram.this, KlientProgram.class);
            ProgramIntent.putExtra("klientName", currentKlientName);
            startActivity(ProgramIntent);
        }
        if(item.getItemId() == R.id.menu_do_menu){
            Intent MenuIntent = new Intent(KlientProgram.this, NutritionProgram.class);
            MenuIntent.putExtra("klientName", currentKlientName);
            startActivity(MenuIntent);
        }
        if(item.getItemId() == R.id.menu_exit){
            auth.signOut();
            Intent loginIntent = new Intent(KlientProgram.this, MainActivity.class);
            startActivity(loginIntent);
        }

        return true;
    }

    private void init(){
        Toolbar toolbar = findViewById(R.id.klient_card_bar_layout_pr);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Клиент " + currentKlientName);
    }
}