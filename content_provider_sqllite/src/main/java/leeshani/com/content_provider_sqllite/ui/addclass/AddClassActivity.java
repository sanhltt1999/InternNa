package leeshani.com.content_provider_sqllite.ui.addclass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import leeshani.com.content_provider_sqllite.R;
import leeshani.com.content_provider_sqllite.data.SchoolDatabase;
import leeshani.com.content_provider_sqllite.data.model.ClassStudent;


public class AddClassActivity extends AppCompatActivity {
    private EditText etClassName, etDateCreate, etTeacher;
    private ImageView ivCalendar;
    private Button btAddClass;
    private Calendar dateCreate;
    private Toolbar toolbar;
    SchoolDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        InitUI();

        setToolbar();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setDateCeate();

        btAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClass();
            }
        });

    }

    private void InitUI() {
        etClassName = findViewById(R.id.edtClassName);
        etDateCreate = findViewById(R.id.edtDateCreate);
        etTeacher = findViewById(R.id.edtTeacher);
        ivCalendar = findViewById(R.id.ivDateCreate);
        btAddClass = findViewById(R.id.btAddClassInList);
        toolbar = findViewById(R.id.AddClassToolbar);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setDateCeate() {
        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                dateCreate = Calendar.getInstance();
                int date = dateCreate.get(Calendar.DATE);
                int month = dateCreate.get(Calendar.MONTH);
                int year = dateCreate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddClassActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateCreate.set(i, i1, i2);
                        etDateCreate.setText(simpleDateFormat.format(dateCreate.getTime()));
                    }
                }, year, month, date);
                datePickerDialog.show();
            }
        });
    }

    private void addClass() {
        String nameClass = etClassName.getText().toString().trim();
        String dateCreate = etDateCreate.getText().toString().trim();
        String teacher = etTeacher.getText().toString().trim();

        if (TextUtils.isEmpty(nameClass) || TextUtils.isEmpty(dateCreate) || TextUtils.isEmpty(teacher)) {
            Toast.makeText(this, "please enter entire information", Toast.LENGTH_SHORT).show();
            return;
        }
        ClassStudent classStudent = new ClassStudent(nameClass, dateCreate, teacher);

        database = new SchoolDatabase(this);
        database.addClass(classStudent);
        etTeacher.setText(null);
        etDateCreate.setText(null);
        etClassName.setText(null);

    }

    private boolean checkExit(ClassStudent classStudent) {
        return true;
    }

}