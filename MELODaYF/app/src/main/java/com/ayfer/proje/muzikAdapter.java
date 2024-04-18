package com.ayfer.proje;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class muzikAdapter extends RecyclerView.Adapter<muzikAdapter.MyViewHolder> {
    private List<muzikListesi> list;
    private final Context context;
    private int calismaPozisyonu = 0;
    private final sarkiDegistirici sarkiDegistirme;
    private OnItemClickListener mListener;

    public muzikAdapter(List<muzikListesi> list, Context context) {
        this.list = list;
        this.context = context;
        this.sarkiDegistirme = ((sarkiDegistirici)context);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public muzikAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.muzik_adapter_layout, null));
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
        holder.kokLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list2.setCaliyorMu(false);
                list2.setCaliyorMu(true);

                sarkiDegistirme.onChanged(holder.getAdapterPosition());

                if (mListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position);
                    }
                }

                try {
                    list2.get(calismaPozisyonu).setCaliyorMu(false);
                    list2.setCaliyorMu(true);

                    sarkiDegistirme.onChanged(position);

                    if (mListener != null) {
                        mListener.onItemClick(position);
                    }

                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("muzikAdapter", "Hata oluştu: " + e.getMessage());
                 //   Toast.makeText(context, "Şarkı çalınamıyor!", Toast.LENGTH_SHORT).show();
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            kokLayout = itemView.findViewById(R.id.kokLayout);
            baslik = itemView.findViewById(R.id.muzikBasligi);
            sanatci = itemView.findViewById(R.id.muzikSanatcisi);
            muzikSuresi = itemView.findViewById(R.id.muzikSuresi);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
