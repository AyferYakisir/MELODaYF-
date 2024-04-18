package com.ayfer.proje;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class anaEkran extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ana_ekran);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton sarkilarButton = findViewById(R.id.sarkilarButton);
        sarkilarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Diğer aktiviteye geçiş için Intent oluşturma
                Intent intent = new Intent(anaEkran.this, sarkilarEkrani.class);
                startActivity(intent);
            }
        });
        ImageButton calmaListesiButton = findViewById(R.id.calmaListesiButton);
        calmaListesiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(anaEkran.this,calmaListesiEkrani.class);
                startActivity(intent);
            }
        });


    }
}