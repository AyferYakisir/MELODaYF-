package com.ayfer.proje;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MELODaYF.db";
    public static final String SARKILAR_TABLE_NAME = "Sarkilar";
    public static final String COL_ID = "id";
    public static final String COL_ADI = "adi";
    public static final String COL_SANATCI = "sanatci";
    public static final String COL_TUR = "tur";
    public static final String COL_DOSYA_YOLU = "dosyaYolu";

    public static final String CALMA_LISTELERI_TABLE_NAME = "CalmaListeleri";
    public static final String COL_CALMA_LISTESI_ID = "id";
    public static final String COL_LISTE_ADI = "listeAdi";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSarkilarTableQuery = "CREATE TABLE " + SARKILAR_TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_ADI + " TEXT," +
                COL_SANATCI + " TEXT," +
                COL_TUR + " TEXT," +
                COL_DOSYA_YOLU + " TEXT)";
        db.execSQL(createSarkilarTableQuery);

        String createCalmaListeleriTableQuery = "CREATE TABLE " + CALMA_LISTELERI_TABLE_NAME + " (" +
                COL_CALMA_LISTESI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_LISTE_ADI + " TEXT)";
        db.execSQL(createCalmaListeleriTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SARKILAR_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CALMA_LISTELERI_TABLE_NAME);
        onCreate(db);
    }

    public boolean ekleSarki(String adi, String sanatci, String tur, String dosyaYolu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ADI, adi);
        contentValues.put(COL_SANATCI, sanatci);
        contentValues.put(COL_TUR, tur);
        contentValues.put(COL_DOSYA_YOLU, dosyaYolu);
        long result = db.insert(SARKILAR_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getSarkilar() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT id AS _id, adi, sanatci, tur, dosyaYolu FROM " + SARKILAR_TABLE_NAME, null);
    }

    public boolean ekleCalmaListesi(String listeAdi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_LISTE_ADI, listeAdi);
        long result = db.insert(CALMA_LISTELERI_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getCalmaListeleri() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + CALMA_LISTELERI_TABLE_NAME, null);
    }

    public void ornekSarkiVerileriEkle() {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] sarkiAdlari = {"aknasnıs", "Şarkı 2", "Şarkı 3"};
        String[] sanatciAdlari = {"HGBDUOOOOOO", "Sanatçı 2", "Sanatçı 3"};
        String[] dosyaYollari = {"C:\\Users\\Ayfer\\Downloads\\Hadise - Şampiyon.mp3", "dosya2.mp3", "dosya3.mp3"};

        for (int i = 0; i < sarkiAdlari.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COL_ADI, sarkiAdlari[i]);
            values.put(COL_SANATCI, sanatciAdlari[i]);
            values.put(COL_DOSYA_YOLU, dosyaYollari[i]);
            db.insert(SARKILAR_TABLE_NAME, null, values);
        }

        db.close();
    }
}
