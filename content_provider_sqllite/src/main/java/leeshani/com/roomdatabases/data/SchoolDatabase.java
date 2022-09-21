package leeshani.com.roomdatabases.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import leeshani.com.roomdatabases.data.model.ClassStudent;
import leeshani.com.roomdatabases.data.model.Student;


public class SchoolDatabase extends SQLiteOpenHelper{

        private Context context;
        private static final String DATABASE_NAME = "class_student.db";
        private static final int DATABASE_VERSION =1;

        private static final String STUDENT_TABLE_NAME = "student";
        private static final String CLASS_TABLE_NAME = "class";
        private static final String COLUMN_ID ="id";
        private static final String COLUMN_STUDENT_NAME = "student_name";
        private static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
        private static final String COLUMN_CLASSNAME = "class_name";
        private static final String COLUMN_TEACHER = "teacher";
        private static final String COLUMN_DATE_CREATE_CLASS= "date_create";

        public SchoolDatabase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String queryClassTable = "CREATE TABLE " + CLASS_TABLE_NAME +
                    " ("+ COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_CLASSNAME + "TEXT,"+
                    COLUMN_TEACHER + "TEXT ,"+
                    COLUMN_DATE_CREATE_CLASS +"TEXT);";
            String queryStudentTable = "CREATE TABLE " + STUDENT_TABLE_NAME +
                    " ("+ COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_STUDENT_NAME + "TEXT,"+
                    COLUMN_DATE_OF_BIRTH + "TEXT ,"+
                    COLUMN_CLASSNAME +"TEXT);";
            db.execSQL(queryClassTable);
            db.execSQL(queryStudentTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " +CLASS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS "+ STUDENT_TABLE_NAME);
            onCreate(db);
        }
        public void addClass(ClassStudent classStudent){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_CLASSNAME, classStudent.getName());
            cv.put(COLUMN_DATE_CREATE_CLASS, classStudent.getDateCreate());
            cv.put(COLUMN_TEACHER, classStudent.getTeacher());
            db.insert(CLASS_TABLE_NAME, null,cv);

        }
        public void addStudent(Student student){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_CLASSNAME, student.getStudentName());
            cv.put(COLUMN_DATE_OF_BIRTH, student.getDate());
            cv.put(COLUMN_CLASSNAME, student.getClasses());
            db.insert(STUDENT_TABLE_NAME, null,cv);
        }
       public Cursor readAllTable(){
            String query = "SELECT * FROM " + CLASS_TABLE_NAME;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;
            if(db!=null){
                cursor = db.rawQuery(query, null);
            }
            return cursor;
        }
        public Cursor readColumn(String columnName, String tableName){
            String query = "SELECT " + columnName + " FROM " +tableName;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = null;
            if(db!=null){
                cursor = db.rawQuery(query,null);
            }
            return cursor;
        }
}
