package com.example.mymanga.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymanga.Data.GenreModel;
import com.example.mymanga.R;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    Context context;
    ArrayList<GenreModel> genreModels;

    public GenreAdapter(Context context, ArrayList<GenreModel> genreModels) {
        this.context = context;
        this.genreModels = genreModels;
    }

    @NonNull
    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.ViewHolder holder, final int position) {
        holder.setImageView(genreModels.get(position).getImgGenre());
        holder.setTextView(genreModels.get(position).getNameGenre());
        holder.itemView.setOnClickListener(v -> Toast.makeText(context, genreModels.get(position).getNameGenre(),
                Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return genreModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGenre;
        TextView tvGenre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGenre = itemView.findViewById(R.id.iv_genre);
            tvGenre = itemView.findViewById(R.id.tv_genre);
        }

        public void setImageView(int imgGenre) {
            ivGenre.setImageResource(imgGenre);
        }

        public void setTextView(String nameGenre) {
            tvGenre.setText(nameGenre);
        }
    }
}
