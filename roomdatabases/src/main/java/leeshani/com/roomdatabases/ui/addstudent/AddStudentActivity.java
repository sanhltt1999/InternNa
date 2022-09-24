package leeshani.com.roomdatabases.ui.addstudent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private List<ClassStudent> classes;
    private static final int REQUEST_CODE_CAMERA = 10;
    private static final int PICK_IMAGE = 101;
    private static final int UPDATE_STUDENT_LIST = 111;
    private static final int UPDATE_SPINNER = 113;
    String unKnow;
    Uri ImageURI;
    String path;
    TakePhotoBottomDialogFragment takePhotoBottomDialogFragment;

    private ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

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
        ivStudentImage = findViewById(R.id.ivStudent);
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
                         int distance = (int) ((birthday.getTimeInMillis() - Calendar.getInstance().getTimeInMillis())/(1000*60*60*24));
                        if (distance > 0){
                            Toast.makeText(AddStudentActivity.this, "Please choose right the date", Toast.LENGTH_SHORT).show();
                        }else{
                        etBirthday.setText(simpleDateFormat.format(birthday.getTime()));}
                    }
                }, year, month, date);
                datePickerDialog.show();
            }
        });
    }

    private void setSpinner() {
        unKnow = "Unknown";
        ArrayList<String> arClasses = new ArrayList<>();

        arClasses.add(unKnow);

        classes = StudentAndClassDatabase.getInstance(AddStudentActivity.this).classDAO().getListClass();

        for (int i = 0; i < classes.size(); i++) {
            arClasses.add(classes.get(i).getName());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arClasses);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spClass.setAdapter(arrayAdapter);
    }

    private void addStudent() {
        String strStudentName = etName.getText().toString().trim();
        String strBirthday = etBirthday.getText().toString().trim();
        String strClass;
        String imageUri;
        if(  ImageURI == null){
            Toast.makeText(AddStudentActivity.this, "Add student photo", Toast.LENGTH_SHORT).show();
            return;
        }else {
            imageUri = ImageURI.toString().trim();
        }
        if (spClass.getSelectedItem().toString() == unKnow) {
            Toast.makeText(AddStudentActivity.this, "Please choose or add class", Toast.LENGTH_LONG).show();
            return;
        } else {
            strClass = spClass.getSelectedItem().toString();
        }
        if (TextUtils.isEmpty(strStudentName) || TextUtils.isEmpty(strBirthday)){
            Toast.makeText(AddStudentActivity.this, "Please enter information", Toast.LENGTH_LONG).show();
        } else {

            Student student = new Student(strStudentName, strBirthday, strClass, imageUri);
            if (checkExit(student)) {
                Toast.makeText(this, "Student exited", Toast.LENGTH_LONG).show();
                return;
            }
            StudentAndClassDatabase.getInstance(AddStudentActivity.this).studentDAO().insertUser(student);
            etName.setText(null);
            etBirthday.setText(null);
        }
    }

    private boolean checkExit(Student student) {
        List<Student> list = StudentAndClassDatabase.getInstance(this).studentDAO().checkStudent(student.getStudentName(), student.getDate());
        return list != null && !list.isEmpty();
    }
    private void openButtonSheetTakeOrChoosePhoto(){

        takePhotoBottomDialogFragment = new TakePhotoBottomDialogFragment();

        takePhotoBottomDialogFragment.setImage(new TakePhotoBottomDialogFragment.OnListener() {
            @Override
            public void ChooseImage() {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityIfNeeded(gallery, PICK_IMAGE);
            }

            @Override
            public void TakePhoto() {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityIfNeeded(takePictureIntent, REQUEST_CODE_CAMERA);
            }
        });
        takePhotoBottomDialogFragment.show(getSupportFragmentManager(),"");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
            path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),bitmap, "val", null);
            ImageURI = Uri.parse(path);
            Glide.with(this)
                    .load(ImageURI)
                    .into(ivStudentImage);
        }

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            ImageURI = data.getData();
            Glide.with(this)
                    .load(ImageURI)
                    .into(ivStudentImage);
        }

        if(requestCode == UPDATE_SPINNER && resultCode == RESULT_OK){
            setSpinner();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onBack() {
        Intent intent = new Intent();
        startActivityIfNeeded(intent, UPDATE_STUDENT_LIST);
    }

}