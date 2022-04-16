package dps924.assignment4.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = SavedRecipe.class, exportSchema = false, version = 5)
@TypeConverters({Converters.class})
public abstract class SavesDatabase extends RoomDatabase {
    private static final String DB_NAME = "saved_recipes_db";
    public static SavesDatabase instance;

    public static synchronized  SavesDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), SavesDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract SaveDao saveDao();
}
