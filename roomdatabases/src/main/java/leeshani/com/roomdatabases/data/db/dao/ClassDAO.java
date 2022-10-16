package leeshani.com.roomdatabases.data.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import leeshani.com.roomdatabases.data.model.ClassStudent;

@Dao
public interface ClassDAO {

    @Insert
    Completable insertUser(ClassStudent classes);

    @Query("SELECT * FROM class")
    Single<List<ClassStudent>> getListClass();

    @Query("SELECT * FROM class where name = :classname")
    List<ClassStudent> checkClass(String classname);


}
