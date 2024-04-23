package com.ayfer.proje;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class listeGoruntulemeAdapter extends RecyclerView.Adapter<listeGoruntulemeAdapter.ViewHolder> {

    private List<muzikListesi> sarkilarListesi;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    public listeGoruntulemeAdapter(Context context, List<muzikListesi> data) {
        this.inflater = LayoutInflater.from(context);
        this.sarkilarListesi = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_liste_goruntuleme_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        muzikListesi sarki = sarkilarListesi.get(position);
        holder.sarkiAdiTextView.setText(sarki.getBaslik());
        holder.sanatciAdiTextView.setText(sarki.getSanatci());
        holder.sureTextView.setText(sarki.getSure());
    }

    @Override
    public int getItemCount() {
        return sarkilarListesi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView sarkiAdiTextView;
        TextView sanatciAdiTextView;
        TextView sureTextView;

        ViewHolder(View itemView) {
            super(itemView);
            sarkiAdiTextView = itemView.findViewById(R.id.textViewMusicTitle);
            sanatciAdiTextView = itemView.findViewById(R.id.textViewArtistName);
            sureTextView = itemView.findViewById(R.id.textViewDuration);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    muzikListesi getItem(int id) {
        return sarkilarListesi.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public List<muzikListesi> getSecilenSarkilar() {
        List<muzikListesi> selectedSongs = new ArrayList<>();
        for (muzikListesi song : sarkilarListesi) {
            if (song.isSecildiMi()) {
                selectedSongs.add(song);
            }
        }
        return selectedSongs;
    }
}
