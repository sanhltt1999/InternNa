package leeshani.com.contentprovider;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddStudentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etName, etBirthday;
    private Button btnAddStudent;
    private ImageView ivCalender;
    private Calendar birthday;
    private Spinner spClass;

    String unknown = "Unknown";

    public static final String COLUMN_STUDENT_NAME = "student_name";
    public static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
    public static final String COLUMN_CLASSNAME = "class_name";

    private static final String SHARE_AUTHORITY = "com.example.content_provider_sqllite.provider";

    public static final Uri CONTENT_URI_STUDENT =
            Uri.parse("content://" + SHARE_AUTHORITY + "/student");
    public static final Uri CONTENT_URI_CLASS =
            Uri.parse("content://" + SHARE_AUTHORITY + "/class");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_acticity);

        InitUI();

        setToolbar();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setBirthday();
        setSpinner();

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudent();
            }
        });
    }

    private void InitUI(){
        toolbar = findViewById(R.id.AddStudentToolbar);
        etName = findViewById(R.id.edtNameStudent);
        etBirthday = findViewById(R.id.edtBirthday);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        ivCalender = findViewById(R.id.ivCalendar);
        spClass = findViewById(R.id.spClass);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setBirthday() {
        ivCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                birthday = Calendar.getInstance();
                int date = birthday.get(Calendar.DATE);
                int month = birthday.get(Calendar.MONTH);
                int year = birthday.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddStudentActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        birthday.set(i, i1, i2);
                        etBirthday.setText(simpleDateFormat.format(birthday.getTime()));
                    }
                }, year, month, date);
                datePickerDialog.show();
            }
        });
    }

    private void setSpinner() {

        ArrayList<String> arClasses  = getClassName();
        arClasses.add(unknown);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arClasses);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClass.setAdapter(arrayAdapter);
    }

    private ArrayList<String> getClassName(){
        ArrayList<String> getNameClass =  new ArrayList<>();

        Cursor c = getContentResolver().query(CONTENT_URI_CLASS, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                getNameClass.add (c.getString(c.getColumnIndexOrThrow("class_name")));
            }
            c.close();
        }else{

            Toast.makeText(this, "Please add new class", Toast.LENGTH_SHORT).show();
        }
        return getNameClass;
    }

    private void addStudent() {
        String strStudentName = etName.getText().toString().trim();
        String strBirthday = etBirthday.getText().toString().trim();
        String strClass;
        if (spClass.getSelectedItem() == unknown) {

            Toast.makeText(AddStudentActivity.this, "Please choose or add class", Toast.LENGTH_LONG).show();
            return;

        } else {
            strClass = spClass.getSelectedItem().toString();
        }
        if (TextUtils.isEmpty(strStudentName) || TextUtils.isEmpty(strBirthday) || TextUtils.isEmpty(strClass)) {

            Toast.makeText(AddStudentActivity.this, "Please enter information", Toast.LENGTH_LONG).show();

        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_STUDENT_NAME, strStudentName);
            values.put(COLUMN_DATE_OF_BIRTH, strBirthday);
            values.put(COLUMN_CLASSNAME, strClass);

            getContentResolver().insert(CONTENT_URI_STUDENT, values);
            etName.setText(null);
            etBirthday.setText(null);
        }
    }

}