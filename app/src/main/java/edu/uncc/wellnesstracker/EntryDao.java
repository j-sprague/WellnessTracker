package edu.uncc.wellnesstracker;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EntryDao {

    @Query("SELECT * FROM entry")
    List<Entry> getAll();

    @Insert
    void insertAll(Entry... entries);

    @Delete
    void delete(Entry entry);

}
