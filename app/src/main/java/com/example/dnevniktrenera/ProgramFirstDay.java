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


public class ProgramFirstDay extends Fragment {
    private Spinner spinnerOne, spinnerTwo, spinnerThree, spinnerFour, spinnerFive, spinnerSix;
    private EditText r1,r2,r3,r4,r5,r6, a1,a2,a3,a4,a5,a6,w1,w2,w3,w4,w5,w6;
    private String exOne, exTwo, exThree, exFour, exFive, exSix;
    private List<String> exercise;
    private DatabaseReference KlientsRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_program_first_day, container, false);
        String trenerId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        /*Получение строки с именем клиента из клиентпрограмм активити в этот фрагмент (у фрагмента нет интента)*/
        Bundle bundle = this.getArguments();

        assert bundle != null;
        String currentKlientName = bundle.getString("klientName");

        KlientsRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(trenerId).child("Klients").child(currentKlientName)
                .child("Program").child("DayOne");

        Button btnSave;
        btnSave = view.findViewById(R.id.btn_save);

        spinnerOne = view.findViewById(R.id.spinner_one);
        spinnerTwo =  view.findViewById(R.id.spinner_two);
        spinnerThree = view.findViewById(R.id.spinner_three);
        spinnerFour = view.findViewById(R.id.spinner_four);
        spinnerFive =  view.findViewById(R.id.spinner_five);
        spinnerSix =  view.findViewById(R.id.spinner_six);

        r1 = view.findViewById(R.id.povtor_one);
        r2 = view.findViewById(R.id.povtor_two);
        r3 = view.findViewById(R.id.povtor_three);
        r4 = view.findViewById(R.id.povtor_four);
        r5 = view.findViewById(R.id.povtor_five);
        r6 = view.findViewById(R.id.povtor_six);

        a1 = view.findViewById(R.id.podhod_one);
        a2 = view.findViewById(R.id.podhod_two);
        a3 = view.findViewById(R.id.podhod_three);
        a4 = view.findViewById(R.id.podhod_four);
        a5 = view.findViewById(R.id.podhod_five);
        a6 = view.findViewById(R.id.podhod_six);

        w1 = view.findViewById(R.id.ves_one);
        w2 = view.findViewById(R.id.ves_two);
        w3 = view.findViewById(R.id.ves_three);
        w4 = view.findViewById(R.id.ves_four);
        w5 = view.findViewById(R.id.ves_five);
        w6 = view.findViewById(R.id.ves_six);


        exercise = new ArrayList<>();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        String onlineUserID = mUser.getUid();
        DatabaseReference exBase = FirebaseDatabase.getInstance().getReference("Users").child(onlineUserID).child("Exercise");

        exBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childSnapshot: snapshot.getChildren()){
                String spinnerEx = childSnapshot.child("task").getValue(String.class);
                exercise.add(spinnerEx);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, exercise);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerOne.setAdapter(arrayAdapter);
                spinnerTwo.setAdapter(arrayAdapter);
                spinnerThree.setAdapter(arrayAdapter);
                spinnerFour.setAdapter(arrayAdapter);
                spinnerFive.setAdapter(arrayAdapter);
                spinnerSix.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_SHORT).show();

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveProgramToBase();
            }

            private void SaveProgramToBase() {
                exOne = spinnerOne.getSelectedItem().toString();
                exTwo = spinnerTwo.getSelectedItem().toString();
                exThree = spinnerThree.getSelectedItem().toString();
                exFour = spinnerFour.getSelectedItem().toString();
                exFive = spinnerFive.getSelectedItem().toString();
                exSix = spinnerSix.getSelectedItem().toString();

                HashMap<String, Object> programOneMap = new HashMap<>();

                programOneMap.put("1", exOne + " " +
                        r1.getText().toString() + "x" +
                        a1.getText().toString() +
                        " с весом " + w1.getText().toString() + " кг");
                programOneMap.put("2", exTwo + " " +
                        r2.getText().toString() + "x" +
                        a2.getText().toString() +
                        " с весом " + w2.getText().toString() + " кг");
                programOneMap.put("3", exThree + " " +
                        r3.getText().toString() + "x" +
                        a3.getText().toString() +
                        " с весом " + w3.getText().toString() + " кг");
                programOneMap.put("4", exFour + " " +
                        r4.getText().toString() + "x" +
                        a4.getText().toString() +
                        " с весом " + w4.getText().toString() + " кг");
                programOneMap.put("5", exFive + " " +
                        r5.getText().toString() + "*" +
                        a5.getText().toString() +
                        " с весом " + w5.getText().toString() + " кг");
                programOneMap.put("6", exSix + " " +
                        r6.getText().toString() + "x" +
                        a6.getText().toString() +
                        " с весом " + w6.getText().toString() + " кг");

                KlientsRef.updateChildren(programOneMap)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "Программа первого дня обновлена", Toast.LENGTH_SHORT).show();
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