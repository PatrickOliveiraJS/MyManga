package com.example.mymanga.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MangaModel implements Parcelable {
    private String Name;
    private String Format;
    private String Banner;
    private String Description;
    private ArrayList<String> Category;
    private ArrayList<ChapterModel> Chapters;

    public MangaModel(String name, String format, String banner, String description, ArrayList<String> category, ArrayList<ChapterModel> chapters) {
        this.Name = name;
        this.Format = format;
        this.Banner = banner;
        this.Description = description;
        this.Category = category;
        this.Chapters = chapters;
    }

    public MangaModel() {
    }

    protected MangaModel(Parcel in) {
        Name = in.readString();
        Format = in.readString();
        Banner = in.readString();
        Description = in.readString();
        Category = in.createStringArrayList();
    }

    public static final Creator<MangaModel> CREATOR = new Creator<MangaModel>() {
        @Override
        public MangaModel createFromParcel(Parcel in) {
            return new MangaModel(in);
        }

        @Override
        public MangaModel[] newArray(int size) {
            return new MangaModel[size];
        }
    };

    public String getName() {
        return Name;
    }

    public String getFormat() {
        return Format;
    }

    public String getBanner() {
        return Banner;
    }

    public ArrayList<String> getCategory() {
        return Category;
    }

    public ArrayList<ChapterModel> getChapters() {
        return Chapters;
    }

    public String getDescription() {
        return Description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Name);
        parcel.writeString(Format);
        parcel.writeString(Banner);
        parcel.writeString(Description);
        parcel.writeStringList(Category);
    }
}
