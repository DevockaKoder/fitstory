package com.example.dnevniktrenera;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class ProgramThreeDay extends Fragment {
    private Spinner spinnerOne3, spinnerTwo3, spinnerThree3, spinnerFour3, spinnerFive3, spinnerSix3;
    private EditText r1_3, r2_3, r3_3, r4_3, r5_3, r6_3,
            a1_3, a2_3, a3_3, a4_3, a5_3, a6_3,
            w1_3, w2_3, w3_3, w4_3, w5_3, w6_3;
    private String exOne3, exTwo3, exThree3, exFour3, exFive3, exSix3;
    private List<String> exerciseThree = new ArrayList<>();
    private DatabaseReference KlientsRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_program_three_day, container, false);
        String trenerId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        /*Получение строки с именем клиента из клиентпрограмм активити в этот фрагмент (у фрагмента нет интента)*/
        Bundle bundle = this.getArguments();

        assert bundle != null;
        String currentKlientName = bundle.getString("klientName");

        KlientsRef = FirebaseDatabase.getInstance().getReference("Users").
                child(trenerId).child("Klients").child(currentKlientName)
                .child("Program").child("DayThree");

        Button btnSaveThree;
        btnSaveThree = view.findViewById(R.id.btn_save_three);

        spinnerOne3 = view.findViewById(R.id.spinner_one3);
        spinnerTwo3 =  view.findViewById(R.id.spinner_two3);
        spinnerThree3 = view.findViewById(R.id.spinner_three3);
        spinnerFour3 = view.findViewById(R.id.spinner_four3);
        spinnerFive3 =  view.findViewById(R.id.spinner_five3);
        spinnerSix3 =  view.findViewById(R.id.spinner_six3);

        r1_3 = view.findViewById(R.id.povtor_one3);
        r2_3 = view.findViewById(R.id.povtor_two3);
        r3_3 = view.findViewById(R.id.povtor_three3);
        r4_3 = view.findViewById(R.id.povtor_four3);
        r5_3 = view.findViewById(R.id.povtor_five3);
        r6_3 = view.findViewById(R.id.povtor_six3);

        a1_3 = view.findViewById(R.id.podhod_one3);
        a2_3 = view.findViewById(R.id.podhod_two3);
        a3_3 = view.findViewById(R.id.podhod_three3);
        a4_3 = view.findViewById(R.id.podhod_four3);
        a5_3 = view.findViewById(R.id.podhod_five3);
        a6_3 = view.findViewById(R.id.podhod_six3);

        w1_3 = view.findViewById(R.id.ves_one3);
        w2_3 = view.findViewById(R.id.ves_two3);
        w3_3 = view.findViewById(R.id.ves_three3);
        w4_3 = view.findViewById(R.id.ves_four3);
        w5_3 = view.findViewById(R.id.ves_five3);
        w6_3 = view.findViewById(R.id.ves_six3);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        String onlineUserID = mUser.getUid();
        DatabaseReference exBaseTwo = FirebaseDatabase.getInstance().getReference("Users")
                .child(onlineUserID).child("Exercise");

        exBaseTwo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot: snapshot.getChildren()){
                    String spinnerEx = childSnapshot.child("task").getValue(String.class);
                    exerciseThree.add(spinnerEx);
                }

                ArrayAdapter<String> arrayAdapterTwo = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, exerciseThree);
                arrayAdapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerOne3.setAdapter(arrayAdapterTwo);
                spinnerTwo3.setAdapter(arrayAdapterTwo);
                spinnerThree3.setAdapter(arrayAdapterTwo);
                spinnerFour3.setAdapter(arrayAdapterTwo);
                spinnerFive3.setAdapter(arrayAdapterTwo);
                spinnerSix3.setAdapter(arrayAdapterTwo);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_SHORT).show();

            }
        });

        btnSaveThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveProgramToBase();
            }

            private void SaveProgramToBase() {
                exOne3 = spinnerOne3.getSelectedItem().toString();
                exTwo3 = spinnerTwo3.getSelectedItem().toString();
                exThree3 = spinnerThree3.getSelectedItem().toString();
                exFour3 = spinnerFour3.getSelectedItem().toString();
                exFive3 = spinnerFive3.getSelectedItem().toString();
                exSix3 = spinnerSix3.getSelectedItem().toString();

                HashMap<String, Object> programOneMap = new HashMap<>();

                programOneMap.put("1", exOne3 + " " +
                        r1_3.getText().toString() + "x" +
                        a1_3.getText().toString() +
                        " с весом " + w1_3.getText().toString() + " кг");
                programOneMap.put("2", exTwo3 + " " +
                        r2_3.getText().toString() + "x" +
                        a2_3.getText().toString() +
                        " с весом " + w2_3.getText().toString() + " кг");
                programOneMap.put("3", exThree3 + " " +
                        r3_3.getText().toString() + "x" +
                        a3_3.getText().toString() +
                        " с весом " + w3_3.getText().toString() + " кг");
                programOneMap.put("4", exFour3 + " " +
                        r4_3.getText().toString() + "x" +
                        a4_3.getText().toString() +
                        " с весом " + w4_3.getText().toString() + " кг");
                programOneMap.put("5", exFive3 + " " +
                        r5_3.getText().toString() + "*" +
                        a5_3.getText().toString() +
                        " с весом " + w5_3.getText().toString() + " кг");
                programOneMap.put("6", exSix3 + " " +
                        r6_3.getText().toString() + "x" +
                        a6_3.getText().toString() +
                        " с весом " + w6_3.getText().toString() + " кг");

                KlientsRef.updateChildren(programOneMap)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "Программа третьего дня обновлена", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String message = Objects.requireNonNull(task.getException()).toString();
                                Toast.makeText(getActivity(), "Ошибка: "+ message, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return view;
    }

}