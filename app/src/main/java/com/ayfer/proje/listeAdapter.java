package com.ayfer.proje;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class listeAdapter extends RecyclerView.Adapter<listeAdapter.MyViewHolder> {
    private List<muzikListesi> list;
    private final Context context;

    private OnItemClickListener mListener;

    public listeAdapter(List<muzikListesi> list, Context context) {
        this.list = list;
        this.context = context;

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public listeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_liste_adapter, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        muzikListesi list2 = list.get(position);

        if (list2.isCaliyorMu()) {
            holder.kokLayout.setBackgroundResource(R.drawable.round_back_pembe);
        } else {
            holder.kokLayout.setBackgroundResource(R.drawable.round_back);
        }

        String sureOlustur = String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(list2.getSure())),
                TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(list2.getSure())) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(list2.getSure()))));

        holder.baslik.setText(list2.getBaslik());
        holder.sanatci.setText(list2.getSanatci());
        holder.muzikSuresi.setText(sureOlustur);

        // Seçim butonunun durumunu güncelle
        holder.secimButonu.setChecked(list2.isSecildiMi());

        // Seçim butonuna tıklama olayını ekle
        holder.secimButonu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Seçim durumunu güncelle
                list2.setSecildiMi(isChecked);

                // Seçim durumunu dinlemek için olay dinleyiciye ilet
                if (mListener != null) {
                    mListener.onItemClick(position);
                }
            }
        });
    }

    public void guncelListe(List<muzikListesi> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout kokLayout;
        private final TextView baslik;
        private final TextView sanatci;
        private final TextView muzikSuresi;
        private final CheckBox secimButonu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            kokLayout = itemView.findViewById(R.id.kokLayout);
            baslik = itemView.findViewById(R.id.muzikBasligi);
            sanatci = itemView.findViewById(R.id.muzikSanatcisi);
            muzikSuresi = itemView.findViewById(R.id.muzikSuresi);
            secimButonu = itemView.findViewById(R.id.secimButonu);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public List<muzikListesi> getSelectedSongs() {
        List<muzikListesi> selectedSongs = new ArrayList<>();
        for (muzikListesi song : list) {
            if (song.isSecildiMi()) {
                selectedSongs.add(song);
            }
        }
        return selectedSongs;
    }


}
