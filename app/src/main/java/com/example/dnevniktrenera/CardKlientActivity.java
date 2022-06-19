package com.example.dnevniktrenera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;


public class CardKlientActivity extends AppCompatActivity {
    private TextView age, weight, growth, phone, tb, imt;
    private EditText notes;
    /*Пароль, для получения данных, на какую карточку переходить. По имени клиента*/
    private String currentKlientName;
    private FirebaseAuth auth;
    private DatabaseReference KlientRef;
    private AppCompatImageButton btnProgram, btnCalc, btnMenu;
    private Button btnSaveNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_klient);
        currentKlientName = getIntent().getExtras().get("klientName").toString();
        auth = FirebaseAuth.getInstance();
        String trenerId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        KlientRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(trenerId).child("Klients").child(currentKlientName).child("KlientInfo");


        btnProgram = findViewById(R.id.btnProgram);
        btnCalc = findViewById(R.id.btnCalc);
        btnMenu = findViewById(R.id.btnMenu);
        btnSaveNotes = findViewById(R.id.btn_save_card);
        age = findViewById(R.id.card_items_age);
        weight = findViewById(R.id.card_items_weight);
        growth = findViewById(R.id.card_items_growth);
        phone = findViewById(R.id.card_items_phone);
        tb = findViewById(R.id.card_tb);
        imt = findViewById(R.id.card_imt);

        notes = findViewById(R.id.edit_text_klient);


        init();
        OnButtonClick();
        SetTextToTextView();
        SetTextToNotes();
    }

    private void SetTextToNotes() {
        KlientRef.child("notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.getValue()) != null) {
                    notes.setText(snapshot.getValue().toString());
                } else {
                    notes.setHint("Введите...");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                notes.setHint("Введите...");
            }
        });
    }

    private void saveNotes() {
        String text = notes.getText().toString();
        HashMap<String, Object> map = new HashMap();
        map.put("notes", text);
        KlientRef.updateChildren(map).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(CardKlientActivity.this, "Заметка сохранена", Toast.LENGTH_SHORT).show();
            }
            else {
                String message = Objects.requireNonNull(task.getException()).toString();
                Toast.makeText(CardKlientActivity.this, "Ошибка: "+ message, Toast.LENGTH_SHORT).show();
            }
        });;
    }

    private void SetTextToTextView() {
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String textAge = snapshot.child("age").getValue(String.class);
                String textWeight = snapshot.child("weight").getValue(String.class);
                String textGrowth  = snapshot.child("growth").getValue(String.class);
                String textPhone = snapshot.child("phone").getValue(String.class);
                String textImt = snapshot.child("Показатель ИМТ").getValue(String.class);
                String textTb = snapshot.child("Индекс тб").getValue(String.class);


                age.setText("Рост: " + textAge);
                weight.setText("Вес: " + textWeight);
                growth.setText("Рост: " + textGrowth);
                phone.setText("Номер телефона: " + textPhone);
                imt.setText("Показатель ИМТ: " + textImt);
                tb.setText("Тип фигуры: " + textTb);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void OnButtonClick() {
        btnProgram.setOnClickListener(view -> {
            Intent ReadyProgramIntent = new Intent(CardKlientActivity.this, ReadyProgramKlient.class);
            ReadyProgramIntent.putExtra("klientName", currentKlientName);
            startActivity(ReadyProgramIntent);
        });
        btnCalc.setOnClickListener(view -> {
            Intent CalcIntent = new Intent(CardKlientActivity.this, KlientCalculate.class);
            CalcIntent.putExtra("klientName", currentKlientName);
            startActivity(CalcIntent);
        });
        btnMenu.setOnClickListener(view -> {
            Intent MenuIntent = new Intent(CardKlientActivity.this, ReadyMenuKlient.class);
            MenuIntent.putExtra("klientName", currentKlientName);
            startActivity(MenuIntent);
        });
        btnSaveNotes.setOnClickListener(view -> {
            saveNotes();
            Toast.makeText(CardKlientActivity.this, "Сохранено", Toast.LENGTH_SHORT).show();
        });

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
            Intent PhotoIntent = new Intent(CardKlientActivity.this, PhotoKlient.class);
            PhotoIntent.putExtra("klientName", currentKlientName);
            startActivity(PhotoIntent);
        }
        if(item.getItemId() == R.id.menu_spisok){
            Intent settingsIntent = new Intent(CardKlientActivity.this, KlientsList.class);
            startActivity(settingsIntent);
        }
        if(item.getItemId() == R.id.menu_do_program){
            Intent ProgramIntent = new Intent(CardKlientActivity.this, KlientProgram.class);
            ProgramIntent.putExtra("klientName", currentKlientName);
            startActivity(ProgramIntent);
        }
        if(item.getItemId() == R.id.menu_do_menu){
            Intent MenuIntent = new Intent(CardKlientActivity.this, NutritionProgram.class);
            MenuIntent.putExtra("klientName", currentKlientName);
            startActivity(MenuIntent);
        }
        if(item.getItemId() == R.id.menu_exit){
            auth.signOut();
            Intent loginIntent = new Intent(CardKlientActivity.this, MainActivity.class);
            startActivity(loginIntent);
        }

        return true;
    }

    private void init(){
        Toolbar toolbar = findViewById(R.id.klient_card_bar_layout);
    setSupportActionBar(toolbar);
    Objects.requireNonNull(getSupportActionBar()).setTitle("Клиент " + currentKlientName);
    }
}