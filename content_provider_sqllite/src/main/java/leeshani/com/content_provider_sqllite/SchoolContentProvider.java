package leeshani.com.content_provider_sqllite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import leeshani.com.content_provider_sqllite.data.SchoolDatabase;

public class SchoolContentProvider extends ContentProvider {

    SchoolDatabase dbHelper;

    private static final int CONTENT_STUDENT = 100;
    private static final int CONTENT_ITEM_STUDENT = 101;
    private static final int CONTENT_CLASS = 102;
    private static final int CONTENT_ITEM_CLASS = 103;

    private static final String AUTHORITY = "com.example.content_provider_sqllite.provider";

    public static final Uri CONTENT_URI_STUDENT =
            Uri.parse("content://" + AUTHORITY + "/student");
    public static final Uri CONTENT_URI_CLASS =
            Uri.parse("content://" + AUTHORITY + "/class");

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "student", CONTENT_STUDENT);
        uriMatcher.addURI(AUTHORITY, "student/#", CONTENT_ITEM_STUDENT);
        uriMatcher.addURI(AUTHORITY, "class", CONTENT_CLASS);
        uriMatcher.addURI(AUTHORITY, "class/#", CONTENT_ITEM_CLASS);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new SchoolDatabase(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case CONTENT_STUDENT:
                queryBuilder.setTables(SchoolDatabase.STUDENT_TABLE_NAME);
                break;
            case CONTENT_ITEM_STUDENT:
                queryBuilder.setTables(SchoolDatabase.STUDENT_TABLE_NAME);
                String studentID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(SchoolDatabase.COLUMN_STUDENT_ID + "=" + studentID);
            case CONTENT_CLASS:
                queryBuilder.setTables(SchoolDatabase.CLASS_TABLE_NAME);
                break;
            case CONTENT_ITEM_CLASS:
                queryBuilder.setTables(SchoolDatabase.CLASS_TABLE_NAME);
                String classID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(SchoolDatabase.COLUMN_CLASS_ID + "=" + classID);
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CONTENT_STUDENT:
                return "vnd.android.cursor.dir/vnd.leeshani.com.content_provider_sqllite.abc.student";
            case CONTENT_ITEM_STUDENT:
                return "vnd.android.cursor.item/vnd.leeshani.com.content_provider_sqllite.abc.student";
            case CONTENT_CLASS:
                return "vnd.android.cursor.dir/vnd.leeshani.com.content_provider_sqllite.abc.class";
            case CONTENT_ITEM_CLASS:
                return "vnd.android.cursor.item/vnd.leeshani.com.content_provider_sqllite.abc.class";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri _uri;
        switch (uriMatcher.match(uri)) {
            case CONTENT_STUDENT:
                long id1 = db.insert(SchoolDatabase.STUDENT_TABLE_NAME, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                _uri = Uri.parse(CONTENT_URI_STUDENT + "/" + id1);
                break;
            case CONTENT_CLASS:
                long id2 = db.insert(SchoolDatabase.CLASS_TABLE_NAME, null, contentValues);
                getContext().getContentResolver().notifyChange(uri, null);
                _uri = Uri.parse(CONTENT_URI_CLASS + "/" + id2);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return _uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleteCount = 0;
        switch (uriMatcher.match(uri)) {
            case CONTENT_STUDENT:
                deleteCount = db.delete(SchoolDatabase.STUDENT_TABLE_NAME, selection, selectionArgs);
                break;
            case CONTENT_ITEM_STUDENT:
                String id1 = uri.getPathSegments().get(1);
                selection = SchoolDatabase.COLUMN_STUDENT_ID + "=" + id1
                        + (!TextUtils.isEmpty(selection) ?
                        "AND (" + selection + ")" : "");
                deleteCount = db.delete(SchoolDatabase.STUDENT_TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case CONTENT_CLASS:
                break;
            case CONTENT_ITEM_CLASS:
                String id2 = uri.getPathSegments().get(1);
                selection = SchoolDatabase.COLUMN_CLASS_ID + "=" + id2
                        + (!TextUtils.isEmpty(selection) ?
                        "AND (" + selection + ")" : "");
                deleteCount = db.delete(SchoolDatabase.CLASS_TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return deleteCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateCount = 0;
        switch ((uriMatcher.match(uri))) {
            case CONTENT_STUDENT:
                updateCount = db.update(SchoolDatabase.STUDENT_TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case CONTENT_ITEM_STUDENT:
                String id1 = uri.getPathSegments().get(1);
                selection = SchoolDatabase.COLUMN_STUDENT_ID + "=" + id1
                        + (!TextUtils.isEmpty(selection) ?
                        "AND (" + selection + ")" : "");
                updateCount = db.update(SchoolDatabase.STUDENT_TABLE_NAME, contentValues, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case CONTENT_CLASS:
                break;
            case CONTENT_ITEM_CLASS:
                String id2 = uri.getPathSegments().get(1);
                selection = SchoolDatabase.COLUMN_CLASS_ID + "=" + id2
                        + (!TextUtils.isEmpty(selection) ?
                        "AND (" + selection + ")" : "");
                updateCount = db.update(SchoolDatabase.CLASS_TABLE_NAME, contentValues, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return updateCount;
    }
}
