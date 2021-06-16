package com.example.mymanga.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymanga.Data.MangaModel;
import com.example.mymanga.DetailActivity;
import com.example.mymanga.R;

import java.util.ArrayList;

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.ViewHolder> {
    Context context;
    ArrayList<MangaModel> mangaModels;

    public MangaAdapter(Context context, ArrayList<MangaModel> arrayList) {
        this.context = context;
        this.mangaModels = arrayList;
    }

    @NonNull
    @Override
    public MangaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.manga_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaAdapter.ViewHolder holder, int position) {
        MangaModel model = mangaModels.get(position);
        holder.setIvManga(model.getBanner());
        holder.setTvManga(model.getName());
        holder.itemView.setOnClickListener(view -> openManga(model));
    }

    @Override
    public int getItemCount() {
        return mangaModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivManga;
        TextView tvManga;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivManga = itemView.findViewById(R.id.iv_manga);
            tvManga = itemView.findViewById(R.id.tv_manga);
        }

        public void setIvManga(String s) {
            Glide.with(itemView).load(s).into(ivManga);
        }

        public void setTvManga(String s) {
            tvManga.setText(s);
        }
    }

    public void openManga(MangaModel manga) {
        context.startActivity(new Intent(context, DetailActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("manga", manga));

    }
}
