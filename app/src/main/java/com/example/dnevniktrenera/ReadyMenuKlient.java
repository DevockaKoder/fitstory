package com.example.dnevniktrenera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.dnevniktrenera.models.DinnerAdapter;
import com.example.dnevniktrenera.models.ObedAdapter;
import com.example.dnevniktrenera.models.PerekusAdapter;
import com.example.dnevniktrenera.models.ZavtrakAdapter;
import com.example.dnevniktrenera.models.MenuDinner;
import com.example.dnevniktrenera.models.MenuObed;
import com.example.dnevniktrenera.models.MenuPerekus;
import com.example.dnevniktrenera.models.MenuZavtrak;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewMergeAdapter;

public class ReadyMenuKlient extends AppCompatActivity {
    private RecyclerView recyclerViewPn;
    private DatabaseReference KlientRef;
    private String currentKlientName;
    private FirebaseAuth auth;
    private LinearLayout pdf_layout;
    ScrollView content_create;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    ZavtrakAdapter zavtrak_adapter, zavtrak_adapter_vt, zavtrak_adapter_sr,
            zavtrak_adapter_ht, zavtrak_adapter_pt, zavtrak_adapter_sb, zavtrak_adapter_vs;
    ObedAdapter obed_adapter, obed_adapter_vt, obed_adapter_sr,
            obed_adapter_ht, obed_adapter_pt, obed_adapter_sb, obed_adapter_vs;
    PerekusAdapter perekus_adapter, perekus_adapter_vt, perekus_adapter_sr,
            perekus_adapter_ht, perekus_adapter_pt, perekus_adapter_sb, perekus_adapter_vs;
    DinnerAdapter dinner_adapter, dinner_adapter_vt, dinner_adapter_sr,
            dinner_adapter_ht, dinner_adapter_pt, dinner_adapter_sb, dinner_adapter_vs;

    ArrayList<MenuZavtrak> list_zavtrak, list_zavtrak_vt, list_zavtrak_sr,
            list_zavtrak_ht, list_zavtrak_pt, list_zavtrak_sb, list_zavtrak_vs ;
    ArrayList<MenuObed> list_obed, list_obed_vt, list_obed_sr, list_obed_ht,
            list_obed_pt, list_obed_sb, list_obed_vs;
    ArrayList<MenuPerekus> list_perekus, list_perekus_vt, list_perekus_sr,
            list_perekus_ht, list_perekus_pt, list_perekus_sb, list_perekus_vs;
    ArrayList<MenuDinner> list_dinner, list_dinner_vt, list_dinner_sr,
            list_dinner_ht, list_dinner_pt, list_dinner_sb, list_dinner_vs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_menu_klient);
        recyclerViewPn = findViewById(R.id.recyclerViewPn);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        /*linearLayoutManager.setReverseLayout(true);*/

        content_create = findViewById(R.id.content_create);
        pdf_layout = findViewById(R.id.pdf_linear);
        init();
        recyclerViewPn.setHasFixedSize(true);
        recyclerViewPn.setLayoutManager(linearLayoutManager);

        currentKlientName = getIntent().getExtras().get("klientName").toString();
        auth = FirebaseAuth.getInstance();
        String trenerId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        KlientRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(trenerId).child("Klients").child(currentKlientName).child("Меню на неделю");


        RecyclerViewMergeAdapter mergeAdapter = new RecyclerViewMergeAdapter();

        list_zavtrak = new ArrayList<>();
        zavtrak_adapter = new ZavtrakAdapter(this, list_zavtrak);

        list_obed = new ArrayList<>();
        obed_adapter = new ObedAdapter(this, list_obed);

        list_perekus = new ArrayList<>();
        perekus_adapter = new PerekusAdapter(this, list_perekus);

        list_dinner = new ArrayList<>();
        dinner_adapter = new DinnerAdapter(this, list_dinner);

        mergeAdapter.addAdapter(zavtrak_adapter);
        mergeAdapter.addAdapter(obed_adapter);
        mergeAdapter.addAdapter(perekus_adapter);
        mergeAdapter.addAdapter(dinner_adapter);

        list_zavtrak_vt = new ArrayList<>();
        zavtrak_adapter_vt = new ZavtrakAdapter(this, list_zavtrak_vt);

        list_obed_vt = new ArrayList<>();
        obed_adapter_vt = new ObedAdapter(this, list_obed_vt);

        list_perekus_vt = new ArrayList<>();
        perekus_adapter_vt = new PerekusAdapter(this, list_perekus_vt);

        list_dinner_vt = new ArrayList<>();
        dinner_adapter_vt = new DinnerAdapter(this, list_dinner_vt);

        mergeAdapter.addAdapter(zavtrak_adapter_vt);
        mergeAdapter.addAdapter(obed_adapter_vt);
        mergeAdapter.addAdapter(perekus_adapter_vt);
        mergeAdapter.addAdapter(dinner_adapter_vt);

        list_zavtrak_sr = new ArrayList<>();
        zavtrak_adapter_sr = new ZavtrakAdapter(this, list_zavtrak_sr);

        list_obed_sr = new ArrayList<>();
        obed_adapter_sr = new ObedAdapter(this, list_obed_sr);

        list_perekus_sr = new ArrayList<>();
        perekus_adapter_sr = new PerekusAdapter(this, list_perekus_sr);

        list_dinner_sr = new ArrayList<>();
        dinner_adapter_sr = new DinnerAdapter(this, list_dinner_sr);

        mergeAdapter.addAdapter(zavtrak_adapter_sr);
        mergeAdapter.addAdapter(obed_adapter_sr);
        mergeAdapter.addAdapter(perekus_adapter_sr);
        mergeAdapter.addAdapter(dinner_adapter_sr);

        list_zavtrak_ht = new ArrayList<>();
        zavtrak_adapter_ht = new ZavtrakAdapter(this, list_zavtrak_ht);

        list_obed_ht = new ArrayList<>();
        obed_adapter_ht = new ObedAdapter(this, list_obed_ht);

        list_perekus_ht = new ArrayList<>();
        perekus_adapter_ht = new PerekusAdapter(this, list_perekus_ht);

        list_dinner_ht = new ArrayList<>();
        dinner_adapter_ht = new DinnerAdapter(this, list_dinner_ht);

        mergeAdapter.addAdapter(zavtrak_adapter_ht);
        mergeAdapter.addAdapter(obed_adapter_ht);
        mergeAdapter.addAdapter(perekus_adapter_ht);
        mergeAdapter.addAdapter(dinner_adapter_ht);

        list_zavtrak_pt = new ArrayList<>();
        zavtrak_adapter_pt = new ZavtrakAdapter(this, list_zavtrak_pt);

        list_obed_pt = new ArrayList<>();
        obed_adapter_pt = new ObedAdapter(this, list_obed_pt);

        list_perekus_pt = new ArrayList<>();
        perekus_adapter_pt = new PerekusAdapter(this, list_perekus_pt);

        list_dinner_pt = new ArrayList<>();
        dinner_adapter_pt = new DinnerAdapter(this, list_dinner_pt);

        mergeAdapter.addAdapter(zavtrak_adapter_pt);
        mergeAdapter.addAdapter(obed_adapter_pt);
        mergeAdapter.addAdapter(perekus_adapter_pt);
        mergeAdapter.addAdapter(dinner_adapter_pt);

        list_zavtrak_sb = new ArrayList<>();
        zavtrak_adapter_sb = new ZavtrakAdapter(this, list_zavtrak_sb);

        list_obed_sb = new ArrayList<>();
        obed_adapter_sb = new ObedAdapter(this, list_obed_sb);

        list_perekus_sb = new ArrayList<>();
        perekus_adapter_sb = new PerekusAdapter(this, list_perekus_sb);

        list_dinner_sb = new ArrayList<>();
        dinner_adapter_sb = new DinnerAdapter(this, list_dinner_sb);

        mergeAdapter.addAdapter(zavtrak_adapter_sb);
        mergeAdapter.addAdapter(obed_adapter_sb);
        mergeAdapter.addAdapter(perekus_adapter_sb);
        mergeAdapter.addAdapter(dinner_adapter_sb);

        list_zavtrak_vs = new ArrayList<>();
        zavtrak_adapter_vs = new ZavtrakAdapter(this, list_zavtrak_vs);

        list_obed_vs = new ArrayList<>();
        obed_adapter_vs = new ObedAdapter(this, list_obed_vs);

        list_perekus_vs = new ArrayList<>();
        perekus_adapter_vs = new PerekusAdapter(this, list_perekus_vs);

        list_dinner_vs = new ArrayList<>();
        dinner_adapter_vs = new DinnerAdapter(this, list_dinner_vs);

        mergeAdapter.addAdapter(zavtrak_adapter_vs);
        mergeAdapter.addAdapter(obed_adapter_vs);
        mergeAdapter.addAdapter(perekus_adapter_vs);
        mergeAdapter.addAdapter(dinner_adapter_vs);

        recyclerViewPn.setAdapter(mergeAdapter);

        SetMenuFromBase();


    }

    private void SetMenuFromBase() {
        /*Понедельник*/
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuZavtrak menuZavtrak = snapshot.child("Понедельник").child("Завтрак").getValue(MenuZavtrak.class);
                list_zavtrak.add(menuZavtrak);
                zavtrak_adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuObed menuObed = snapshot.child("Понедельник").child("Обед").getValue(MenuObed.class);
                list_obed.add(menuObed);
                obed_adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuPerekus menuPerekus = snapshot.child("Понедельник").child("Перекус").getValue(MenuPerekus.class);
                list_perekus.add(menuPerekus);
                perekus_adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuDinner menuDinner = snapshot.child("Понедельник").child("Ужин").getValue(MenuDinner.class);
                list_dinner.add(menuDinner);
                dinner_adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        /*Вторник*/
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuZavtrak menuZavtrakVt = snapshot.child("Вторник").child("Завтрак").getValue(MenuZavtrak.class);
                list_zavtrak_vt.add(menuZavtrakVt);
                zavtrak_adapter_vt.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuObed menuObedVt = snapshot.child("Вторник").child("Обед").getValue(MenuObed.class);
                list_obed_vt.add(menuObedVt);
                obed_adapter_vt.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuPerekus menuPerekusVt = snapshot.child("Вторник").child("Перекус").getValue(MenuPerekus.class);
                list_perekus_vt.add(menuPerekusVt);
                perekus_adapter_vt.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuDinner menuDinnerVt = snapshot.child("Вторник").child("Ужин").getValue(MenuDinner.class);
                list_dinner_vt.add(menuDinnerVt);
                dinner_adapter_vt.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        /*Среда*/
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuZavtrak menuZavtrakVt = snapshot.child("Среда").child("Завтрак").getValue(MenuZavtrak.class);
                list_zavtrak_sr.add(menuZavtrakVt);
                zavtrak_adapter_sr.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuObed menuObedVt = snapshot.child("Среда").child("Обед").getValue(MenuObed.class);
                list_obed_sr.add(menuObedVt);
                obed_adapter_sr.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuPerekus menuPerekusVt = snapshot.child("Среда").child("Перекус").getValue(MenuPerekus.class);
                list_perekus_sr.add(menuPerekusVt);
                perekus_adapter_sr.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuDinner menuDinnerVt = snapshot.child("Среда").child("Ужин").getValue(MenuDinner.class);
                list_dinner_sr.add(menuDinnerVt);
                dinner_adapter_sr.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        /*Четверг*/
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuZavtrak menuZavtrakVt = snapshot.child("Четверг").child("Завтрак").getValue(MenuZavtrak.class);
                list_zavtrak_ht.add(menuZavtrakVt);
                zavtrak_adapter_ht.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuObed menuObedVt = snapshot.child("Четверг").child("Обед").getValue(MenuObed.class);
                list_obed_ht.add(menuObedVt);
                obed_adapter_ht.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuPerekus menuPerekusVt = snapshot.child("Четверг").child("Перекус").getValue(MenuPerekus.class);
                list_perekus_ht.add(menuPerekusVt);
                perekus_adapter_ht.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuDinner menuDinnerVt = snapshot.child("Четверг").child("Ужин").getValue(MenuDinner.class);
                list_dinner_ht.add(menuDinnerVt);
                dinner_adapter_ht.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        /*Пятница*/
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuZavtrak menuZavtrakVt = snapshot.child("Пятница").child("Завтрак").getValue(MenuZavtrak.class);
                list_zavtrak_pt.add(menuZavtrakVt);
                zavtrak_adapter_pt.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuObed menuObedVt = snapshot.child("Пятница").child("Обед").getValue(MenuObed.class);
                list_obed_pt.add(menuObedVt);
                obed_adapter_pt.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuPerekus menuPerekusVt = snapshot.child("Пятница").child("Перекус").getValue(MenuPerekus.class);
                list_perekus_pt.add(menuPerekusVt);
                perekus_adapter_pt.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuDinner menuDinnerVt = snapshot.child("Пятница").child("Ужин").getValue(MenuDinner.class);
                list_dinner_pt.add(menuDinnerVt);
                dinner_adapter_pt.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        /*Суббота*/
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuZavtrak menuZavtrakVt = snapshot.child("Суббота").child("Завтрак").getValue(MenuZavtrak.class);
                list_zavtrak_sb.add(menuZavtrakVt);
                zavtrak_adapter_sb.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuObed menuObedVt = snapshot.child("Суббота").child("Обед").getValue(MenuObed.class);
                list_obed_sb.add(menuObedVt);
                obed_adapter_sb.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuPerekus menuPerekusVt = snapshot.child("Суббота").child("Перекус").getValue(MenuPerekus.class);
                list_perekus_sb.add(menuPerekusVt);
                perekus_adapter_sb.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuDinner menuDinnerVt = snapshot.child("Суббота").child("Ужин").getValue(MenuDinner.class);
                list_dinner_sb.add(menuDinnerVt);
                dinner_adapter_sb.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        /*Воскресенье*/
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuZavtrak menuZavtrakVt = snapshot.child("Воскресенье").child("Завтрак").getValue(MenuZavtrak.class);
                list_zavtrak_vs.add(menuZavtrakVt);
                zavtrak_adapter_vs.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuObed menuObedVt = snapshot.child("Воскресенье").child("Обед").getValue(MenuObed.class);
                list_obed_vs.add(menuObedVt);
                obed_adapter_vs.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuPerekus menuPerekusVt = snapshot.child("Воскресенье").child("Перекус").getValue(MenuPerekus.class);
                list_perekus_vs.add(menuPerekusVt);
                perekus_adapter_vs.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        KlientRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MenuDinner menuDinnerVt = snapshot.child("Воскресенье").child("Ужин").getValue(MenuDinner.class);
                list_dinner_vs.add(menuDinnerVt);
                dinner_adapter_vs.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_program, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.menu_save){
          generatePDF();
        }

        return true;
    }
    public static int getActivityHeight(Activity activity)
    {
        return activity.findViewById(R.id.pdf_linear).getHeight();
    }
    public static int getActivityWidth(Activity activity)
    {
        return activity.findViewById(R.id.pdf_linear).getWidth();
    }


    private void generatePDF() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float height =content_create.getHeight();
        float width = content_create.getWidth();
        String file_name = "/" + currentKlientName + "_меню на неделю.pdf";

        int convertHeight = (int) height, convertWidth = (int) width;

        // создаем документ
        PdfDocument document = new PdfDocument();
        // определяем размер страницы
        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(convertWidth, convertHeight, 8).create();
        // получаем страницу, на котором будем генерировать контент
        PdfDocument.Page page = document.startPage(pageInfo);

        // получаем холст (Canvas) страницы
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        // получаем контент, который нужно добавить в PDF, и загружаем его в Bitmap
        Bitmap bitmap = loadBitmapFromView(content_create, content_create.getWidth(), content_create.getHeight());
        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHeight, true);

        // рисуем содержимое и закрываем страницу
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Menu");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        verifyStoragePermissions(ReadyMenuKlient.this);

        // сохраняем записанный контент
        String targetPdf = dir.getAbsolutePath()  + file_name ;
        File filePath = new File(targetPdf);
        try {

            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(getApplicationContext(), "PDf сохранён в " + filePath.getAbsolutePath(),
                    Toast.LENGTH_SHORT).show();
            // обновляем список initViews();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Что-то пошло не так: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // закрываем документ
        document.close();
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }


    private void init(){
        Toolbar toolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(toolbar);
        String klientName = getIntent().getExtras().get("klientName").toString();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Клиент " + klientName);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}