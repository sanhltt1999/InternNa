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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
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
    private List<Student> mStudents;
    public static final String KEY_TO_PUT_STUDENT = "object_student";
    ConfirmDeleteStudentDialogFragment confirmDeleteDialog;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    private final ActivityResultLauncher<Intent> backActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {

                        StudentAndClassDatabase.getInstance(StudentsActivity.this).studentDAO().getListStudent()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SingleObserver<List<Student>>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(@NonNull List<Student> students) {
                                        mStudents = students;
                                        tvStudentTotal.setText(String.valueOf(mStudents.size()));
                                        studentAdapter.setData(mStudents);
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                    }
                                });
                    }

                }
            });

    private final ActivityResultLauncher<Intent> deleteStudent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_CANCELED) {
                        showListStudents();
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

        showListStudents();

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
                backActivity.launch(new Intent(StudentsActivity.this, AddStudentActivity.class));
            }
        });

    }

    private void setToolbar() {
        setSupportActionBar(tbStudent);
        if (getSupportActionBar() != null) {
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

        StudentAndClassDatabase.getInstance(StudentsActivity.this).classDAO().getListClass()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ClassStudent>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<ClassStudent> classStudents) {

                        for (int i = 0; i < classStudents.size(); i++) {
                            nameClasses.add(classStudents.get(i).getName());
                        }

                        String[] classes = new String[nameClasses.size()];
                        nameClasses.toArray(classes);
                        ChooseFilterClassBottomSheetFragment chooseFilterClassBottomSheetDialog;
                        chooseFilterClassBottomSheetDialog = new ChooseFilterClassBottomSheetFragment(classes);
                        chooseFilterClassBottomSheetDialog.setOnListener(new ChooseFilterClassBottomSheetFragment.OnListener() {
                            @Override
                            public void ChooseClass(String className) {
                                List<Student> studentInClass = new ArrayList<>();
                                mStudents = StudentAndClassDatabase.getInstance(StudentsActivity.this).studentDAO().getListStudents();

                                if (mStudents.size() == 0) {
                                    Toast.makeText(StudentsActivity.this, R.string.add_student_in_class,
                                            Toast.LENGTH_SHORT).show();
                                    chooseFilterClassBottomSheetDialog.dismiss();
                                    return;
                                }
                                for (int i = 0; i < mStudents.size(); i++) {
                                    String nameClass = mStudents.get(i).getClasses();
                                    if (nameClass.equals(className)) {
                                        studentInClass.add(mStudents.get(i));
                                    }
                                }
                                if (studentInClass.size() == 0) {
                                    Toast.makeText(StudentsActivity.this, R.string.add_student_in_class,
                                            Toast.LENGTH_SHORT).show();
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

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

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
                showListStudents();
                confirmDeleteDialog.dismiss();
            }
        });
        confirmDeleteDialog.show(getSupportFragmentManager(), ConfirmDeleteStudentDialogFragment.TAG);

    }

    private void showListStudents() {
        StudentAndClassDatabase.getInstance(StudentsActivity.this).studentDAO().getListStudent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Student>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Student> students) {
                        rvStudent.setAdapter(studentAdapter);
                        tvStudentTotal.setText(String.valueOf(students.size()));
                        studentAdapter.setData(students);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

}