package leeshani.com.content_provider_sqllite.ui.editstudent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
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

import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import leeshani.com.content_provider_sqllite.SchoolContentProvider;
import leeshani.com.content_provider_sqllite.data.SchoolDatabase;
import leeshani.com.content_provider_sqllite.R;
import leeshani.com.content_provider_sqllite.data.model.Student;

public class EditStudentActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText etName, etDate;
    private Button btEdit;
    private ImageView ivEditCalendar;
    private Calendar birthday;
    private Spinner spEditClass;
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
                onBack();
            }
        });

        setBirthday();
        setSpinnerClass();

        student = (Student) getIntent().getExtras().get("object_student");
        if (student != null) {
            etName.setText(student.getStudentName());
            etDate.setText(student.getDate());
        }

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStudent();

            }
        });
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

    private void setSpinnerClass() {
        ArrayList<String> arClasses =getClassName();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arClasses);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEditClass.setAdapter(arrayAdapter);
    }

    private void updateStudent() {
        String strStudentName = etName.getText().toString().trim();
        String strBirthday = etDate.getText().toString().trim();
        String strClass;
        if (spEditClass.getSelectedItem() == null) {
            Toast.makeText(EditStudentActivity.this, "Please choose or add class", Toast.LENGTH_LONG).show();
            return;
        } else {
            strClass = spEditClass.getSelectedItem().toString();
        }

        if (TextUtils.isEmpty(strStudentName) || TextUtils.isEmpty(strBirthday) || TextUtils.isEmpty(strClass)) {
            Toast.makeText(EditStudentActivity.this, "Please enter information", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(SchoolDatabase.COLUMN_STUDENT_NAME, strStudentName);
        values.put(SchoolDatabase.COLUMN_DATE_OF_BIRTH, strBirthday);
        values.put(SchoolDatabase.COLUMN_CLASS_STUDENT, strClass);

        Uri uri = Uri.parse(String.valueOf(SchoolContentProvider.CONTENT_URI_STUDENT));
        getContentResolver().update(uri, values, SchoolDatabase.COLUMN_DATE_OF_BIRTH + " =?", new String[]{student.getDate()});
        Toast.makeText(this, "Edit successfully", Toast.LENGTH_SHORT).show();
    }

    private ArrayList<String> getClassName(){
        ArrayList<String> getNameClass =  new ArrayList<>();
        Uri uri = SchoolContentProvider.CONTENT_URI_CLASS;
        Cursor c = getContentResolver().query(uri, null, null, null, null);
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
    private void onBack(){
        Intent intentEditResult = new Intent();
        setResult(RESULT_CANCELED, intentEditResult);
        finish();
    }


}