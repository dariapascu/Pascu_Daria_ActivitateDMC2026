package com.example.lab8;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "carti")
public class Carte {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "titlu")
    public String titlu;

    @ColumnInfo(name = "autor")
    public String autor;

    @ColumnInfo(name = "an")
    public int an;

    public Carte() {}

    public Carte(String titlu, String autor, int an) {
        this.titlu = titlu;
        this.autor = autor;
        this.an = an;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + titlu + " - " + autor + " (" + an + ")";
    }
}
