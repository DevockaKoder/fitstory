package com.example.dnevniktrenera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PhotoKlient extends AppCompatActivity {
    private ImageView photoOne, photoTwo, photoThree;
    private FirebaseAuth auth;
    private String TrenerId, currentKlientName;
    private DatabaseReference KlientRef;
    private TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_klient);
        auth = FirebaseAuth.getInstance();
        TrenerId = auth.getCurrentUser().getUid();
        currentKlientName = getIntent().getExtras().get("klientName").toString();
        KlientRef = FirebaseDatabase.getInstance().getReference("Users").child(TrenerId).child("Klients");

        photoOne = findViewById(R.id.card_image_one);
        photoTwo = findViewById(R.id.card_image_two);
        photoThree = findViewById(R.id.card_image_three);

        name = findViewById(R.id.photo_and_name);
        name.setText("Фото клиента: " + currentKlientName);


        SetPhotoToImageView();

    }
    private void SetPhotoToImageView() {
        KlientRef.child(currentKlientName).child("KlientInfo").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String imageOne = snapshot.child("imageOne").getValue(String.class);
                String imageTwo = snapshot.child("imageTwo").getValue(String.class);
                String imageThree = snapshot.child("imageThree").getValue(String.class);


                Picasso.get().load(imageOne).into(photoOne);
                Picasso.get().load(imageTwo).into(photoTwo);
                Picasso.get().load(imageThree).into(photoThree);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}