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


public class CalculaterIMT extends Fragment {
    private EditText text_from_user_kg, text_from_user_sm;
    private TextView result;
    private Button btn_raschet;
    private float numKg, numSm, imt;
    private DatabaseReference KlientsRef;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_calculater_imt, container, false);

        Button btnSave;
        btnSave = view.findViewById(R.id.btn_save_imt);

        Bundle bundle = this.getArguments();
        assert bundle != null;
        String trenerId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String currentKlientName = bundle.getString("klientName");

        KlientsRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(trenerId).child("Klients").child(currentKlientName)
                .child("KlientInfo");

        text_from_user_kg = (EditText) view.findViewById(R.id.massa);
        text_from_user_sm = (EditText) view.findViewById(R.id.rost);

        KlientsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                text_from_user_kg.setText(Objects.requireNonNull(snapshot.child("weight").getValue()).toString());
                text_from_user_sm.setText(Objects.requireNonNull(snapshot.child("growth").getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        result = (TextView) view.findViewById(R.id.result_field);
        btn_raschet = (Button) view.findViewById(R.id.btn_res);
        btn_raschet.setOnClickListener(view1 -> {
            String text = text_from_user_kg.getText().toString();
            if (!text.matches("")) {
                numKg = Float.parseFloat(text_from_user_kg.getText().toString());
                numSm = Float.parseFloat(text_from_user_sm.getText().toString());
                imt = (numKg/(numSm*numSm))*10000;
                @SuppressLint("DefaultLocale")
                String res = String.format("%.2f",imt);
                if (imt <= 16) {
                    result.setText(res + "\nВыраженный дефицит массы тела");
                } else if ((imt > 16) && (imt<18.5)) {
                    result.setText(res + "\nНедостаточная масса тела");
                }
                else if ((imt > 18.5) && (imt < 25)) {
                    result.setText(res + "\nНорма");
                }else if ((imt > 25) && (imt<30)) {
                    result.setText(res + "\nИзбыточная масса тела");
                }
                else if ((imt > 30) && (imt<35)) {
                    result.setText(res + "\nВозможно, это первая стадия ожирения. Проконсультируйтесь с врачом");
                } else if ((imt > 35) && (imt<40)) {
                    result.setText(res + "\nВозможно, это вторая стадия ожирения. Проконсультируйтесь с врачом");
                } else if (imt > 40) {
                    result.setText(res + "\nВозможно, это третья стадия ожирения. Проконсультируйтесь с врачом");
                }
                btn_raschet.setText("Готово");
                btn_raschet.setBackgroundTintList(ColorStateList.valueOf(Color.DKGRAY));
            } else {
                Toast.makeText(getActivity(),
                        "Нужно ввести значение массы и роста",
                        Toast.LENGTH_LONG).show();
                btn_raschet.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            }

        });

        btnSave.setOnClickListener(view12 -> {
            HashMap<String, Object> imtMap = new HashMap<>();
            imtMap.put("Показатель ИМТ", result.getText().toString());

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