package leeshani.com.roomdatabases.ui.student;

import android.content.Intent;
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

import leeshani.com.roomdatabases.ChooseFilterClassBottomSheetFragment;
import leeshani.com.roomdatabases.ConfirmDeleteStudentDialogFragment;
import leeshani.com.roomdatabases.R;
import leeshani.com.roomdatabases.data.db.StudentAndClassDatabase;
import leeshani.com.roomdatabases.data.model.ClassStudent;
import leeshani.com.roomdatabases.data.model.Student;
import leeshani.com.roomdatabases.ui.addstudent.AddStudentActivity;
import leeshani.com.roomdatabases.ui.editstudent.EditStudentActivity;
import leeshani.com.roomdatabases.ui.student.adapter.StudentAdapter;

public class StudentsActivity extends AppCompatActivity {

    private Toolbar tbStudent;
    private ImageView imAddStudent;
    private RecyclerView rvStudent;
    private TextView tvChooseClass;
    private TextView tvStudentTotal;

    private StudentAdapter studentAdapter;
    private List<Student> students;
    public static final String KEY_TO_PUT_STUDENT = "object_student";
    ConfirmDeleteStudentDialogFragment confirmDeleteDialog;

    private final ActivityResultLauncher<Intent> backActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){

                students = StudentAndClassDatabase.getInstance(StudentsActivity.this)
                        .studentDAO().getListStudent();
                tvStudentTotal.setText(String.valueOf(students.size()));
                studentAdapter.setData(students);
            }

        }
    });

    private final ActivityResultLauncher<Intent> deleteStudent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_CANCELED){
                students = StudentAndClassDatabase.getInstance(StudentsActivity.this)
                        .studentDAO().getListStudent();

                rvStudent.setAdapter(studentAdapter);
                tvStudentTotal.setText(String.valueOf(students.size()));
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

        students = StudentAndClassDatabase.getInstance(StudentsActivity.this)
                .studentDAO().getListStudent();
        tvStudentTotal.setText(String.valueOf(students.size()));
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
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

    }

    private void InitUI() {
        tbStudent = findViewById(R.id.TbStudent);
        imAddStudent = findViewById(R.id.imAdd);
        rvStudent = findViewById(R.id.rvStudent);
        tvChooseClass = findViewById(R.id.tvChooseClass);
        tvStudentTotal = findViewById(R.id.tvStudentTotal);
    }

    private void openBottomSheetDialog() {

        List<String> nameClasses = new ArrayList<>();

        List<ClassStudent> classStudents = StudentAndClassDatabase.getInstance(StudentsActivity.this).classDAO().getListClass();

        for (int i = 0; i < classStudents.size(); i++) {
            nameClasses.add(classStudents.get(i).getName());
        }

        String[] classes = new String[nameClasses.size()];
        nameClasses.toArray(classes);

        ChooseFilterClassBottomSheetFragment chooseFilterClassBottomSheetDialog = new ChooseFilterClassBottomSheetFragment(classes);
        chooseFilterClassBottomSheetDialog.setOnListener(new ChooseFilterClassBottomSheetFragment.OnListener() {
            @Override
            public void ChooseClass(String className) {
                List<Student> studentInClass = new ArrayList<>();
                students = StudentAndClassDatabase.getInstance(StudentsActivity.this)
                        .studentDAO().getListStudent();

                if (students.size() == 0) {
                    Toast.makeText(StudentsActivity.this, R.string.add_student_in_class, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(StudentsActivity.this, R.string.add_student_in_class, Toast.LENGTH_SHORT).show();
                    chooseFilterClassBottomSheetDialog.dismiss();
                    return;
                }
                tvChooseClass.setText(className);
                tvStudentTotal.setText(String.valueOf(studentInClass.size()));
                studentAdapter.setData(studentInClass);
                chooseFilterClassBottomSheetDialog.dismiss();
            }

        });
        chooseFilterClassBottomSheetDialog.show(getSupportFragmentManager(), null);
    }

    private void clickEditStudent(Student student) {
        Intent itEdit = new Intent(StudentsActivity.this, EditStudentActivity.class);
        Bundle bundleStudent = new Bundle();
        bundleStudent.putSerializable(KEY_TO_PUT_STUDENT, student);
        itEdit.putExtras(bundleStudent);
        deleteStudent.launch(itEdit);
    }

    private void clickDeleteStudent(Student student) {
        confirmDeleteDialog = new ConfirmDeleteStudentDialogFragment();
        confirmDeleteDialog.setOnListener(new ConfirmDeleteStudentDialogFragment.OnListener() {
            @Override
            public void confirmDelete() {
                StudentAndClassDatabase.getInstance(StudentsActivity.this).studentDAO().deleteStudent(student);
                students = StudentAndClassDatabase.getInstance(StudentsActivity.this)
                        .studentDAO().getListStudent();

                rvStudent.setAdapter(studentAdapter);
                tvStudentTotal.setText(String.valueOf(students.size()));
                studentAdapter.setData(students);
                confirmDeleteDialog.dismiss();
            }
        });
        confirmDeleteDialog.show(getSupportFragmentManager(),ConfirmDeleteStudentDialogFragment.TAG);

    }

}