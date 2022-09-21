package leeshani.com.roomdatabases.ui.editstudent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
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

import leeshani.com.roomdatabases.data.model.ClassStudent;
import leeshani.com.roomdatabases.R;
import leeshani.com.roomdatabases.data.model.Student;

public class EditStudentActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText etName, etDate;
    private Button btEdit;
    private ImageView ivEditCalendar;
    private Calendar birthday;
    private Spinner spEditClass;
    private List<ClassStudent> classes;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        InitUI();

        setToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setBirthday();
//        setSpinnerClass();

        student = (Student) getIntent().getExtras().get("object_student");
        if (student != null) {
            etName.setText(student.getStudentName());
            etDate.setText(student.getDate());
        }
//        btEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                updateStudent();
//            }
//        });
    }

    private void InitUI() {
        toolbar = findViewById(R.id.tbEditStudent);
        etName = findViewById(R.id.edtEditNameStudent);
        etDate = findViewById(R.id.edtEditBirthday);
        btEdit = findViewById(R.id.btnConfirmEdit);
        ivEditCalendar = findViewById(R.id.ivCalendarEdit);
        spEditClass = findViewById(R.id.spEditClass);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setBirthday() {
        ivEditCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                birthday = Calendar.getInstance();
                int date = birthday.get(Calendar.DATE);
                int month = birthday.get(Calendar.MONTH);
                int year = birthday.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditStudentActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        birthday.set(i, i1, i2);
                        etDate.setText(simpleDateFormat.format(birthday.getTime()));
                    }
                }, year, month, date);
                datePickerDialog.show();
            }
        });
    }

//    private void setSpinnerClass() {
//        ArrayList<String> arClasses = new ArrayList<>();
//        classes = StudentAndClassDatabase.getInstance(EditStudentActivity.this).classDAO().getListClass();
//        for (int i = 0; i < classes.size(); i++) {
//            arClasses.add(classes.get(i).getName());
//        }
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arClasses);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spEditClass.setAdapter(arrayAdapter);
//    }
//
//    private void updateStudent() {
//        String strStudentName = etName.getText().toString().trim();
//        String strBirthday = etDate.getText().toString().trim();
//        String strClass;
//        if (spEditClass.getSelectedItem() == null) {
//            Toast.makeText(EditStudentActivity.this, "Please choose or add class", Toast.LENGTH_LONG).show();
//            return;
//        } else {
//            strClass = spEditClass.getSelectedItem().toString();
//        }
//        if (TextUtils.isEmpty(strStudentName) || TextUtils.isEmpty(strBirthday) || TextUtils.isEmpty(strClass)) {
//            Toast.makeText(EditStudentActivity.this, "Please enter information", Toast.LENGTH_LONG).show();
//            return;
//        }
//        student.setStudentName(strStudentName);
//        student.setDate(strBirthday);
//        student.setClasses(strClass);
//
//        StudentAndClassDatabase.getInstance(EditStudentActivity.this).studentDAO().updateStudent(student);
//
//        Intent intentEditResult = new Intent();
//        setResult(Activity.RESULT_OK, intentEditResult);
//        finish();
//    }


}