package dps924.assignment4.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SaveDao {
    @Query("SELECT * FROM save")
    List<SavedRecipe> getSavedList();

    @Insert
    void insertSave(SavedRecipe recipe);

    @Query("SELECT * FROM save WHERE id=:id")
    SavedRecipe getSave(int id);

    @Update
    void updateSave(SavedRecipe recipe);

    @Delete
    void deleteSave(SavedRecipe recipe);

    @Query("DELETE FROM save")
    void  deleteAll();
}
