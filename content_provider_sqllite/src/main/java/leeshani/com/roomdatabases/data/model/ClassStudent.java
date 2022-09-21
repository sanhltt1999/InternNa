package leeshani.com.roomdatabases.data.model;


public class ClassStudent {

    private int id;

    private String name;
    private String dateCreate;
    private String teacher;

    public ClassStudent(String name, String dateCreate, String teacher) {
        this.name = name;
        this.dateCreate = dateCreate;
        this.teacher = teacher;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
