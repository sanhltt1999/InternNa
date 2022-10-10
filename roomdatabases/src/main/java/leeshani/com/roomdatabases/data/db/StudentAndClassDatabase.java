package leeshani.com.roomdatabases.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import leeshani.com.roomdatabases.data.db.dao.ClassDAO;
import leeshani.com.roomdatabases.data.db.dao.StudentDAO;
import leeshani.com.roomdatabases.data.model.ClassStudent;
import leeshani.com.roomdatabases.data.model.Student;

@Database(entities = {Student.class, ClassStudent.class}, version = 1)
public abstract class StudentAndClassDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "class_student.db";
    private static StudentAndClassDatabase instance;

    public static synchronized StudentAndClassDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), StudentAndClassDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract ClassDAO classDAO();

    public abstract StudentDAO studentDAO();

}
