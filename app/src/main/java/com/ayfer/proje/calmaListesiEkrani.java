package com.ayfer.proje;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class calmaListesiEkrani extends AppCompatActivity {

    private RecyclerView recyclerView;
    private listeGoruntulemeAdapter adapter;
    private List<muzikListesi> sarkilarListesi = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calma_listesi_ekrani);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Intent'ten çalma listesi adını al
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("calmaListesiAdi")) {
            String calmaListesiAdi = intent.getStringExtra("calmaListesiAdi");

            // Çalma listesi adını ekranda göster
            TextView textView = findViewById(R.id.textViewCalmaListesiAdi);
            textView.setText(calmaListesiAdi);
        }

        // RecyclerView için bir adapter oluşturun ve seçilen şarkıları listeleyin
        recyclerView = findViewById(R.id.recyclerViewSarkilar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Seçilen şarkıları al
        List<muzikListesi> selectedSongs = new ArrayList<>();

        // Seçilen şarkıları içeren bir List'i RecyclerView'a bağlayın
        adapter = new listeGoruntulemeAdapter(this, selectedSongs);
        recyclerView.setAdapter(adapter);

        // Seçilen şarkıları almak için metodu çağırın
        getSelectedSongs();
    }

    

    // Seçilen şarkıları al
    private void getSelectedSongs() {
        // Adapter üzerinden seçilen şarkıları al
        List<muzikListesi> selectedSongs = adapter.getSecilenSarkilar();

        // Seçilen şarkıları sarkilarListesi'ne ekle
        sarkilarListesi.addAll(selectedSongs);

        // Adapterı güncelle
        adapter.notifyDataSetChanged();
    }
}
