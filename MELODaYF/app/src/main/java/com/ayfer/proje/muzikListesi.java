package com.ayfer.proje;

import android.net.Uri;

public class muzikListesi {
    private String baslik,sanatci,sure;
    private boolean caliyorMu;
    private Uri muzikDosyasi;

    public muzikListesi(String baslik,String sanatci,String sure,boolean calisiyorMu,Uri muzikDosyasi){
        this.baslik=baslik;
        this.sanatci=sanatci;
        this.sure=sure;
        this.caliyorMu=calisiyorMu;
        this.muzikDosyasi=muzikDosyasi;
    }
    public String getBaslik() {
        return baslik;
    }
    public String getSanatci() {
        return sanatci;
    }
    public String getSure() {
        return sure;
    }

    public boolean isCaliyorMu() {
        return caliyorMu;
    }

    public Uri getMuzikDosyasi() {
        return muzikDosyasi;
    }

    public void setCaliyorMu(boolean caliyorMu) {
        this.caliyorMu = caliyorMu;
    }

    public muzikListesi get(int calismaPozisyonu) {
        return null;
    }
}
