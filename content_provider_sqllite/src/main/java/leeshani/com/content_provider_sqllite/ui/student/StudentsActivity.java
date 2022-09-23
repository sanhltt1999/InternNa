package leeshani.com.content_provider_sqllite.ui.student;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import leeshani.com.content_provider_sqllite.ChooseFilterClassBottomSheetFragment;
import leeshani.com.content_provider_sqllite.ConfirmDeleteStudentDialogFragment;
import leeshani.com.content_provider_sqllite.R;
import leeshani.com.content_provider_sqllite.data.SchoolDatabase;
import leeshani.com.content_provider_sqllite.data.model.StudentData;
import leeshani.com.content_provider_sqllite.ui.addstudent.AddStudentActivity;
import leeshani.com.content_provider_sqllite.data.model.ClassStudent;
import leeshani.com.content_provider_sqllite.ui.editstudent.EditStudentActivity;
import leeshani.com.content_provider_sqllite.data.model.Student;
import leeshani.com.content_provider_sqllite.ui.student.adapter.StudentAdapter;

public class StudentsActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_STUDENT = 83;
    private Toolbar tbStudent;
    private ImageView imAddStudent;
    private RecyclerView rvStudent;
    private Button btnChooseClass;
    private TextView tvStudentTotal;

    private StudentAdapter studentAdapter;
    private List<Student> students;
    private List<ClassStudent> classStudents;
    SchoolDatabase database;

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
        students = new ArrayList<>();
        studentAdapter.setData(students);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvStudent.setLayoutManager(linearLayoutManager);
        rvStudent.setAdapter(studentAdapter);

        btnChooseClass.setOnClickListener(new View.OnClickListener() {
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
        btnChooseClass = findViewById(R.id.btChooseClass);
        tvStudentTotal = findViewById(R.id.tvStudentTotal);
    }

    private void openBottomSheetDialog() {

        List<String> nameClasses = new ArrayList<>();

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
                btnChooseClass.setText(className);
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
            String currentClass = btnChooseClass.getText().toString();
            students = getListStudent();
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
    }

    private void clickDeleteStudent(Student student) {
        ConfirmDeleteStudentDialogFragment confirmDeleteDialog = new ConfirmDeleteStudentDialogFragment();
        confirmDeleteDialog.setOnListener(new ConfirmDeleteStudentDialogFragment.OnListener() {
            @Override
            public void ConfirmDelete() {
                List<Student> studentInClass = new ArrayList<>();
                String currentClass = btnChooseClass.getText().toString();
                students = getListStudent();
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
    private ArrayList<String> getClassName(){
        ArrayList<String> getNameClass =  new ArrayList<>();
        database = new SchoolDatabase(this);
        Cursor cursor = database.readAllTable();
        if (cursor.getCount() == 0){
            return getNameClass;
        }else {
            while (cursor.moveToNext()){
                getNameClass.add(cursor.getString(StudentData.STUDENT_NAME.getValue()));
            }
            return getNameClass;
        }
    }
    private List<Student> getListStudent(){
        List<Student> list = new ArrayList<>();
        database =  new SchoolDatabase(this);
        Cursor cursor =  database.readAllTable();
        if (cursor.getCount()==0){
            Toast.makeText(this, "Please add students", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                Student student = new Student(cursor.getString(1), cursor.getString(2), cursor.getString(3));
                list.add(student);
            }
        }
        return list;
    }

}