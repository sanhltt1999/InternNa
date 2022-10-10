package leeshani.com.content_provider_sqllite.ui.addstudent;

import static leeshani.com.content_provider_sqllite.data.SchoolDatabase.COLUMN_CLASS_STUDENT;

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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import leeshani.com.content_provider_sqllite.R;
import leeshani.com.content_provider_sqllite.SchoolContentProvider;
import leeshani.com.content_provider_sqllite.data.SchoolDatabase;
import leeshani.com.content_provider_sqllite.ui.addclass.AddClassActivity;

public class AddStudentActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText etName, etBirthday;
    private Button btnAddClass, btnAddStudent;
    private ImageView ivCalender;
    private Calendar birthday;
    private Spinner spClass;
    ArrayAdapter<String> arrayAdapter;
    String unknown = "Unknown";
    private static final String DATE_FORMAT = "dd/MM/yyyy";


    private final ActivityResultLauncher<Intent> addClass = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_CANCELED) {
                        setSpinner();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        InitUI();

        setToolbar();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        setBirthday();


        setSpinner();

        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itAddClass = new Intent(AddStudentActivity.this, AddClassActivity.class);
                addClass.launch(itAddClass);
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setBirthday() {
        ivCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
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
        ArrayList<String> arClasses;
        arClasses = getClassName();
        arClasses.add(unknown);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arClasses);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClass.setAdapter(arrayAdapter);
    }

    private void addStudent() {
        String strStudentName = etName.getText().toString().trim();
        String strBirthday = etBirthday.getText().toString().trim();
        String strClass;
        if (spClass.getSelectedItem() == unknown) {
            Toast.makeText(AddStudentActivity.this, R.string.choose_or_add_class, Toast.LENGTH_LONG).show();
            return;

        } else {
            strClass = spClass.getSelectedItem().toString();
        }
        if (TextUtils.isEmpty(strStudentName) || TextUtils.isEmpty(strBirthday) || TextUtils.isEmpty(strClass)) {
            Toast.makeText(AddStudentActivity.this, R.string.enter_information, Toast.LENGTH_LONG).show();
        } else {
            ContentValues values = new ContentValues();
            values.put(SchoolDatabase.COLUMN_STUDENT_NAME, strStudentName);
            values.put(SchoolDatabase.COLUMN_DATE_OF_BIRTH, strBirthday);
            values.put(SchoolDatabase.COLUMN_CLASSNAME, strClass);

            getContentResolver().insert(SchoolContentProvider.CONTENT_URI_STUDENT, values);

            etName.setText(null);
            etBirthday.setText(null);
        }
    }

    private ArrayList<String> getClassName() {
        ArrayList<String> getNameClass = new ArrayList<>();
        Uri uri = SchoolContentProvider.CONTENT_URI_CLASS;
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                getNameClass.add(c.getString(c.getColumnIndexOrThrow(COLUMN_CLASS_STUDENT)));
            }
            c.close();
        } else {
            Toast.makeText(this, R.string.pls_add_new_class, Toast.LENGTH_SHORT).show();
        }
        return getNameClass;
    }

    private void onBack() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}