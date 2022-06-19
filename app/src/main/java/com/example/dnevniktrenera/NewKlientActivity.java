package com.example.dnevniktrenera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class NewKlientActivity extends AppCompatActivity {

    private String Name, Phone, Age, Weight, Growth, TrenerId, saveCurrentDate, saveCurrentTime, randomKey;
    private String downloadImagePeredUrl, downloadImageSpinaUrl, downloadImageBokUrl;
    private ImageView klientImage, klientImageSpina, klientImageBok;
    private EditText klientName, klientPhone, klientAge, klientWeight, klientGrowth;
    private AppCompatImageButton addNewKlientButton;
    private static int GALLERYPICK = 1;
    private static int GALLERYPICK2 = 1;
    private static int GALLERYPICK3 = 1;
    private Uri ImageUri_pered, ImageUri_bok, ImageUri_spina;
    private StorageReference KlientImageRef;
    private DatabaseReference KlientsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_klient);

        init();

        klientImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
        klientImageSpina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery2();
            }
        });
        klientImageBok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery3();
            }
        });

        addNewKlientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();
            }});
    }

    private void ValidateProductData() {
        Name = klientName.getText().toString();
        Phone = klientPhone.getText().toString();
        Age = klientAge.getText().toString();
        Weight = klientWeight.getText().toString();
        Growth = klientGrowth.getText().toString();

        if((ImageUri_pered == null) && (ImageUri_bok == null) && (ImageUri_spina == null)){
            Toast.makeText(this, "Добавьте фотографии клиента", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Phone)){
            Toast.makeText(this, "Укажите номер клиента", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Age)){
            Toast.makeText(this, "Укажите возраст", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Name)){
            Toast.makeText(this, "Укажите имя клиента", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Weight)){
            Toast.makeText(this, "Укажите вес клиента", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Growth)){
            Toast.makeText(this, "Укажите рост клиента", Toast.LENGTH_SHORT).show();
        }
        else {
            KlientsInformation();
        }
    }

    private void KlientsInformation() {

        loadingBar.setTitle("Загрузка данных");
        loadingBar.setMessage("Пожалуйста, подождите...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HHmmss");
        saveCurrentTime = currentTime.format(calendar.getTime());

        randomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = KlientImageRef.child("фото_спереди_" + ImageUri_pered.getLastPathSegment() + randomKey + ".jpg");
        final StorageReference filePath_spina = KlientImageRef.child("фото_со_спины_" + ImageUri_spina.getLastPathSegment() + randomKey + ".jpg");
        final StorageReference filePath_bok = KlientImageRef.child("фото_cбоку_" + ImageUri_bok.getLastPathSegment() + randomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri_pered);
        final UploadTask uploadTask_spina = filePath_spina.putFile(ImageUri_spina);
        final UploadTask uploadTask_bok = filePath_bok.putFile(ImageUri_bok);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(NewKlientActivity.this, "Ошибка: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(NewKlientActivity.this, "Изображения успешно загружены.", Toast.LENGTH_SHORT).show();

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImagePeredUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImagePeredUrl = task.getResult().toString();
                            Toast.makeText(NewKlientActivity.this, "Фото сохранены", Toast.LENGTH_SHORT).show();
                           /* SaveProductInfoToDatabase();*/
                        }
                    }
                });
            }
        });
        uploadTask_spina.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(NewKlientActivity.this, "Ошибка: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                uploadTask_spina.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageSpinaUrl = filePath_spina.getDownloadUrl().toString();
                        return filePath_spina.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageSpinaUrl = task.getResult().toString();
                            /*SaveProductInfoToDatabase();*/
                        }
                    }
                });
            }
        });
        uploadTask_bok.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(NewKlientActivity.this, "Ошибка: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                uploadTask_bok.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageBokUrl = filePath_bok.getDownloadUrl().toString();
                        return filePath_bok.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageBokUrl = task.getResult().toString();
                            SaveProductInfoToDatabase();
                            Toast.makeText(NewKlientActivity.this, "Теперь клиент есть в вашем списке", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> klientMap = new HashMap<>();

        klientMap.put("kid", randomKey);
        klientMap.put("date", saveCurrentDate);
        klientMap.put("time", saveCurrentTime);
        klientMap.put("imageOne", downloadImagePeredUrl);
        klientMap.put("imageTwo", downloadImageSpinaUrl);
        klientMap.put("imageThree", downloadImageBokUrl);
        klientMap.put("name", Name);
        klientMap.put("phone", Phone);
        klientMap.put("age", Age);
        klientMap.put("weight", Weight);
        klientMap.put("growth", Growth);
        klientMap.put("uid", TrenerId);


        KlientsRef.child(Name).child("KlientInfo").updateChildren(klientMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            loadingBar.dismiss();

                            Intent SpisokKlientsIntent = new Intent(NewKlientActivity.this, KlientsList.class);
                            startActivity(SpisokKlientsIntent);
                        }
                        else {
                            String message = task.getException().toString();
                            Toast.makeText(NewKlientActivity.this, "Ошибка: "+ message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
    }
    private void OpenGallery() {
        GALLERYPICK = 1;
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERYPICK);
    }
    private void OpenGallery2() {
        GALLERYPICK2 = 1;
        Intent galleryIntent2 = new Intent();
        galleryIntent2.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent2.setType("image/*");
        startActivityForResult(galleryIntent2,GALLERYPICK2);
    }
    private void OpenGallery3() {
        GALLERYPICK3 = 1;
        Intent galleryIntent3 = new Intent();
        galleryIntent3.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent3.setType("image/*");
        startActivityForResult(galleryIntent3,GALLERYPICK3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERYPICK && resultCode == RESULT_OK){
            ImageUri_pered = data.getData();
            klientImage.setImageURI(ImageUri_pered);
            GALLERYPICK = 0;
        } else if(requestCode == GALLERYPICK2 && resultCode == RESULT_OK) {
            ImageUri_spina = data.getData();
            klientImageSpina.setImageURI(ImageUri_spina);
            GALLERYPICK2 = 0;
        } else if(requestCode == GALLERYPICK3 && resultCode == RESULT_OK) {
            ImageUri_bok = data.getData();
            klientImageBok.setImageURI(ImageUri_bok);
            GALLERYPICK3 = 0;
        }

    }


    private void init() {

        klientImage = findViewById(R.id.select_klient_image);
        klientImageSpina = findViewById(R.id.select_klient_image_spina);
        klientImageBok = findViewById(R.id.select_klient_image_bok);
        klientName = findViewById(R.id.klient_name);
        klientPhone = findViewById(R.id.klient_phone);
        klientAge = findViewById(R.id.klient_age);
        klientWeight = findViewById(R.id.klient_weight);
        klientGrowth = findViewById(R.id.klient_growth);
        TrenerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        addNewKlientButton = findViewById(R.id.add_new_client_btn);
        KlientImageRef = FirebaseStorage.getInstance().getReference().child("Klient Images");
        KlientsRef = FirebaseDatabase.getInstance().getReference("Users").child(TrenerId).child("Klients");
        loadingBar = new ProgressDialog(this);

    }
}