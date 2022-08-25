package leeshani.com.signupandlogin_layout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtErrorEmail;
    private TextInputLayout tilEmail;
    private EditText etEmail;
    private ImageView ivEmail;
    private Button btnSend;
    private CardView cvSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        anhXa();
        setToolbar();
        addChangerListenerEmail();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void anhXa() {
        cvSend = findViewById(R.id.cvSend);
        btnSend = findViewById(R.id.btnSend);
        tilEmail = findViewById(R.id.tilEmailForgotPassword);
        etEmail = findViewById(R.id.etEmailForgot);
        toolbar = findViewById(R.id.toolbarForgotPassword);
        txtErrorEmail = findViewById(R.id.txtErrorEmailForgot);
        ivEmail = findViewById(R.id.imageViewEmailForgot);
    }

    private void addChangerListenerEmail() {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!MainActivity.isValidEmail(etEmail.getText())) {
                    errorText(tilEmail, txtErrorEmail, R.string.error_forgot, ivEmail);
                    cvSend.setBackgroundResource(R.drawable.btn_not_allow);
                    btnSend.setBackgroundResource(R.drawable.btn_not_allow);
                } else {
                    rightText(tilEmail, txtErrorEmail, ivEmail);
                    cvSend.setBackgroundResource(R.drawable.btn);
                    btnSend.setBackgroundResource(R.drawable.btn);
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
}