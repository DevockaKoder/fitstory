package com.example.dnevniktrenera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ReadyProgramKlient extends AppCompatActivity {
    private TextView exOne, exTwo, exThree, exFour, exFive, exSix,
            exOne2, exTwo2, exThree2, exFour2, exFive2, exSix2,
            exOne3, exTwo3, exThree3, exFour3, exFive3, exSix3, name;
    private Button editProg;
    private FirebaseAuth auth;
    private String TrenerId, currentKlientName;
    private DatabaseReference ProgramRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_program_klient);
        editProg = findViewById(R.id.btn_edit_program);
        auth = FirebaseAuth.getInstance();
        TrenerId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        currentKlientName = getIntent().getExtras().get("klientName").toString();
        ProgramRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(TrenerId).child("Klients")
                .child(currentKlientName).child("Program");

        exOne = findViewById(R.id.ex_one);
        exTwo = findViewById(R.id.ex_two);
        exThree = findViewById(R.id.ex_three);
        exFour = findViewById(R.id.ex_four);
        exFive = findViewById(R.id.ex_five);
        exSix = findViewById(R.id.ex_six);

        exOne2 = findViewById(R.id.ex_one2);
        exTwo2 = findViewById(R.id.ex_two2);
        exThree2 = findViewById(R.id.ex_three2);
        exFour2 = findViewById(R.id.ex_four2);
        exFive2 = findViewById(R.id.ex_five2);
        exSix2 = findViewById(R.id.ex_six2);

        exOne3 = findViewById(R.id.ex_one3);
        exTwo3 = findViewById(R.id.ex_two3);
        exThree3 = findViewById(R.id.ex_three3);
        exFour3 = findViewById(R.id.ex_four3);
        exFive3 = findViewById(R.id.ex_five3);
        exSix3 = findViewById(R.id.ex_six3);

        name = findViewById(R.id.pr_and_name);
        name.setText("Программы тренировок клиента: " + currentKlientName);

        SetProgramText();
        EditProgram();

    }

    private void EditProgram() {
        editProg.setOnClickListener(view -> {
            Intent ProgramIntent = new Intent(ReadyProgramKlient.this, KlientProgram.class);
            ProgramIntent.putExtra("klientName", currentKlientName);
            startActivity(ProgramIntent);
        });
    }

    private void SetProgramText() {
        ProgramRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                exOne.setText(snapshot.child("DayOne").child("1").getValue(String.class));
                exTwo.setText(snapshot.child("DayOne").child("2").getValue(String.class));
                exThree.setText(snapshot.child("DayOne").child("3").getValue(String.class));
                exFour.setText(snapshot.child("DayOne").child("4").getValue(String.class));
                exFive.setText(snapshot.child("DayOne").child("5").getValue(String.class));
                exSix.setText(snapshot.child("DayOne").child("6").getValue(String.class));

                exOne2.setText(snapshot.child("DayTwo").child("1").getValue(String.class));
                exTwo2.setText(snapshot.child("DayTwo").child("2").getValue(String.class));
                exThree2.setText(snapshot.child("DayTwo").child("3").getValue(String.class));
                exFour2.setText(snapshot.child("DayTwo").child("4").getValue(String.class));
                exFive2.setText(snapshot.child("DayTwo").child("5").getValue(String.class));
                exSix2.setText(snapshot.child("DayTwo").child("6").getValue(String.class));

                exOne3.setText(snapshot.child("DayThree").child("1").getValue(String.class));
                exTwo3.setText(snapshot.child("DayThree").child("2").getValue(String.class));
                exThree3.setText(snapshot.child("DayThree").child("3").getValue(String.class));
                exFour3.setText(snapshot.child("DayThree").child("4").getValue(String.class));
                exFive3.setText(snapshot.child("DayThree").child("5").getValue(String.class));
                exSix3.setText(snapshot.child("DayThree").child("6").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}