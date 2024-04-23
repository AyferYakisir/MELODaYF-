package com.ayfer.proje;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MediaPlayerService extends Service {
    public static final String ACTION_PLAY = "com.ayfer.proje.action.PLAY";

    private MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_PLAY.equals(action)) {
                String adi = intent.getStringExtra("adi");
                String sanatci = intent.getStringExtra("sanatci");
                String dosyaYolu = intent.getStringExtra("dosyaYolu");
                playMusic(dosyaYolu);
            }
        }
        return START_NOT_STICKY;
    }

    private void playMusic(String dosyaYolu) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(dosyaYolu);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
