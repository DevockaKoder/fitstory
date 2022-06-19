package com.example.dnevniktrenera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dnevniktrenera.NewKlientActivity;
import com.example.dnevniktrenera.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    AppCompatImageButton btnSignIn, btnRegister;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    FirebaseUser currentUser;
    /*private DatabaseReference rootRef;*/


    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources resources = getResources();
        int textColor = resources.getColor(R.color.teal_300,  null);
        setContentView(R.layout.activity_main);
        /*Записываем в переменные айди кнопок*/
        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);
        /*Записываем в переменные айди relativelayot для вывода подсказок в дальнейшем*/
        root = findViewById(R.id.root_element);
        /*Подключаем данные в три переменные auth, db, users*/
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        /*Вносим в переменную данные об авторизованном пользователе*/
        currentUser = auth.getCurrentUser();
        /*Создаем обработчик события для кнопки зарегистрироваться*/
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterWindow();
            }
        });
        /*Создаем обработчик события для кнопки войти*/
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInWindow();
            }
        });

    }


    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogTheme));
        dialog.setTitle("Войти");
        dialog.setMessage("Введите все данные для входа");

        LayoutInflater inflater = LayoutInflater.from(this);
        View sign_in_window = inflater.inflate(R.layout.sign_in_window, null);
        dialog.setView(sign_in_window);

        /*Получаем данные из полей ввода пользовательских данных*/
        final MaterialEditText email = sign_in_window.findViewById(R.id.emailField);
        final MaterialEditText pass = sign_in_window.findViewById(R.id.pass_field);

        /*Создаем две кнопки:
        для закрытия формы регистрации и для отправки данных для завершения регистрации*/
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*Обрабатываем 4 возможных ошибки - пользователь не ввел данные и нажал отправить*/
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (pass.getText().toString().length() < 6) {
                    Snackbar.make(root, "Пароль должен быть больше 6 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user.isEmailVerified()){
                                    startActivity(new Intent(MainActivity.this, MapActivity.class));
                                    finish();
                                } else {
                                    user.sendEmailVerification();
                                    Snackbar.make(root, "Проверьте почту", Snackbar.LENGTH_LONG)
                                            .show();
                                }
                            }
                        }).addOnFailureListener(e ->
                                Snackbar.make(root, "Ошибка авторизации. " + e.getMessage(), Snackbar.LENGTH_LONG)
                                .show());
            }
        });
        dialog.show();

    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogTheme));
        dialog.setTitle("Регистрация");
        dialog.setMessage("Введите все данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_window = inflater.inflate(R.layout.register_window, null);
        dialog.setView(register_window);

        /*Получаем данные из полей ввода пользовательских данных*/
        final MaterialEditText email = register_window.findViewById(R.id.emailField);
        final MaterialEditText pass = register_window.findViewById(R.id.pass_field);
        final MaterialEditText name = register_window.findViewById(R.id.name_field);
        final MaterialEditText phone = register_window.findViewById(R.id.phone_field);

        /*Создаем две кнопки:
        для закрытия формы регистрации и для отправки данных для завершения регистрации*/
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Зарегистрироваться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*Обрабатываем 4 возможных ошибки - пользователь не ввел данные и нажал отправить*/
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(root, "Введи имя и фамилию", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Snackbar.make(root, "Введите ваш телефон", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (pass.getText().toString().length() < 6) {
                    Snackbar.make(root, "Пароль должен быть больше 6 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                /*Регистрация пользователя*/
                auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(email.getText().toString());
                                user.setPass(pass.getText().toString());
                                user.setName(name.getText().toString());
                                user.setPhone(phone.getText().toString());

                                /*Передаем все данные пользователя в таблицу users, ключом будет id*/
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Data")
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Snackbar.make(root, "Пользователь добавлен", Snackbar.LENGTH_LONG)
                                                        .show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(root, "Ошибка регистрации" + e.getMessage(), Snackbar.LENGTH_SHORT)
                                                        .show();
                                    }
                                });
                            }
                        });
            }
        });
        dialog.show();
    }

    /*Код для зарегистрированных пользователей (начинают сразу с активити с кнопками) */
  @Override
    protected void onStart() {
        super.onStart();
        if(currentUser != null) {
            startActivity(new Intent(MainActivity.this, MapActivity.class));
        } else {
            Toast.makeText(MainActivity.this, "Необходимо войти в учетную запись", Toast.LENGTH_SHORT).show();
        }
    }

}