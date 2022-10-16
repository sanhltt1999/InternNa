package leeshani.com.roomdatabases.ui.addclass;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import leeshani.com.roomdatabases.R;
import leeshani.com.roomdatabases.data.db.StudentAndClassDatabase;
import leeshani.com.roomdatabases.data.model.ClassStudent;

public class AddClassActivity extends AppCompatActivity {
    private EditText etClassName;
    private EditText etDateCreate;
    private EditText etTeacher;
    private ImageView ivCalendar;
    private Button btAddClass;
    private Calendar dateCreate;
    private Toolbar toolbar;
    public static final String DATE_FORMAT = "dd/MM/yyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        InitUI();

        setToolbar();

        toolbar.setNavigationOnClickListener(view -> onBack());

        setDateCeate();

        btAddClass.setOnClickListener(view -> addClass());
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
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setDateCeate() {
        ivCalendar.setOnClickListener(view -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            dateCreate = Calendar.getInstance();
            int date = dateCreate.get(Calendar.DATE);
            int month = dateCreate.get(Calendar.MONTH);
            int year = dateCreate.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddClassActivity.this,
                    (datePicker, i, i1, i2) -> {
                        dateCreate.set(i, i1, i2);
                        etDateCreate.setText(simpleDateFormat.format(dateCreate.getTime()));
                    },
                    year,
                    month,
                    date);
            datePickerDialog.show();
        });
    }

    private void addClass() {
        String nameClass = etClassName.getText().toString().trim();
        String dateCreate = etDateCreate.getText().toString().trim();
        String teacher = etTeacher.getText().toString().trim();

        if (TextUtils.isEmpty(nameClass) || TextUtils.isEmpty(dateCreate) || TextUtils.isEmpty(teacher)) {
            Toast.makeText(this, R.string.add_information, Toast.LENGTH_SHORT).show();
        } else {
            ClassStudent classStudent = new ClassStudent(nameClass, dateCreate, teacher);
            if (checkExit(classStudent)) {
                Toast.makeText(this, R.string.class_exit, Toast.LENGTH_SHORT).show();
                return;
            }
            insertClass(classStudent);

        }
    }

    private boolean checkExit(ClassStudent classStudent) {

        List<ClassStudent> list = StudentAndClassDatabase
                .getInstance(AddClassActivity.this).classDAO().checkClass(classStudent.getName());

        return list != null && !list.isEmpty();
    }

    private void insertClass(ClassStudent insertClass) {

        StudentAndClassDatabase.getInstance(AddClassActivity.this).classDAO().insertUser(insertClass)
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        etTeacher.setText(null);
                        etDateCreate.setText(null);
                        etClassName.setText(null);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void onBack() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}