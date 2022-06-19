package com.example.dnevniktrenera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dnevniktrenera.models.Model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;


public class ExerciseBase extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private ProgressBar loader;

    private String key = "";
    private String task;
    private String description;
    private String kat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_exercise_base);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loader = new ProgressBar(this);
        loader.setProgressTintList(ColorStateList.valueOf(Color.BLUE));


        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        String onlineUserID = mUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(onlineUserID).child("Exercise");

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> addTask());
    }

    private void addTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.input_file_exbase, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText task = myView.findViewById(R.id.task);
        final EditText description = myView.findViewById(R.id.description);
        final EditText kat = myView.findViewById(R.id.kat_ex);
        Button save = myView.findViewById(R.id.saveBtn);
        Button cancel = myView.findViewById(R.id.CancelBtn);

        cancel.setOnClickListener(v -> dialog.dismiss());

        save.setOnClickListener(v -> {
            String mTask = task.getText().toString().trim();
            String mDescription = description.getText().toString().trim();
            String mKat = kat.getText().toString().trim();
            String id = reference.push().getKey();
            String date = DateFormat.getDateInstance().format(new Date());


            if (TextUtils.isEmpty(mTask)) {
                task.setError("Введите название");
                return;
            }
            if (TextUtils.isEmpty(mDescription)) {
                description.setError("Введите описание");
                return;
            }
            if (TextUtils.isEmpty(mKat)) {
                kat.setError("Введите категорию");
                return;
            } else {
                loader.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                Model model = new Model(mTask, mDescription, id, date, mKat);
                assert id != null;
                reference.child(id).setValue(model).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(ExerciseBase.this, "Успешно добавлено", Toast.LENGTH_SHORT).show();
                    } else {
                        String error = Objects.requireNonNull(task1.getException()).toString();
                        Toast.makeText(ExerciseBase.this, "Ошибка: " + error, Toast.LENGTH_SHORT).show();
                    }
                    loader.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                });

            }

            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(reference, Model.class)
                .build();

        FirebaseRecyclerAdapter<Model, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Model, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull final Model model) {
                holder.setDate(model.getDate());
                holder.setTask(model.getTask());
                holder.setDesc(model.getDescription());
                holder.setKat(model.getKat());


                holder.mView.setOnClickListener(v -> {
                    key = getRef(position).getKey();
                    task = model.getTask();
                    description = model.getDescription();
                    kat = model.getKat();
                    updateTask();
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_exercise, parent, false);
                return new MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTask(String task) {
            TextView taskTextView = mView.findViewById(R.id.taskTv);
            taskTextView.setText(task);
        }

        public void setDesc(String desc) {
            TextView descTextView = mView.findViewById(R.id.descriptionTv);
            descTextView.setText(desc);
        }

        public void setKat(String kat) {
            TextView katTextView = mView.findViewById(R.id.katTv);
            katTextView.setText(kat);
        }

        public void setDate(String date) { }
    }

    private void updateTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update_exbase, null);
        myDialog.setView(view);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText mTask = view.findViewById(R.id.mEditTextTask);
        final EditText mDescription = view.findViewById(R.id.mEditTextDescription);
        final EditText mKat = view.findViewById(R.id.mEditKat);

        mTask.setText(task);
        mTask.setSelection(task.length());

        mDescription.setText(description);
        mDescription.setSelection(description.length());

        mKat.setText(kat);
        mDescription.setSelection(kat.length());

        Button delButton = view.findViewById(R.id.btnDelete);
        Button updateButton = view.findViewById(R.id.btnUpdate);

        updateButton.setOnClickListener(v -> {
            String task = mTask.getText().toString().trim();
            String description = mDescription.getText().toString().trim();
            String kat = mKat.getText().toString().trim();

            String date = DateFormat.getDateInstance().format(new Date());

            Model model = new Model(task, description, key, date, kat);

            reference.child(key).setValue(model).addOnCompleteListener(soobsh -> {

                if (soobsh.isSuccessful()){
                    Toast.makeText(ExerciseBase.this, "Успешно изменено", Toast.LENGTH_SHORT).show();
                }else {
                    String err = Objects.requireNonNull(soobsh.getException()).toString();
                    Toast.makeText(ExerciseBase.this, "Ошибка изменения "+err, Toast.LENGTH_SHORT).show();
                }

            });

            dialog.dismiss();

        });

        delButton.setOnClickListener(v -> {
            reference.child(key).removeValue().addOnCompleteListener(soobsh -> {
                if (soobsh.isSuccessful()){
                    Toast.makeText(ExerciseBase.this, "Успешно удалено", Toast.LENGTH_SHORT).show();
                }else {
                    String err = Objects.requireNonNull(soobsh.getException()).toString();
                    Toast.makeText(ExerciseBase.this, "Ошибка удаления "+ err, Toast.LENGTH_SHORT).show();
                }
            });
            dialog.dismiss();
        });

        dialog.show();
    }
}