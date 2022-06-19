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


public class ProgramTwoDay extends Fragment {
    private Spinner spinnerOne2, spinnerTwo2, spinnerThree2, spinnerFour2, spinnerFive2, spinnerSix2;
    private EditText r1_2, r2_2, r3_2, r4_2, r5_2, r6_2,
            a1_2, a2_2, a3_2, a4_2, a5_2, a6_2,
            w1_2, w2_2, w3_2, w4_2, w5_2, w6_2;
    private String exOne2, exTwo2, exThree2, exFour2, exFive2, exSix2;
    private List<String> exerciseTwo;
    private DatabaseReference KlientsRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_program_two_day, container, false);
        String trenerId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        /*Получение строки с именем клиента из клиентпрограмм активити в этот фрагмент (у фрагмента нет интента)*/
        Bundle bundle = this.getArguments();

        assert bundle != null;
        String currentKlientName = bundle.getString("klientName");

        KlientsRef = FirebaseDatabase.getInstance().getReference("Users").
                child(trenerId).child("Klients").child(currentKlientName)
                .child("Program").child("DayTwo");

        Button btnSaveTwo;
        btnSaveTwo = view.findViewById(R.id.btn_save_two);

        spinnerOne2 = view.findViewById(R.id.spinner_one2);
        spinnerTwo2 =  view.findViewById(R.id.spinner_two2);
        spinnerThree2 = view.findViewById(R.id.spinner_three2);
        spinnerFour2 = view.findViewById(R.id.spinner_four2);
        spinnerFive2 =  view.findViewById(R.id.spinner_five2);
        spinnerSix2 =  view.findViewById(R.id.spinner_six2);

        r1_2 = view.findViewById(R.id.povtor_one2);
        r2_2 = view.findViewById(R.id.povtor_two2);
        r3_2 = view.findViewById(R.id.povtor_three2);
        r4_2 = view.findViewById(R.id.povtor_four2);
        r5_2 = view.findViewById(R.id.povtor_five2);
        r6_2 = view.findViewById(R.id.povtor_six2);

        a1_2 = view.findViewById(R.id.podhod_one2);
        a2_2 = view.findViewById(R.id.podhod_two2);
        a3_2 = view.findViewById(R.id.podhod_three2);
        a4_2 = view.findViewById(R.id.podhod_four2);
        a5_2 = view.findViewById(R.id.podhod_five2);
        a6_2 = view.findViewById(R.id.podhod_six2);

        w1_2 = view.findViewById(R.id.ves_one2);
        w2_2 = view.findViewById(R.id.ves_two2);
        w3_2 = view.findViewById(R.id.ves_three2);
        w4_2 = view.findViewById(R.id.ves_four2);
        w5_2 = view.findViewById(R.id.ves_five2);
        w6_2 = view.findViewById(R.id.ves_six2);


        exerciseTwo = new ArrayList<>();

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
                    exerciseTwo.add(spinnerEx);
                }

                ArrayAdapter<String> arrayAdapterTwo = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, exerciseTwo);
                arrayAdapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerOne2.setAdapter(arrayAdapterTwo);
                spinnerTwo2.setAdapter(arrayAdapterTwo);
                spinnerThree2.setAdapter(arrayAdapterTwo);
                spinnerFour2.setAdapter(arrayAdapterTwo);
                spinnerFive2.setAdapter(arrayAdapterTwo);
                spinnerSix2.setAdapter(arrayAdapterTwo);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_SHORT).show();

            }
        });

        btnSaveTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveProgramToBase();
            }

            private void SaveProgramToBase() {
                exOne2 = spinnerOne2.getSelectedItem().toString();
                exTwo2 = spinnerTwo2.getSelectedItem().toString();
                exThree2 = spinnerThree2.getSelectedItem().toString();
                exFour2 = spinnerFour2.getSelectedItem().toString();
                exFive2 = spinnerFive2.getSelectedItem().toString();
                exSix2 = spinnerSix2.getSelectedItem().toString();

                HashMap<String, Object> programOneMap = new HashMap<>();

                programOneMap.put("1", exOne2 + " " +
                        r1_2.getText().toString() + "x" +
                        a1_2.getText().toString() +
                        " с весом " + w1_2.getText().toString() + " кг");
                programOneMap.put("2", exTwo2 + " " +
                        r2_2.getText().toString() + "x" +
                        a2_2.getText().toString() +
                        " с весом " + w2_2.getText().toString() + " кг");
                programOneMap.put("3", exThree2 + " " +
                        r3_2.getText().toString() + "x" +
                        a3_2.getText().toString() +
                        " с весом " + w3_2.getText().toString() + " кг");
                programOneMap.put("4", exFour2 + " " +
                        r4_2.getText().toString() + "x" +
                        a4_2.getText().toString() +
                        " с весом " + w4_2.getText().toString() + " кг");
                programOneMap.put("5", exFive2 + " " +
                        r5_2.getText().toString() + "*" +
                        a5_2.getText().toString() +
                        " с весом " + w5_2.getText().toString() + " кг");
                programOneMap.put("6", exSix2 + " " +
                        r6_2.getText().toString() + "x" +
                        a6_2.getText().toString() +
                        " с весом " + w6_2.getText().toString() + " кг");

                KlientsRef.updateChildren(programOneMap)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "Программа второго дня обновлена", Toast.LENGTH_SHORT).show();
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