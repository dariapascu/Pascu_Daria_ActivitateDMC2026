package com.example.lab8;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CarteDao {

    @Insert
    void insert(Carte carte);

    @Query("SELECT * FROM carti")
    List<Carte> getAll();

    @Query("SELECT * FROM carti WHERE titlu = :titlu")
    List<Carte> getByTitlu(String titlu);

    @Query("SELECT * FROM carti WHERE an BETWEEN :anMin AND :anMax")
    List<Carte> getByAnInterval(int anMin, int anMax);

    @Query("DELETE FROM carti WHERE an > :an")
    void deleteWhereAnGreaterThan(int an);
    @Query("UPDATE carti SET an = an + 1 WHERE titlu LIKE :prefix")
    void incrementAnWhereTitluStartsWith(String prefix);
}
