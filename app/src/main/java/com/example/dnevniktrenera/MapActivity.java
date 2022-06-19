package com.example.dnevniktrenera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapActivity extends AppCompatActivity {

    private AppCompatImageButton btn_spisok, btn_exbase, btn_recbase;
    private Button btn_exit;
    FirebaseAuth auth;
    DatabaseReference TrenersRef;
    private String currentTrenerName;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        auth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.trener_bar_layout);
        setSupportActionBar(toolbar);

        TrenersRef = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid()).child("Data");

        TrenersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentTrenerName = snapshot.child("name").getValue().toString();
                getSupportActionBar().setTitle(currentTrenerName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        OnBtnClick();
        init();
    }


    public void OnBtnClick() {
        this.btn_spisok = findViewById(R.id.btnSpisok);
        this.btn_exbase = findViewById(R.id.btnExBase);
        this.btn_recbase = findViewById(R.id.btnRecBase);
        this.btn_exit = findViewById(R.id.btn_exit);

        btn_exit.setOnClickListener(view -> {
            auth.signOut();
            Intent intent;
            intent = new Intent(MapActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btn_spisok.setOnClickListener(view -> {
            Intent intent;
            intent = new Intent(MapActivity.this, KlientsList.class);
            startActivity(intent);
        });

        btn_exbase.setOnClickListener(view -> {
            Intent intent;
            intent = new Intent(MapActivity.this, ExerciseBase.class);
            startActivity(intent);
        });

        btn_recbase.setOnClickListener(view -> {
            Intent intent;
            intent = new Intent(MapActivity.this, RecipeBase.class);
            startActivity(intent);
        });

    }
    private void init(){

    }
}