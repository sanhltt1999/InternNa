package leeshani.com.roomdatabases.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "student")
public class Student implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String studentName;
    private String date;
    private String classes;

    public Student(String studentName, String date, String classes) {
        this.studentName = studentName;
        this.date = date;
        this.classes = classes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }
}
