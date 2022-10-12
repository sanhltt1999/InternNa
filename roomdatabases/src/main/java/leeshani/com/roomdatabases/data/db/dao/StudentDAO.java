package leeshani.com.roomdatabases.data.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import leeshani.com.roomdatabases.data.model.Student;

@Dao
public interface StudentDAO {

    @Insert
    Completable insertUser(Student students);

    @Query("SELECT * FROM student")
    Single<List<Student>> getListStudent();

    @Query("SELECT *FROM student where studentName = :studentName and date = :dateOfBirth")
    Single<List<Student>> checkStudent(String studentName, String dateOfBirth);

    @Update
    Completable updateStudent(Student student);

    @Delete
    Completable deleteStudent(Student student);

    @Query("SELECT * FROM student")
    List<Student> getListStudents();

}
