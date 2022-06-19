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

import com.example.dnevniktrenera.models.ModelRecipes;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class RecipeBase extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private ProgressBar loader;

    private String key = "";
    private String nameRecipe;
    private String recipe;
    private String calories;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recipe_base);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerViewRecipe);
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
        reference = FirebaseDatabase.getInstance().getReference("Users").child(onlineUserID).child("Recipes");

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> addTask());
    }

    private void addTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.input_file_recbase, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText nameRecipe = myView.findViewById(R.id.name_recipe);
        final EditText calories = myView.findViewById(R.id.calories_recipe);
        final EditText recipe = myView.findViewById(R.id.recipe);
        final EditText type = myView.findViewById(R.id.type);

        Button save = myView.findViewById(R.id.saveBtn);
        Button cancel = myView.findViewById(R.id.CancelBtn);

        cancel.setOnClickListener(v -> dialog.dismiss());

        save.setOnClickListener(v -> {
            String mName = nameRecipe.getText().toString().trim();
            String mCalories = calories.getText().toString().trim();
            String mRecipe = recipe.getText().toString().trim();
            String mType = type.getText().toString().trim();
            String id = reference.push().getKey();


            if (TextUtils.isEmpty(mName)) {
                nameRecipe.setError("Введите название");
                return;
            }
            if (TextUtils.isEmpty(mCalories)) {
                calories.setError("Введите КБЖУ");
                return;
            }
            if (TextUtils.isEmpty(mRecipe)) {
                recipe.setError("Введите описание");
                return;
            }
            if (TextUtils.isEmpty(mType)) {
                type.setError("Введите категорию");
                return;
            } else {
                loader.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                ModelRecipes model = new ModelRecipes(mName, mCalories, mRecipe, id, mType);
                assert id != null;
                reference.child(mName).setValue(model).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(RecipeBase.this, "Успешно добавлено", Toast.LENGTH_SHORT).show();
                    } else {
                        String error = Objects.requireNonNull(task1.getException()).toString();
                        Toast.makeText(RecipeBase.this, "Ошибка: " + error, Toast.LENGTH_SHORT).show();
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

        FirebaseRecyclerOptions<ModelRecipes> options = new FirebaseRecyclerOptions.Builder<ModelRecipes>()
                .setQuery(reference, ModelRecipes.class)
                .build();

        FirebaseRecyclerAdapter<ModelRecipes, MyViewHolderRecipe> adapter = new FirebaseRecyclerAdapter<ModelRecipes, MyViewHolderRecipe>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolderRecipe holder, final int position, @NonNull final ModelRecipes model) {
                holder.setName(model.getNameRecipe());
                holder.setCalories(model.getCalories());
                holder.setRecipe(model.getRecipe());
                holder.setType(model.getType());


                holder.mView.setOnClickListener(v -> {
                    key = getRef(position).getKey();
                    nameRecipe = model.getNameRecipe();
                    calories = model.getCalories();
                    recipe = model.getRecipe();
                    type = model.getType();
                    updateTask();
                });
            }

            @NonNull
            @Override
            public MyViewHolderRecipe onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reciepes, parent, false);
                return new MyViewHolderRecipe(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class MyViewHolderRecipe extends RecyclerView.ViewHolder {
        View mView;

        public MyViewHolderRecipe(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView nameTextView = mView.findViewById(R.id.nameTv);
            nameTextView.setText(name);
        }
        public void setCalories(String calories) {
            TextView caloriesTextView = mView.findViewById(R.id.caloriesTv);
            caloriesTextView.setText(calories);
        }

        public void setRecipe(String recipe) {
            TextView recipeTextView = mView.findViewById(R.id.recipeTv);
            recipeTextView.setText(recipe);
        }

        public void setType(String type) {
            TextView typeTextView = mView.findViewById(R.id.typeTv);
            typeTextView.setText(type);
        }

    }

    private void updateTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.update_recbase, null);
        myDialog.setView(view);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText mName = view.findViewById(R.id.mEditTextName);
        final EditText mCalories = view.findViewById(R.id.mEditTextCalories);
        final EditText mRecipe = view.findViewById(R.id.mEditTextRecipe);
        final EditText mType = view.findViewById(R.id.mEditType);

        mName.setText(nameRecipe);
        mName.setSelection(nameRecipe.length());

        mCalories.setText(calories);
        mCalories.setSelection(calories.length());

        mRecipe.setText(recipe);
        mRecipe.setSelection(recipe.length());

        mType.setText(type);
        mType.setSelection(type.length());

        Button delButton = view.findViewById(R.id.btnDelete);
        Button updateButton = view.findViewById(R.id.btnUpdate);

        updateButton.setOnClickListener(v -> {
            String name = mName.getText().toString().trim();
            String calories = mCalories.getText().toString().trim();
            String recipe = mRecipe.getText().toString().trim();
            String type = mType.getText().toString().trim();


            ModelRecipes model = new ModelRecipes(name, calories, recipe, key, type);

            reference.child(nameRecipe).setValue(model).addOnCompleteListener(soobsh -> {

                if (soobsh.isSuccessful()){
                    Toast.makeText(RecipeBase.this, "Успешно изменено", Toast.LENGTH_SHORT).show();
                }else {
                    String err = Objects.requireNonNull(soobsh.getException()).toString();
                    Toast.makeText(RecipeBase.this, "Ошибка изменения "+err, Toast.LENGTH_SHORT).show();
                }

            });

            dialog.dismiss();

        });

        delButton.setOnClickListener(v -> {
            reference.child(nameRecipe).removeValue().addOnCompleteListener(soobsh -> {
                if (soobsh.isSuccessful()){
                    Toast.makeText(RecipeBase.this, "Успешно удалено", Toast.LENGTH_SHORT).show();
                }else {
                    String err = Objects.requireNonNull(soobsh.getException()).toString();
                    Toast.makeText(RecipeBase.this, "Ошибка удаления "+ err, Toast.LENGTH_SHORT).show();
                }
            });
            dialog.dismiss();
        });

        dialog.show();
    }
}