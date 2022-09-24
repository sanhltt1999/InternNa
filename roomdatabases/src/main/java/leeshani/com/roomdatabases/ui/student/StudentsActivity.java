package leeshani.com.roomdatabases.ui.student;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import leeshani.com.roomdatabases.ChooseFilterClassBottomSheetFragment;
import leeshani.com.roomdatabases.ConfirmDeleteStudentDialogFragment;
import leeshani.com.roomdatabases.ui.addstudent.AddStudentActivity;
import leeshani.com.roomdatabases.data.db.StudentAndClassDatabase;
import leeshani.com.roomdatabases.data.model.ClassStudent;
import leeshani.com.roomdatabases.ui.editstudent.EditStudentActivity;
import leeshani.com.roomdatabases.R;
import leeshani.com.roomdatabases.data.model.Student;
import leeshani.com.roomdatabases.ui.student.adapter.StudentAdapter;

public class StudentsActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_STUDENT = 83;
    private static final int UPDATE_STUDENT_LIST = 111;
    private Toolbar tbStudent;
    private ImageView imAddStudent;
    private RecyclerView rvStudent;
    private TextView tvChooseClass;
    private TextView tvStudentTotal;

    private StudentAdapter studentAdapter;
    private List<Student> students;
    private List<ClassStudent> classStudents;

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
                Intent intent = new Intent(StudentsActivity.this, AddStudentActivity.class);
                startActivity(intent);
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

        List<String> nameClasses = new ArrayList<>();

        classStudents = StudentAndClassDatabase.getInstance(StudentsActivity.this).classDAO().getListClass();

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
        Intent itEdit = new Intent(StudentsActivity.this, EditStudentActivity.class);
        Bundle bundleStudent = new Bundle();
        bundleStudent.putSerializable("object_student", student);
        itEdit.putExtras(bundleStudent);
        startActivityIfNeeded(itEdit, REQUEST_CODE_STUDENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_STUDENT && resultCode == RESULT_OK) {
            List<Student> studentInClass = new ArrayList<>();
            String currentClass = tvChooseClass.getText().toString();
            students = StudentAndClassDatabase.getInstance(StudentsActivity.this)
                    .studentDAO().getListStudent();
            for (int i = 0; i < students.size(); i++) {
                String nameClass = students.get(i).getClasses();
                if (nameClass.equals(currentClass)) {
                    studentInClass.add(students.get(i));
                }
            }
            rvStudent.setAdapter(studentAdapter);
            tvStudentTotal.setText("" + studentInClass.size());
            studentAdapter.setData(studentInClass);
        }
        if(requestCode == UPDATE_STUDENT_LIST && resultCode == RESULT_OK){
            students = StudentAndClassDatabase.getInstance(StudentsActivity.this)
                    .studentDAO().getListStudent();

            studentAdapter.setData(students);

        }
    }

    private void clickDeleteStudent(Student student) {
        ConfirmDeleteStudentDialogFragment confirmDeleteDialog = new ConfirmDeleteStudentDialogFragment();
        confirmDeleteDialog.setOnListener(new ConfirmDeleteStudentDialogFragment.OnListener() {
            @Override
            public void confirmDelete() {
                StudentAndClassDatabase.getInstance(StudentsActivity.this).studentDAO().deleteStudent(student);
                List<Student> studentInClass = new ArrayList<>();
                String currentClass = tvChooseClass.getText().toString();
                students = StudentAndClassDatabase.getInstance(StudentsActivity.this)
                        .studentDAO().getListStudent();
                for (int j = 0; j < students.size(); j++) {
                    String nameClass = students.get(j).getClasses();
                    if (nameClass.equals(currentClass)) {
                        studentInClass.add(students.get(j));
                    }
                }
                rvStudent.setAdapter(studentAdapter);
                tvStudentTotal.setText("" + studentInClass.size());
                studentAdapter.setData(studentInClass);
            }
        });
        confirmDeleteDialog.show(getSupportFragmentManager(),ConfirmDeleteStudentDialogFragment.TAG);
    }

}