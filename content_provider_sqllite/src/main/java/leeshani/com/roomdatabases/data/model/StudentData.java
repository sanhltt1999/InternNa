package leeshani.com.roomdatabases.data.model;

public enum StudentData {
    ID(0), STUDENT_NAME(1), DATE(2), CLASSES(3);

    private int value;

    StudentData(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}