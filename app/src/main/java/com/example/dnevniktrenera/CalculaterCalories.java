package com.example.dnevniktrenera;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Objects;


public class CalculaterCalories extends Fragment {
    private EditText text_from_user_kg, text_from_user_sm, text_from_user_age;
    private Spinner spinnerMale, spinnerPurp, spinnerFiza;
    private TextView result;
    private Button btn_raschet;
    private float numKg, numSm, numAge, calories, newCalories;
    private DatabaseReference KlientsRef;
    private final String[] male = new String[] {"Мужской", "Женский"};
    private final String[] purp = new String[] {"Набор", "Поддержание", "Похудение"};
    private final String[] fiza = new String[] {"Минимальная активность", "Легкая нагрузка",
            "3-5 тренировок в неделю", "Ежедневные тренировки", "Две тренировки в день"};
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_calculater_calories, container, false);

        Button btnSave;
        btnSave = view.findViewById(R.id.btn_save_calories);

        text_from_user_age = view.findViewById(R.id.age_calories);
        text_from_user_kg = view.findViewById(R.id.massa_calories);
        text_from_user_sm = view.findViewById(R.id.rost_calories);

        spinnerMale = view.findViewById(R.id.spinner_pol);
        spinnerPurp = view.findViewById(R.id.spinner_v);
        spinnerFiza = view.findViewById(R.id.spinner_fiza);

        ArrayAdapter<String> adapterMale = new ArrayAdapter<>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, new ArrayList<String>(Arrays.asList(male)));
        adapterMale.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMale.setAdapter(adapterMale);

        ArrayAdapter<String> adapterPurp = new ArrayAdapter<>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, new ArrayList<String>(Arrays.asList(purp)));
        adapterPurp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPurp.setAdapter(adapterPurp);

        ArrayAdapter<String> adapterFiza = new ArrayAdapter<>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, new ArrayList<String>(Arrays.asList(fiza)));
        adapterFiza.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiza.setAdapter(adapterFiza);


        Bundle bundle = this.getArguments();

        assert bundle != null;
        String trenerId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String currentKlientName = bundle.getString("klientName");
        KlientsRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(trenerId).child("Klients").child(currentKlientName)
                .child("KlientInfo");

        KlientsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                text_from_user_kg.setText(Objects.requireNonNull(snapshot.child("weight").getValue()).toString());
                text_from_user_sm.setText(Objects.requireNonNull(snapshot.child("growth").getValue()).toString());
                text_from_user_age.setText(Objects.requireNonNull(snapshot.child("age").getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        result = view.findViewById(R.id.result_field_calories);
        btn_raschet = view.findViewById(R.id.btn_res_calories);

        btn_raschet.setOnClickListener(view1 -> {
            String text = text_from_user_kg.getText().toString();
            if (!text.matches("")) {
                numKg = Float.parseFloat(text_from_user_kg.getText().toString());
                numSm = Float.parseFloat(text_from_user_sm.getText().toString());
                numAge = Float.parseFloat(text_from_user_age.getText().toString());

                calories = (float) ((10 * numKg) + (6.25 * numSm) - (5 * numAge));

                if ((spinnerMale.getSelectedItem().toString().equals("Мужской"))) {
                    newCalories = calories + 5;
                    if (spinnerPurp.getSelectedItem().toString().equals("Поддержание")) {
                        if (spinnerFiza.getSelectedItem().toString().equals("Минимальная активность")) {
                            newCalories *= 1.2;
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Легкая нагрузка")) {
                            newCalories *= 1.375;
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("3-5 тренировок в неделю")) {
                            newCalories *= 1.55;
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Ежедневные тренировки")) {
                            newCalories *= 1.725;
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Две тренировки в день")) {
                            newCalories *= 1.9;
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        }
                    } else if ((spinnerPurp.getSelectedItem().toString().equals("Набор"))) {
                        if (spinnerFiza.getSelectedItem().toString().equals("Минимальная активность")) {
                            newCalories = (float) ((newCalories * 1.2) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Легкая нагрузка")) {
                            newCalories = (float) ((newCalories * 1.375) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("3-5 тренировок в неделю")) {
                            newCalories = (float) ((newCalories * 1.55) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Ежедневные тренировки")) {
                            newCalories = (float) ((newCalories * 1.725) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Две тренировки в день")) {
                            newCalories = (float) ((newCalories * 1.9) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        }
                    } else if ((spinnerPurp.getSelectedItem().toString().equals("Набор"))) {
                        if (spinnerFiza.getSelectedItem().toString().equals("Минимальная активность")) {
                            newCalories = (float) ((newCalories * 1.2) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Легкая нагрузка")) {
                            newCalories = (float) ((newCalories * 1.375) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("3-5 тренировок в неделю")) {
                            newCalories = (float) ((newCalories * 1.55) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Ежедневные тренировки")) {
                            newCalories = (float) ((newCalories * 1.725) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Две тренировки в день")) {
                            newCalories = (float) ((newCalories * 1.9) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        }
                    } else if ((spinnerPurp.getSelectedItem().toString().equals("Похудение"))) {
                        if (spinnerFiza.getSelectedItem().toString().equals("Минимальная активность")) {
                            newCalories = (float) ((newCalories * 1.2) - 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Легкая нагрузка")) {
                            newCalories = (float) ((newCalories * 1.375) - 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("3-5 тренировок в неделю")) {
                            newCalories = (float) ((newCalories * 1.55) - 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Ежедневные тренировки")) {
                            newCalories = (float) ((newCalories * 1.725) - 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Две тренировки в день")) {
                            newCalories = (float) ((newCalories * 1.9) - 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        }
                    }
                }

                if ((spinnerMale.getSelectedItem().toString().equals("Женский"))) {
                    newCalories = calories - 161;
                    if (spinnerPurp.getSelectedItem().toString().equals("Поддержание")) {
                        if (spinnerFiza.getSelectedItem().toString().equals("Минимальная активность")) {
                            newCalories *= 1.2;
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Легкая нагрузка")) {
                            newCalories *= 1.375;
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("3-5 тренировок в неделю")) {
                            newCalories *= 1.55;
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Ежедневные тренировки")) {
                            newCalories *= 1.725;
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Две тренировки в день")) {
                            newCalories *= 1.9;
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        }
                    } else if ((spinnerPurp.getSelectedItem().toString().equals("Набор"))) {
                        if (spinnerFiza.getSelectedItem().toString().equals("Минимальная активность")) {
                            newCalories = (float) ((newCalories * 1.2) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Легкая нагрузка")) {
                            newCalories = (float) ((newCalories * 1.375) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("3-5 тренировок в неделю")) {
                            newCalories = (float) ((newCalories * 1.55) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Ежедневные тренировки")) {
                            newCalories = (float) ((newCalories * 1.725) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Две тренировки в день")) {
                            newCalories = (float) ((newCalories * 1.9) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        }
                    } else if ((spinnerPurp.getSelectedItem().toString().equals("Набор"))) {
                        if (spinnerFiza.getSelectedItem().toString().equals("Минимальная активность")) {
                            newCalories = (float) ((newCalories * 1.2) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Легкая нагрузка")) {
                            newCalories = (float) ((newCalories * 1.375) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("3-5 тренировок в неделю")) {
                            newCalories = (float) ((newCalories * 1.55) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Ежедневные тренировки")) {
                            newCalories = (float) ((newCalories * 1.725) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Две тренировки в день")) {
                            newCalories = (float) ((newCalories * 1.9) + 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        }
                    } else if ((spinnerPurp.getSelectedItem().toString().equals("Похудение"))) {
                        if (spinnerFiza.getSelectedItem().toString().equals("Минимальная активность")) {
                            newCalories = (float) ((newCalories * 1.2) - 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Легкая нагрузка")) {
                            newCalories = (float) ((newCalories * 1.375) - 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("3-5 тренировок в неделю")) {
                            newCalories = (float) ((newCalories * 1.55) - 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Ежедневные тренировки")) {
                            newCalories = (float) ((newCalories * 1.725) - 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        } else if (spinnerFiza.getSelectedItem().toString().equals("Две тренировки в день")) {
                            newCalories = (float) ((newCalories * 1.9) - 200);
                            @SuppressLint("DefaultLocale") String res = String.format("%.2f", newCalories);
                            result.setText(res + " калорий");
                        }
                    }
                }

                btn_raschet.setText("Готово");
                btn_raschet.setBackgroundTintList(ColorStateList.valueOf(Color.DKGRAY));
            } else {
                Toast.makeText(getActivity(),
                        "Нужно ввести значение массы, роста и возраста",
                        Toast.LENGTH_LONG).show();
                btn_raschet.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            }

        });
        btnSave.setOnClickListener(view12 -> {
            HashMap<String, Object> caloriesMap = new HashMap<>();
            caloriesMap.put("Дневная калорийность", result.getText().toString());

            KlientsRef.updateChildren(caloriesMap)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Теперь информация в базе данных", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String message = Objects.requireNonNull(task.getException()).toString();
                            Toast.makeText(getActivity(), "Ошибка: "+ message, Toast.LENGTH_SHORT).show();
                        }
                    });

        });
        return view;
    }
}