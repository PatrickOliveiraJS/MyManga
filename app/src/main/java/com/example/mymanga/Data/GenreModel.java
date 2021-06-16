package com.example.mymanga.Data;

public class GenreModel {
    int imgGenre;
    String nameGenre;

    public GenreModel(int imgGenre, String nameGenre) {
        this.imgGenre = imgGenre;
        this.nameGenre = nameGenre;
    }

    public int getImgGenre() {
        return imgGenre;
    }

    public String getNameGenre() {
        return nameGenre;
    }
}
