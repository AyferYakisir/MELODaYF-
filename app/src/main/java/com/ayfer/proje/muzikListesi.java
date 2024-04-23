package com.ayfer.proje;

import android.net.Uri;

import java.util.List;

public class muzikListesi {
    private String baslik,sanatci,sure;
    private boolean caliyorMu;
    private boolean secildiMi;
    private Uri muzikDosyasi;

    public muzikListesi(String baslik,String sanatci,String sure,boolean calisiyorMu,Uri muzikDosyasi,boolean secildiMi){
        this.baslik=baslik;
        this.sanatci=sanatci;
        this.sure=sure;
        this.caliyorMu=calisiyorMu;
        this.muzikDosyasi=muzikDosyasi;
        this.secildiMi=secildiMi;
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

    public boolean isSecildiMi(){return secildiMi;}

    public Uri getMuzikDosyasi() {
        return muzikDosyasi;
    }

    public void setCaliyorMu(boolean caliyorMu) {
        this.caliyorMu = caliyorMu;
    }
    public void setSecildiMi(boolean secildiMi) {
        this.secildiMi = secildiMi;
    }

    public muzikListesi get(int calismaPozisyonu) {
        return null;
    }


}

