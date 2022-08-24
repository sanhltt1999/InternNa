package leeshani.com.signupandlogin_layout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtErrorEmail, txtErrorPassword;
    private TextInputLayout  tilEmail, tilPassword;
    private EditText  etEmail, etPassword;
    private ImageView ivEmail, ivPassword, ivIntentForgotPassword;
    MainActivity mainActivity ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();
        setToolbar();
        addChangerListenerEmail();
        addChangerListenerPassword();

        ivIntentForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private void anhXa(){
        tilEmail = (TextInputLayout) findViewById(R.id.tilEmailLogin);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPasswordLogin);
        etPassword = (EditText) findViewById(R.id.etPasswordLogin);
        etEmail = (EditText) findViewById(R.id.etEmailLogin);
        toolbar = (Toolbar) findViewById(R.id.toolbarLogin);
        txtErrorEmail = (TextView) findViewById(R.id.txtErrorEmailLogin);
        txtErrorPassword = (TextView) findViewById(R.id.txtErrorPasswordLogin);
        ivEmail = (ImageView) findViewById(R.id.imageViewEmailLogin);
        ivPassword = (ImageView) findViewById(R.id.imageViewPasswordLogin);
        ivIntentForgotPassword = (ImageView) findViewById(R.id.right_arrow_Login);
    }


    private void addChangerListenerEmail() {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @SuppressLint("ResourceType")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().contains("@") && !charSequence.toString().isEmpty()){
                    errorText(tilEmail,txtErrorEmail,"Not a valid email address",ivEmail);
                }else{
                    rightText(tilEmail,txtErrorEmail,ivEmail);
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

            @SuppressLint("ResourceType")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()<8){
                    errorText(tilPassword,txtErrorPassword,"Password do not match",ivPassword);
                }else{
                    rightText(tilPassword,txtErrorPassword,ivPassword);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    private void errorText(TextInputLayout textInputLayout,TextView txtMessageError,String messageError,ImageView imageView){
        textInputLayout.setBackgroundResource(R.drawable.text_input_layout);
        txtMessageError.setText(messageError);
        imageView.setImageResource(R.drawable.ic_clear);
    }
    private void rightText(TextInputLayout textInputLayout, TextView txtMessageError, ImageView imageView){
        txtMessageError.setText(null);
        textInputLayout.setBackgroundResource(R.drawable.edtsignup);
        imageView.setImageResource(R.drawable.ic_right_text);
    }

}