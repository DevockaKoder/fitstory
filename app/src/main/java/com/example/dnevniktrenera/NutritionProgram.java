package com.example.dnevniktrenera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class NutritionProgram extends AppCompatActivity {
    private Spinner day, vid, name;
    private TextView caloriesText;
    private Button btnSaveMenu;
    private FirebaseAuth auth;
    private String TrenerId, currentKlientName;
    private DatabaseReference RecipesRef, KlientRef;

    private final String[] day_of_week = new String[] {"Понедельник", "Вторник", "Среда",
            "Четверг", "Пятница", "Суббота", "Воскресенье"};
    private final String[] vid_trapez = new String[] {"Завтрак", "Обед", "Перекус", "Ужин"};
    private List<String> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_program);
        auth = FirebaseAuth.getInstance();
        TrenerId = auth.getCurrentUser().getUid();
        currentKlientName = getIntent().getExtras().get("klientName").toString();
        KlientRef = FirebaseDatabase.getInstance().getReference("Users").child(TrenerId).child("Klients").child(currentKlientName);
        RecipesRef = FirebaseDatabase.getInstance().getReference("Users").child(TrenerId).child("Recipes");
        caloriesText = findViewById(R.id.calories_klient);
        SetTextToView();
        SetAdaptersToSpinner();
        OnBtnClick();


    }

    private void SetTextToView() {
        KlientRef.child("KlientInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String calories = snapshot.child("Дневная калорийность").getValue().toString();
                caloriesText.setText("На " + calories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NutritionProgram.this, "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SetAdaptersToSpinner() {
        day = findViewById(R.id.spinner_day);
        vid = findViewById(R.id.spinner_trapeza);
        name = findViewById(R.id.spinner_name_blyudo);

        ArrayAdapter<String> adapterDay = new ArrayAdapter<>(NutritionProgram.this,
                R.layout.support_simple_spinner_dropdown_item, new ArrayList<String>(Arrays.asList(day_of_week)));
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(adapterDay);

        ArrayAdapter<String> adapterVid = new ArrayAdapter<>(NutritionProgram.this,
                R.layout.support_simple_spinner_dropdown_item, new ArrayList<String>(Arrays.asList(vid_trapez)));
        adapterVid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vid.setAdapter(adapterVid);

        recipes = new ArrayList<>();

        RecipesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot: snapshot.getChildren()){
                    String spinnerEx = childSnapshot.child("nameRecipe").getValue(String.class);
                    recipes.add(spinnerEx);
                }

                ArrayAdapter<String> adapterRecipes = new ArrayAdapter<>(NutritionProgram.this, R.layout.support_simple_spinner_dropdown_item, recipes);
                adapterRecipes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                name.setAdapter(adapterRecipes);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NutritionProgram.this, "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void OnBtnClick() {
        this.btnSaveMenu = findViewById(R.id.btn_save_menu);

        btnSaveMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMenuToBase();
            }

            private void SaveMenuToBase() {
                String dayMenu = day.getSelectedItem().toString();
                String vidMenu = vid.getSelectedItem().toString();
                String nameMenu = name.getSelectedItem().toString();

                final String[] calories = {new String()};
                final String[] recipe = {new String()};

                RecipesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            calories[0] = (childSnapshot.child("calories").getValue()).toString();
                            recipe[0] = (childSnapshot.child("recipe").getValue()).toString();
                        }

                            HashMap<String, Object> MenuMap = new HashMap<>();

                            MenuMap.put("DayWeek", dayMenu);
                            MenuMap.put("type", vidMenu);
                            MenuMap.put("name", nameMenu);
                            MenuMap.put("calories", calories[0]);
                            MenuMap.put("recipe", recipe[0]);
                            KlientRef.child("Меню на неделю").child(dayMenu).child(vidMenu).updateChildren(MenuMap);

                            Toast.makeText(NutritionProgram.this, "Сохранено", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(NutritionProgram.this, "Ошибка", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}