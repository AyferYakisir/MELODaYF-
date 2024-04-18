package com.ayfer.proje;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

public class calmaListesiEkrani extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private GridView gridViewCalmaListeleri;

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
        gridViewCalmaListeleri = findViewById(R.id.gridViewCalmaListeleri);
        databaseHelper = new DatabaseHelper(this);

// Veritabanından çalma listelerini al
        Cursor cursor = databaseHelper.getCalmaListeleri();

// Verileri gridView'e bağla
        String[] fromColumns = {DatabaseHelper.COL_LISTE_ADI};
        int[] toViews = {android.R.id.text1};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursor,
                fromColumns,
                toViews,
                0);

        gridViewCalmaListeleri.setAdapter(adapter);

    }
}