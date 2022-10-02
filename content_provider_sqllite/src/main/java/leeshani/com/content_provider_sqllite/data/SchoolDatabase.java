package leeshani.com.content_provider_sqllite.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import leeshani.com.content_provider_sqllite.data.model.Student;

public class SchoolDatabase extends SQLiteOpenHelper{

    private Context context;
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
    public static final String COLUMN_DATE_CREATE_CLASS= "date_create";
    public static final String COLUMN_CLASS_STUDENT = "class_name";

        public SchoolDatabase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String queryClassTable = "CREATE TABLE " + CLASS_TABLE_NAME +
                    " ("+ COLUMN_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_CLASSNAME + " TEXT, "+
                    COLUMN_DATE_CREATE_CLASS + " TEXT, "+
                    COLUMN_TEACHER +" TEXT)";

            String queryStudentTable = "CREATE TABLE " + STUDENT_TABLE_NAME +
                    " ("+ COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_STUDENT_NAME + " TEXT, "+
                    COLUMN_DATE_OF_BIRTH + " TEXT, "+
                    COLUMN_CLASS_STUDENT+" TEXT);";
            db.execSQL(queryClassTable);
            db.execSQL(queryStudentTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " +CLASS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS "+ STUDENT_TABLE_NAME);
            onCreate(db);
        }

//        public void addClass(ClassStudent classStudent){
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues cv = new ContentValues();
//            cv.put(COLUMN_CLASSNAME, classStudent.getName());
//            cv.put(COLUMN_DATE_CREATE_CLASS, classStudent.getDateCreate());
//            cv.put(COLUMN_TEACHER, classStudent.getTeacher());
//            db.insert(CLASS_TABLE_NAME, null,cv);
//        }
//
//        public void addStudent(Student student){
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues cv = new ContentValues();
//            cv.put(COLUMN_STUDENT_NAME, student.getStudentName());
//            cv.put(COLUMN_DATE_OF_BIRTH, student.getDate());
//            cv.put(COLUMN_CLASSNAME, student.getClasses());
//            db.insert(STUDENT_TABLE_NAME, null,cv);
//            db.close();
//        }

//       public Cursor readAllTable() {
//           SQLiteDatabase db = this.getReadableDatabase();
//           String query = "SELECT * FROM " + CLASS_TABLE_NAME;
//           Cursor cursor = null;
//           if (db != null) {
//               cursor = db.rawQuery(query, null);
//           }
//           return cursor;
//       }

//        public Cursor readAllStudentTable() {
//            SQLiteDatabase db = this.getReadableDatabase();
//            String query = "SELECT * FROM " + STUDENT_TABLE_NAME;
//            Cursor cursor = null;
//            if (db != null) {
//                cursor = db.rawQuery(query, null);
//            }
//            return cursor;
//        }

        public void updateStudent( Student student){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_STUDENT_NAME, student.getStudentName());
            cv.put(COLUMN_DATE_OF_BIRTH, student.getDate());
            cv.put(COLUMN_CLASSNAME, student.getClasses());

            db.update(STUDENT_TABLE_NAME, cv, null, null);
        }

        public void deleteStudent(Student student){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(STUDENT_TABLE_NAME,"date_of_birth=?"  , new String[]{student.getDate()});
        }
        
}
