package leeshani.com.roomdatabases.ui.addstudent;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import leeshani.com.roomdatabases.R;
import leeshani.com.roomdatabases.data.db.StudentAndClassDatabase;
import leeshani.com.roomdatabases.data.model.ClassStudent;
import leeshani.com.roomdatabases.data.model.Student;
import leeshani.com.roomdatabases.ui.TakePhotoBottomDialogFragment;
import leeshani.com.roomdatabases.ui.addclass.AddClassActivity;

public class AddStudentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etName, etBirthday;
    private Button btnAddClass, btnAddStudent;
    private ImageView ivCalender;
    private ImageView ivStudentImage;
    private Calendar birthday;
    private Spinner spClass;
    public static final String KEY_GET_DATA = "data";
    public static final String DATE_FORMAT = "dd/MM/yyy";
    public int change_date_to_second = 1000 * 60 * 60 * 24;
    List<Student> mStudents;

    private String unKnow;
    private Uri imageURI;
    TakePhotoBottomDialogFragment takePhotoBottomDialogFragment;

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
    private final ActivityResultLauncher<Intent> takePhoto = registerForActivityResult
            (new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK) {
                                if (result.getData() != null) {
                                    Bundle bundle = result.getData().getExtras();
                                    if (bundle == null) {
                                        return;
                                    }
                                    insertImageFromGallery(bundle);
                                }
                            }
                        }
                    });

    private final ActivityResultLauncher<Intent> getImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null) {
                            imageURI = result.getData().getData();
                            Glide.with(AddStudentActivity.this)
                                    .load(imageURI)
                                    .into(ivStudentImage);
                        }

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

        ivStudentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openButtonSheetTakeOrChoosePhoto();
            }
        });

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
        ivStudentImage = findViewById(R.id.ivStudent);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddStudentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                birthday.set(i, i1, i2);
                                int timeDistance = (int) ((birthday.getTimeInMillis()
                                        - Calendar.getInstance().getTimeInMillis()) / change_date_to_second);
                                if (timeDistance > 0) {
                                    Toast.makeText(AddStudentActivity.this, R.string.choose_right_date,
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    etBirthday.setText(simpleDateFormat.format(birthday.getTime()));
                                }
                            }
                        }, year, month, date);
                datePickerDialog.show();
            }
        });
    }

    private void setSpinner() {
        unKnow = getString(R.string.unknown);
        ArrayList<String> arClasses = new ArrayList<>();

        arClasses.add(unKnow);

        StudentAndClassDatabase.getInstance(AddStudentActivity.this).classDAO().getListClass()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ClassStudent>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<ClassStudent> classStudents) {
                        for (int i = 0; i < classStudents.size(); i++) {
                            arClasses.add(classStudents.get(i).getName());
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddStudentActivity.this, android.R.layout.simple_spinner_item, arClasses);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spClass.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void addStudent() {
        String strStudentName = etName.getText().toString().trim();
        String strBirthday = etBirthday.getText().toString().trim();
        String strClass;
        String imageUri;
        if (imageURI == null) {
            Toast.makeText(AddStudentActivity.this, R.string.add_photo, Toast.LENGTH_SHORT).show();
            return;
        } else {
            imageUri = imageURI.toString().trim();
        }
        if (spClass.getSelectedItem().toString().equals(unKnow)) {
            Toast.makeText(AddStudentActivity.this, R.string.choose_add_class, Toast.LENGTH_LONG).show();
            return;
        } else {
            strClass = spClass.getSelectedItem().toString();
        }
        if (TextUtils.isEmpty(strStudentName) || TextUtils.isEmpty(strBirthday)) {
            Toast.makeText(AddStudentActivity.this, R.string.add_information, Toast.LENGTH_LONG).show();
        } else {

            Student student = new Student(strStudentName, strBirthday, strClass, imageUri);
            if (checkExit(student)) {
                Toast.makeText(this, R.string.student_exit, Toast.LENGTH_LONG).show();
                return;
            }
            StudentAndClassDatabase.getInstance(AddStudentActivity.this).studentDAO().insertUser(student)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            etName.setText(null);
                            etBirthday.setText(null);
                            ivStudentImage.setImageResource(0);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        }
    }

    private boolean checkExit(Student student) {
        List<Student> list = StudentAndClassDatabase.getInstance(this).
                studentDAO().checkStudent(student.getStudentName(),
                        student.getDate());
        return list != null && !list.isEmpty();
    }

    private void insertImageFromGallery(Bundle bundle) {
        Bitmap bitmap = (Bitmap) bundle.get(KEY_GET_DATA);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getApplicationContext()
                .getContentResolver(), bitmap, getString(R.string.val), null);
        imageURI = Uri.parse(path);
        Glide.with(AddStudentActivity.this)
                .load(imageURI)
                .into(ivStudentImage);
    }

    private void openButtonSheetTakeOrChoosePhoto() {

        takePhotoBottomDialogFragment = new TakePhotoBottomDialogFragment();

        takePhotoBottomDialogFragment.setImage(new TakePhotoBottomDialogFragment.OnListener() {
            @Override
            public void ChooseImage() {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                getImage.launch(gallery);
            }

            @Override
            public void TakePhoto() {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePhoto.launch(takePictureIntent);
            }
        });
        takePhotoBottomDialogFragment.show(getSupportFragmentManager(), null);
    }

    private void onBack() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}