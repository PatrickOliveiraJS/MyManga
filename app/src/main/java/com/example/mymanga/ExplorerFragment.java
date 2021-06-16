package com.example.mymanga;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymanga.Adapter.GenreAdapter;
import com.example.mymanga.Adapter.MangaAdapter;
import com.example.mymanga.Data.GenreModel;
import com.example.mymanga.Data.MangaModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExplorerFragment extends Fragment {
    RecyclerView rvGenre, rvAnimes;
    MangaAdapter mangaAdapter;
    private static final String TAG = "ExplorerFragment";
    ArrayList<MangaModel> arrayList;
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference ref = db.getReference("/Mangas");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorer, container, false);
        rvGenre = view.findViewById(R.id.rv_genre);
        rvAnimes = view.findViewById(R.id.rv_animes);

        arrayList = new ArrayList<>();
        GenreAdapter adapter = new GenreAdapter(getContext(), getGenreModels());
        mangaAdapter = new MangaAdapter(getContext(), arrayList);
        rvGenre.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenre.setItemAnimator(new DefaultItemAnimator());
        rvGenre.setAdapter(adapter);
        rvAnimes.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvAnimes.setAdapter(getMangas());

        return view;
    }

    public ArrayList<GenreModel> getGenreModels() {
        ArrayList<GenreModel> genreModels = new ArrayList<>();

        genreModels.add(new GenreModel(R.drawable.ic_play_circle, "Ação"));
        genreModels.add(new GenreModel(R.drawable.ic_romance, "Romance"));
        genreModels.add(new GenreModel(R.drawable.ic_science, "Fic. Científica"));
        genreModels.add(new GenreModel(R.drawable.ic_school, "Vida Escolar"));
        genreModels.add(new GenreModel(R.drawable.ic_sports, "Esportes"));
        genreModels.add(new GenreModel(R.drawable.ic_whatshot, "Ecchi"));
        return genreModels;
    }

    public MangaAdapter getMangas() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MangaModel mangaModel = dataSnapshot.getValue(MangaModel.class);
                    arrayList.add(mangaModel);
                }
                mangaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "getManga: Fail to access database: " + error.getMessage());
            }
        });
        return mangaAdapter;
    }
}