package leeshani.com.roomdatabases.ui.addstudent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
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

import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import leeshani.com.roomdatabases.R;
import leeshani.com.roomdatabases.data.SchoolDatabase;
import leeshani.com.roomdatabases.data.model.Student;
import leeshani.com.roomdatabases.data.model.StudentData;
import leeshani.com.roomdatabases.ui.addclass.AddClassActivity;

public class AddStudentActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText etName, etBirthday;
    private Button btnAddClass, btnAddStudent;
    private ImageView ivCalender;
    private Calendar birthday;
    private Spinner spClass;
    private SchoolDatabase database;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

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

        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itAddClass = new Intent(AddStudentActivity.this, AddClassActivity.class);
                startActivity(itAddClass);
            }
        });

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudent();
            }
        });

    }

    private void InitUI() {
        etName = findViewById(R.id.edtNameStudent);
        etBirthday = findViewById(R.id.edtBirthday);
        btnAddClass = findViewById(R.id.btAddClass);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        ivCalender = findViewById(R.id.ivCalendar);
        spClass = findViewById(R.id.spClass);
        toolbar = findViewById(R.id.AddStudentToolbar);
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
        ArrayList<String> arClasses = getClassName();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arClasses);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClass.setAdapter(arrayAdapter);
    }

    private void addStudent() {
        String strStudentName = etName.getText().toString().trim();
        String strBirthday = etBirthday.getText().toString().trim();
        String strClass;
        if (spClass.getSelectedItem() == null) {
            Toast.makeText(AddStudentActivity.this, "Please choose or add class", Toast.LENGTH_LONG).show();
            return;
        } else {
            strClass = spClass.getSelectedItem().toString();
        }
        if (TextUtils.isEmpty(strStudentName) || TextUtils.isEmpty(strBirthday) || TextUtils.isEmpty(strClass)) {
            Toast.makeText(AddStudentActivity.this, "Please enter information", Toast.LENGTH_LONG).show();
        } else {

            Student student = new Student(strStudentName, strBirthday, strClass);

           database = new SchoolDatabase(this);
            database.addStudent(student);
            etName.setText(null);
            etBirthday.setText(null);
        }
    }

    private ArrayList<String> getClassName(){
        ArrayList<String> getNameClass =  new ArrayList<>();
        database = new SchoolDatabase(this);
        Cursor cursor = database.readAllTable();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "Please add new class", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                getNameClass.add(cursor.getString(StudentData.STUDENT_NAME.getValue()));
            }
        }
        return getNameClass;
    }

}