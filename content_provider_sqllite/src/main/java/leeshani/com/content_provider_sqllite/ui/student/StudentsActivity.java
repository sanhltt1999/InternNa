package leeshani.com.content_provider_sqllite.ui.student;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import leeshani.com.content_provider_sqllite.ChooseFilterClassBottomSheetFragment;
import leeshani.com.content_provider_sqllite.ConfirmDeleteStudentDialogFragment;
import leeshani.com.content_provider_sqllite.R;
import leeshani.com.content_provider_sqllite.SchoolContentProvider;
import leeshani.com.content_provider_sqllite.data.SchoolDatabase;
import leeshani.com.content_provider_sqllite.data.model.Student;
import leeshani.com.content_provider_sqllite.ui.addstudent.AddStudentActivity;
import leeshani.com.content_provider_sqllite.ui.editstudent.EditStudentActivity;
import leeshani.com.content_provider_sqllite.ui.student.adapter.StudentAdapter;

public class StudentsActivity extends AppCompatActivity {
    private Toolbar tbStudent;
    private ImageView imAddStudent;
    private RecyclerView rvStudent;
    private TextView tvStudentTotal;
    private TextView tvChooseClass;

    private StudentAdapter studentAdapter;
    private List<Student> students;

    private ActivityResultLauncher<Intent> backActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        students = getListStudent();
                        tvStudentTotal.setText(""+students.size());
                        studentAdapter.setData(students);
                    }

                }
            });

    private ActivityResultLauncher<Intent> editStudent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_CANCELED){
                        students = getListStudent();
                        rvStudent.setAdapter(studentAdapter);
                        tvStudentTotal.setText("" + students.size());
                        studentAdapter.setData(students);
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        InitUI();

        setToolbar();

        studentAdapter = new StudentAdapter(new StudentAdapter.IClickStudent() {
            @Override
            public void editStudent(Student student) {
                clickEditStudent(student);
            }

            @Override
            public void deleteStudent(Student student) {
                clickDeleteStudent(student);
            }
        });
        students = getListStudent();
        tvStudentTotal.setText("" +students.size());
        studentAdapter.setData(students);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvStudent.setLayoutManager(linearLayoutManager);
        rvStudent.setAdapter(studentAdapter);

        tvChooseClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheetDialog();
            }
        });

        imAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backActivity.launch(new Intent(StudentsActivity.this,AddStudentActivity.class));
            }
        });

    }

    private void setToolbar() {
        setSupportActionBar(tbStudent);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void InitUI() {
        tbStudent = findViewById(R.id.TbStudent);
        imAddStudent = findViewById(R.id.imAdd);
        rvStudent = findViewById(R.id.rvStudent);
        tvChooseClass = findViewById(R.id.tvChooseClass);
        tvStudentTotal = findViewById(R.id.tvStudentTotal);
    }

    private void openBottomSheetDialog() {

        List<String> nameClasses ;

        nameClasses = getClassName();

        String[] classes = new String[nameClasses.size()];
        nameClasses.toArray(classes);

        ChooseFilterClassBottomSheetFragment chooseFilterClassBottomSheetDialog = new ChooseFilterClassBottomSheetFragment(classes);
        chooseFilterClassBottomSheetDialog.setOnListener(new ChooseFilterClassBottomSheetFragment.OnListener() {
            @Override
            public void ChooseClass(String className) {
                List<Student> studentInClass = new ArrayList<>();
                students = getListStudent();

                if (students.size() == 0) {
                    Toast.makeText(StudentsActivity.this, "Please add student in chosen class", Toast.LENGTH_SHORT).show();
                    chooseFilterClassBottomSheetDialog.dismiss();
                    return;
                }
                for (int i = 0; i < students.size(); i++) {
                    String nameClass = students.get(i).getClasses();
                    if (nameClass.equals(className)) {
                        studentInClass.add(students.get(i));
                    }
                }
                if (studentInClass.size() == 0) {
                    Toast.makeText(StudentsActivity.this, "Please add student in chosen class", Toast.LENGTH_SHORT).show();
                    chooseFilterClassBottomSheetDialog.dismiss();
                    return;
                }
                tvChooseClass.setText(className);
                tvStudentTotal.setText("" + studentInClass.size());
                studentAdapter.setData(studentInClass);
                chooseFilterClassBottomSheetDialog.dismiss();
            }

        });
        chooseFilterClassBottomSheetDialog.show(getSupportFragmentManager(), "");
    }

    private void clickEditStudent(Student student) {
        Uri uri1 = SchoolContentProvider.CONTENT_URI_STUDENT;
        Intent itEdit = new Intent(StudentsActivity.this, EditStudentActivity.class);
        Bundle bundleStudent = new Bundle();
        bundleStudent.putSerializable("object_student", student);
        itEdit.putExtras(bundleStudent);
        editStudent.launch(itEdit);

    }

    private void clickDeleteStudent(Student student) {
        ConfirmDeleteStudentDialogFragment confirmDeleteDialog = new ConfirmDeleteStudentDialogFragment();
        confirmDeleteDialog.setOnListener(new ConfirmDeleteStudentDialogFragment.OnListener() {
            @Override
            public void ConfirmDelete() {
                Uri uri = Uri.parse(String.valueOf(SchoolContentProvider.CONTENT_URI_STUDENT));
                getContentResolver().delete(uri,SchoolDatabase.COLUMN_DATE_OF_BIRTH + " =?", new String[]{student.getDate()});
                students = getListStudent();
                if (students.size() == 0){
                    Toast.makeText(StudentsActivity.this, "Add student", Toast.LENGTH_SHORT).show();
                }else{
                    rvStudent.setAdapter(studentAdapter);
                    tvStudentTotal.setText("" + students.size());
                    studentAdapter.setData(students);
                    confirmDeleteDialog.dismiss();
                }
            }
        });
        confirmDeleteDialog.show(getSupportFragmentManager(),ConfirmDeleteStudentDialogFragment.TAG);
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

    private List<Student> getListStudent(){
        List<Student> list = new ArrayList<>();

        Uri uri1 = SchoolContentProvider.CONTENT_URI_STUDENT;
        Cursor c = getContentResolver().query(uri1, null, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                Student student = new Student(c.getString(c.getColumnIndexOrThrow(SchoolDatabase.COLUMN_STUDENT_NAME)),
                        c.getString(c.getColumnIndexOrThrow("date_of_birth")), c.getString(c.getColumnIndexOrThrow("class_name")));
                list.add (student);
            }
            c.close();
        }
        return list;
    }

}