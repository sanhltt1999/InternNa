package leeshani.com.sharedpreferences;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;


public class EdtProfile extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tvSave;
    private ImageButton ibTakePhoto;
    private ImageView ivAvt, ivCalendar;
    private EditText etUsername, etEmail, etPhone, etGender, etDateOfBirth;
    final static int REQUEST_CODE_CAMERA = 12;
    final static int PICK_IMAGE = 1;
    Uri ImageURI;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        InitUI();
        setToolbar();
        sharedPreferences = getSharedPreferences("dataInfor", MODE_PRIVATE);
        etUsername.setText(sharedPreferences.getString("username", null));
        etEmail.setText(sharedPreferences.getString("email", null));
        etGender.setText(sharedPreferences.getString("gender", null));
        etPhone.setText(sharedPreferences.getString("phone", null));
        etDateOfBirth.setText(sharedPreferences.getString("date of birth", null));

        Glide.with(this)
                .load(sharedPreferences.getString("image", null))
                .into(ivAvt);

        setDateOfBirth();
        takePhoto();

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String gender = etGender.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String date = etDateOfBirth.getText().toString();
                String uriImage = ImageURI.toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                editor.putString("email", email);
                editor.putString("gender", gender);
                editor.putString("phone", phone);
                editor.putString("date of birth", date);
                editor.putString("image", uriImage);
                editor.apply();
            }
        });
    }

    private void takePhoto() {
        ibTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(EdtProfile.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_choose_file);

                Window window = dialog.getWindow();
                if (window == null) return;
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                windowAttributes.gravity = Gravity.CENTER_HORIZONTAL;
                window.setAttributes(windowAttributes);
                dialog.show();

                Button choosePicture = dialog.findViewById(R.id.chooseImage);
                Button takePhoto = dialog.findViewById(R.id.takePhotoCamera);
                takePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityIfNeeded(takePictureIntent, REQUEST_CODE_CAMERA);
                    }
                });

                choosePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        startActivityIfNeeded(gallery, PICK_IMAGE);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap, "val", null);
            ImageURI = Uri.parse(path);
            Glide.with(this)
                    .load(ImageURI)
                    .into(ivAvt);
        }
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            ImageURI = data.getData();
            Glide.with(this)
                    .load(ImageURI)
                    .into(ivAvt);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setDateOfBirth() {
        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int date = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EdtProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(i, i1, i2);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        etDateOfBirth.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, year, month, date);
                datePickerDialog.show();
            }
        });
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    private void InitUI() {
        toolbar = findViewById(R.id.toolbar1);
        ibTakePhoto = findViewById(R.id.takePhoto);
        ivAvt = findViewById(R.id.imAvt);
        etUsername = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etGender = findViewById(R.id.etGender);
        etDateOfBirth = findViewById(R.id.etDateOfBirth);
        ivCalendar = findViewById(R.id.calendar);
        tvSave = findViewById(R.id.save);
    }

}