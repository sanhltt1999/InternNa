package leeshani.com.content_provider_sqllite.ui.addclass;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import leeshani.com.content_provider_sqllite.R;
import leeshani.com.content_provider_sqllite.SchoolContentProvider;
import leeshani.com.content_provider_sqllite.data.SchoolDatabase;

public class AddClassActivity extends AppCompatActivity {
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private EditText etClassName, etDateCreate, etTeacher;
    private ImageView ivCalendar;
    private Button btAddClass;
    private Calendar dateCreate;
    private Toolbar toolbar;

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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setDateCeate() {
        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
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
            Toast.makeText(this, R.string.enter_information, Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(SchoolDatabase.COLUMN_CLASSNAME, nameClass);
        values.put(SchoolDatabase.COLUMN_DATE_CREATE_CLASS, dateCreate);
        values.put(SchoolDatabase.COLUMN_TEACHER, teacher);

        getContentResolver().insert(SchoolContentProvider.CONTENT_URI_CLASS, values);

        etTeacher.setText(null);
        etDateCreate.setText(null);
        etClassName.setText(null);

    }

}