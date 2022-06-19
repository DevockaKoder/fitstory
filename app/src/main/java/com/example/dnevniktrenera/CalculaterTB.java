package com.example.dnevniktrenera;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;


public class CalculaterTB extends Fragment {
    private EditText text_from_user_t, text_from_user_b;
    private Spinner spinnerMale;
    private TextView result;
    private Button btn_raschet;
    private float numT, numB, TB;
    private DatabaseReference KlientsRef;
    private final String[] male = new String[] {"Мужской", "Женский"};
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_calculater_tb, container, false);

        Button btnSave;
        btnSave = view.findViewById(R.id.btn_save_tb);

        Bundle bundle = this.getArguments();
        assert bundle != null;
        String trenerId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String currentKlientName = bundle.getString("klientName");

        KlientsRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(trenerId).child("Klients").child(currentKlientName)
                .child("KlientInfo");

        text_from_user_t = view.findViewById(R.id.massa_tb);
        text_from_user_b = view.findViewById(R.id.rost_tb);

        spinnerMale = view.findViewById(R.id.spinner_pol_tb);

        ArrayAdapter<String> adapterMale = new ArrayAdapter<>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item, new ArrayList<String>(Arrays.asList(male)));
        adapterMale.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMale.setAdapter(adapterMale);
        result = view.findViewById(R.id.result_field_calories);
        btn_raschet = view.findViewById(R.id.btn_res_calories);

        btn_raschet.setOnClickListener(view1 -> {
            String text = text_from_user_t.getText().toString();
            if (!text.matches("")) {
                numT = Float.parseFloat(text_from_user_t.getText().toString());
                numB = Float.parseFloat(text_from_user_b.getText().toString());

                TB = (numT / numB);
                @SuppressLint("DefaultLocale")
                String res = String.format("%.2f", TB);
                if ((spinnerMale.getSelectedItem().toString().equals("Мужской"))) {
                    if ((TB >= 0.8) && (TB <= 0.9)) {
                        result.setText("\nТип фигуры \"Авокадо\", равномерное распределение жировой ткани на талии и бедрах ");
                    } else if (TB > 0.9){
                        result.setText("\nТип фигуры \"Яблоко\", характеризуется отложением жирового запаса в области талии и живота," +
                                " является наиболее опасным вариантом расположения жира, повышая риск развития CC заболеваний (атеросклероза, ИБС, инсульта), сахарного диабета II типа");
                    } else if (TB < 0.8) {
                        result.setText("\nТип фигуры \"Груша\", характеризуется отложением жирового запаса на ягодицах и бедрах " +
                                "и является наиболее здоровым вариантом расположения жира");
                    }
                } else {
                    if ((TB >= 0.8) && (TB <= 0.85)) {
                        result.setText("\nТип фигуры \"Авокадо\", равномерное распределение жировой ткани на талии и бедрах ");
                    } else if (TB > 0.85){
                        result.setText("\nТип фигуры \"Яблоко\", характеризуется отложением жирового запаса в области талии и живота," +
                                " является наиболее опасным вариантом расположения жира, повышая риск развития сердечно-сосудистых заболеваний (атеросклероза, ИБС, инсульта), сахарного диабета второго типа, гиперлипидимии");
                    } else if (TB < 0.8) {
                        result.setText("\nТип фигуры \"Груша\", характеризуется отложением жирового запаса на ягодицах и бедрах " +
                                "и является наиболее здоровым вариантом расположения жира");
                    }

                }
                btn_raschet.setText("Готово");
                btn_raschet.setBackgroundTintList(ColorStateList.valueOf(Color.DKGRAY));

            } else {
                Toast.makeText(getActivity(),
                        "Нужно ввести значение обхвата талии и бедер",
                        Toast.LENGTH_LONG).show();
                btn_raschet.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            }

        });

        btnSave.setOnClickListener(view12 -> {
            HashMap<String, Object> imtMap = new HashMap<>();
            imtMap.put("Индекс тб", result.getText().toString());

            KlientsRef.updateChildren(imtMap)
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