package leeshani.com.signupandlogin_layout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtErrorName, txtErrorEmail, txtErrorPassword;
    private TextInputLayout tilName, tilEmail, tilPassword;
    private EditText etName, etEmail, etPassword;
    private ImageView ivEmail, ivName, ivPassword, ivIntentLogin;
    public static final int MAX_LENGTH_PASSWORD = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        setToolbar();
        addTextChangedListenerName();
        addChangerListenerEmail();
        addChangerListenerPassword();
        ivIntentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void anhXa() {
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        toolbar = findViewById(R.id.toolbar);
        tilName = findViewById(R.id.txtInPutLayOutName);
        etName = findViewById(R.id.etName);
        txtErrorName = findViewById(R.id.txtErrorname);
        txtErrorEmail = findViewById(R.id.txtErrorEmail);
        txtErrorPassword = findViewById(R.id.txtErrorPassword);
        ivEmail = findViewById(R.id.imageViewEmail);
        ivName = findViewById(R.id.imageViewname);
        ivPassword = findViewById(R.id.imageViewPassword);
        ivIntentLogin = findViewById(R.id.right_arrow);
    }

    private void addTextChangedListenerName() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().matches("[a-zA-Z]+")) {
                    errorText(tilName, txtErrorName, R.string.error_name, ivName);
                } else {
                    rightText(tilName, txtErrorName, ivName);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void addChangerListenerEmail() {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("ResourceType")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isValidEmail(etEmail.getText())) {
                    errorText(tilEmail, txtErrorEmail, R.string.error_email, ivEmail);
                } else {
                    rightText(tilEmail, txtErrorEmail, ivEmail);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void addChangerListenerPassword() {
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < MAX_LENGTH_PASSWORD) {
                    errorText(tilPassword, txtErrorPassword, R.string.error_password, ivPassword);
                } else {
                    rightText(tilPassword, txtErrorPassword, ivPassword);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void errorText(TextInputLayout textInputLayout, TextView txtMessageError, int messageError, ImageView imageView) {
        textInputLayout.setBackgroundResource(R.drawable.text_input_layout);
        txtMessageError.setText(messageError);
        imageView.setImageResource(R.drawable.ic_clear);
    }

    private void rightText(TextInputLayout textInputLayout, TextView txtMessageError, ImageView imageView) {
        txtMessageError.setText(null);
        textInputLayout.setBackgroundResource(R.drawable.edtsignup);
        imageView.setImageResource(R.drawable.ic_right_text);
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}