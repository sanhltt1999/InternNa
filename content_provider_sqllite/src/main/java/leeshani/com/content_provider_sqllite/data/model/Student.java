package leeshani.com.content_provider_sqllite.data.model;

import java.io.Serializable;


public class Student implements Serializable {

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
