package leeshani.com.content_provider_sqllite.data.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import leeshani.com.content_provider_sqllite.data.model.Student;

@Dao
public interface StudentDAO {
    @Insert
    void insertUser(Student students);

    @Query("SELECT * FROM student")
    List<Student> getListStudent();

    @Query("SELECT *FROM student where studentName= :studentName and date= :dateOfBirth")
    List<Student> checkStudent(String studentName, String dateOfBirth);

    @Update
    void updateStudent(Student student);

    @Delete
    void deleteStudent(Student student);

}
