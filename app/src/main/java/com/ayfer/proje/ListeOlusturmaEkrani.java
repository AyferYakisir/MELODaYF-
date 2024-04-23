package com.ayfer.proje;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ListeOlusturmaEkrani extends AppCompatActivity {
    private RecyclerView recyclerView;
    private listeAdapter adapter;
    private final List<muzikListesi> muzikListesiList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decodeView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decodeView.setSystemUiVisibility(option);

        setContentView(R.layout.activity_liste_olusturma_ekrani);

        recyclerView = findViewById(R.id.muzikListesi);
        final ImageButton geriButonu = findViewById(R.id.geriButonu);
        final ImageButton listeOlusturmaButonu = findViewById(R.id.okButonu);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        geriButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListeOlusturmaEkrani.this, sarkilarEkrani.class);
                startActivity(intent);
            }
        });
        listeOlusturmaButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder((ListeOlusturmaEkrani.this));
                builder.setTitle(("Çalma Listesi Oluştur"));
                builder.setMessage("Çalma Listesi Adını Giriniz:");

                final EditText giris = new EditText(ListeOlusturmaEkrani.this);
                LinearLayout.LayoutParams gr = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                giris.setLayoutParams(gr);

                builder.setView(giris);
                // Onayla ve İptal düğmelerini ekle
                builder.setPositiveButton("Oluştur", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Kullanıcının girdiği adı al
                        String calmaListesiAdi = giris.getText().toString();

                        // Çalma listesi adını kullanarak işlemleri yapabilirsiniz
                        // Örneğin, bu adla bir klasör oluşturabilir ve seçilen şarkıları bu klasöre ekleyebilirsiniz
                        Toast.makeText(ListeOlusturmaEkrani.this, "Çalma listesi oluşturuldu: " + calmaListesiAdi, Toast.LENGTH_SHORT).show();

                        // Seçilen şarkıları al
                        List<muzikListesi> secilenSarkilar = adapter.getSelectedSongs();

                        // Çalma listesi ekranına geçiş yap
                        Intent intent = new Intent(ListeOlusturmaEkrani.this, calmaListesiEkrani.class);
                        // Yeni oluşturulan çalma listesi adını intent ile gönder
                        intent.putExtra("calmaListesiAdi", calmaListesiAdi);
                        // Seçilen şarkıları da intent ile gönder
                        intent.putExtra("secilenSarkilar", new ArrayList<>(secilenSarkilar));
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // AlertDialog'u göster
                builder.show();
            }
        });


        // İzin talebi için kontrol
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6.0 ve üzeri sürümlerde izin talep işlemini gerçekleştir
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // İzin talep edilmediyse, izin talep işlemini başlat
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        11);
            } else {
                // İzin zaten verilmişse, müzik dosyalarını al
                getMuzikDosyaları();
            }
        } else {
            // Android 6.0'dan önceki sürümlerde, izin talebi gerekmez
            getMuzikDosyaları();
        }
    }

    @SuppressLint("Range")
    private void getMuzikDosyaları() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, MediaStore.Audio.Media.IS_MUSIC + "=?", new String[]{"1"}, null);

        if (cursor == null) {
            Toast.makeText(this, "Hata: Müzik dosyaları alınamadı", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!cursor.moveToFirst()) {
            Toast.makeText(this, "Şarkı bulunamadı", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        do {
            String muzikDosyasiAdi = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String sanatciAdi = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long cursorId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            Uri muzikDosyasiUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursorId);
            String sure = "00:00";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                sure = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
            }
            muzikListesi muziklist = new muzikListesi(muzikDosyasiAdi, sanatciAdi, sure, false, muzikDosyasiUri, false);
            muzikListesiList.add(muziklist);
        } while (cursor.moveToNext());
        cursor.close();
        if (!muzikListesiList.isEmpty()) {
            adapter = new listeAdapter(muzikListesiList, ListeOlusturmaEkrani.this);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Müzik dosyaları bulunamadı", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11) { // İzin talep kodu
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // İzin verildiğinde işlemleri yeniden başlat
                getMuzikDosyaları();
            } else {
                // İzin reddedildiğinde kullanıcıya bilgi ver
                Toast.makeText(this, "izin verilmedi.", Toast.LENGTH_LONG).show();

                // İzinleri tekrar talep etme
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decodeView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decodeView.setSystemUiVisibility(option);
        }
    }
}
