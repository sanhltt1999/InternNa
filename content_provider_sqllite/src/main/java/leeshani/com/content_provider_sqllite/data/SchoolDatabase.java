package leeshani.com.content_provider_sqllite.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SchoolDatabase extends SQLiteOpenHelper {

    private final Context context;
    public static final String DATABASE_NAME = "class_student.db";
    private static final int DATABASE_VERSION = 1;
    public static final String STUDENT_TABLE_NAME = "student";
    public static final String CLASS_TABLE_NAME = "class";
    public static final String COLUMN_STUDENT_ID = "id";
    public static final String COLUMN_CLASS_ID = "id";
    public static final String COLUMN_STUDENT_NAME = "student_name";
    public static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
    public static final String COLUMN_CLASSNAME = "class_name";
    public static final String COLUMN_TEACHER = "teacher";
    public static final String COLUMN_DATE_CREATE_CLASS = "date_create";
    public static final String COLUMN_CLASS_STUDENT = "class_name";

    public SchoolDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryClassTable = "CREATE TABLE " + CLASS_TABLE_NAME +
                " (" + COLUMN_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CLASSNAME + " TEXT, " +
                COLUMN_DATE_CREATE_CLASS + " TEXT, " +
                COLUMN_TEACHER + " TEXT)";

        String queryStudentTable = "CREATE TABLE " + STUDENT_TABLE_NAME +
                " (" + COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STUDENT_NAME + " TEXT, " +
                COLUMN_DATE_OF_BIRTH + " TEXT, " +
                COLUMN_CLASS_STUDENT + " TEXT);";
        db.execSQL(queryClassTable);
        db.execSQL(queryStudentTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CLASS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE_NAME);
        onCreate(db);
    }

}
