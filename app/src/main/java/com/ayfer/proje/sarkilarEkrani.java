package com.ayfer.proje;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;

public class sarkilarEkrani extends AppCompatActivity implements sarkiDegistirici {
    private final List<muzikListesi> muzikListesiList =new ArrayList<>();
   private RecyclerView muzikRecyclerView;
   private MediaPlayer medyaOynatici;
   private TextView bitisZamani,baslamaZamani;

   private boolean calisiyorMu = false;
   private SeekBar oynatmaCubugu;
   private  ImageView oynatmaButonuImg;
   private Timer timer;
   private int calınanSarkiListesiPsition=0;
   private muzikAdapter muzikAdaptör;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decodeView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decodeView.setSystemUiVisibility(option);

        setContentView(R.layout.activity_sarkilar_ekrani);

        final LinearLayout aramaButonu = findViewById(R.id.aramaButonu);
        final LinearLayout menuButonu = findViewById(R.id.menuButonu);
        muzikRecyclerView  = findViewById(R.id.muzikListesi);
        final CardView oynatmaButonu = findViewById(R.id.oynatmaButonu);
        oynatmaButonuImg = findViewById(R.id.oynatmaButonuImg);
        final ImageView sonrakiButonu = findViewById(R.id.sonrakiButonu);
        final ImageView oncekiButonu = findViewById(R.id.oncekiButonu);
        final ImageButton geriButonu = findViewById(R.id.geriButonu);
        final ImageButton listeButonu = findViewById(R.id.listeButonu);

        baslamaZamani = findViewById(R.id.baslamaZamani);
        bitisZamani = findViewById(R.id.bitisZamani);
        oynatmaCubugu = findViewById(R.id.oynatmaCubugu);

        RecyclerView muzikListesi = findViewById(R.id.muzikListesi);
        muzikListesi.setHasFixedSize(true);
        muzikListesi.setLayoutManager(new LinearLayoutManager(this));

        medyaOynatici = new MediaPlayer();

        geriButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(sarkilarEkrani.this,anaEkran.class);
                startActivity(intent);
            }
        });
        listeButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sarkilarEkrani.this, ListeOlusturmaEkrani.class);
                startActivity(intent);
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

        sonrakiButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sonrakiSarkiListesiPosition = calınanSarkiListesiPsition+1;
                if(sonrakiSarkiListesiPosition>=muzikListesiList.size()){
                    sonrakiSarkiListesiPosition = 0;
                }
                muzikListesiList.get(calınanSarkiListesiPsition).setCaliyorMu(false);
                muzikListesiList.get(sonrakiSarkiListesiPosition).setCaliyorMu(true);

                muzikAdaptör.guncelListe(muzikListesiList);
                muzikRecyclerView.scrollToPosition(sonrakiSarkiListesiPosition);
                onChanged(sonrakiSarkiListesiPosition);
            }
        });
        oncekiButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int oncekiSarkiListesiPosition = calınanSarkiListesiPsition-1;
                if(oncekiSarkiListesiPosition<0){
                    oncekiSarkiListesiPosition = muzikListesiList.size()-1;
                }
                muzikListesiList.get(calınanSarkiListesiPsition).setCaliyorMu(false);
                muzikListesiList.get(oncekiSarkiListesiPosition).setCaliyorMu(true);

                muzikAdaptör.guncelListe(muzikListesiList);
                muzikRecyclerView.scrollToPosition(oncekiSarkiListesiPosition);
                onChanged(oncekiSarkiListesiPosition);

            }
        });
        oynatmaButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calisiyorMu){
                    calisiyorMu=false;

                    medyaOynatici.pause();
                    oynatmaButonuImg.setImageResource(R.drawable.oynat_icon);
                }
                else{
                    calisiyorMu=true;
                    medyaOynatici.start();
                    oynatmaButonuImg.setImageResource(R.drawable.oynatiliyor_icon);


                }

            }
        });
        oynatmaCubugu.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    if(calisiyorMu){
                        medyaOynatici.seekTo(progress);
                    }
                    else{
                        medyaOynatici.seekTo(0);
                    }

                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        if (!muzikListesiList.isEmpty()) {
            muzikAdaptör = new muzikAdapter(muzikListesiList, sarkilarEkrani.this);
            muzikRecyclerView.setAdapter(muzikAdaptör);

            // Buraya tıklama olaylarını dinlemek için kod ekleyin
            muzikAdaptör.setOnItemClickListener(new muzikAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {

                    muzikListesi tıklananMuzik = muzikListesiList.get(position);

                    // Örneğin, tıklanan müzik dosyasının adını alabilir ve bir Toast mesajıyla gösterebiliriz.
                    String dosyaAdi = tıklananMuzik.getBaslik();
                    Context context = getApplicationContext();

                    // Tıklanan müziğin başka bir ekranda çalınması, durdurulması vb. işlemler de burada yapılabilir.
                }
            });
        } else {
            Toast.makeText(this, "Müzik dosyaları bulunamadı", Toast.LENGTH_SHORT).show();
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
            muzikListesi muziklist = new muzikListesi(muzikDosyasiAdi, sanatciAdi, sure, false, muzikDosyasiUri,false);
            muzikListesiList.add(muziklist);
        } while (cursor.moveToNext());
        cursor.close();
        if (!muzikListesiList.isEmpty()) {
            muzikAdaptör = new muzikAdapter(muzikListesiList, sarkilarEkrani.this);
            muzikRecyclerView.setAdapter(muzikAdaptör);
        } else {
            Toast.makeText(this, "Müzik dosyaları bulunamadı", Toast.LENGTH_SHORT).show();
        }cursor.close();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11) { // İzin talep kodu
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
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
        if(hasFocus){
            View decodeView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decodeView.setSystemUiVisibility(option);
        }
    }
    @Override
    public void onChanged(int position) {
        calınanSarkiListesiPsition = position;
        if(medyaOynatici.isPlaying()){
            medyaOynatici.pause();
            medyaOynatici.reset();
        }
        medyaOynatici.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    medyaOynatici.setDataSource(sarkilarEkrani.this,muzikListesiList.get(position).getMuzikDosyasi());
                    medyaOynatici.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                  //  Toast.makeText(sarkilarEkrani.this,"Şarkı çalınamıyor!",Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
        medyaOynatici.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                final int getToplamSure = mp.getDuration();

                String sureOlustur = String.format(Locale.getDefault(),"%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(getToplamSure),
                        TimeUnit.MILLISECONDS.toSeconds(getToplamSure)-
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getToplamSure)));

                bitisZamani.setText(sureOlustur);
                calisiyorMu=true;

                mp.start();
                oynatmaCubugu.setMax(getToplamSure);
                oynatmaButonuImg.setImageResource(R.drawable.oynatiliyor_icon);
            }
        });
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final int getGecenSure=medyaOynatici.getCurrentPosition();
                        String sureOlustur = String.format(Locale.getDefault(),"%02d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(getGecenSure),
                                TimeUnit.MILLISECONDS.toSeconds(getGecenSure)-
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getGecenSure)));

                        oynatmaCubugu.setProgress(getGecenSure);
                        baslamaZamani.setText(sureOlustur);
                    }
                });
            }
        },1000,1000);

        medyaOynatici.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                medyaOynatici.reset();

                timer.purge();
                timer.cancel();

                calisiyorMu=false;

                oynatmaButonuImg.setImageResource(R.drawable.oynat_icon);

                oynatmaCubugu.setProgress(0);
            }
        });

        Log.d("sarkilarEkrani", "onItemClick method called");

    }
}
