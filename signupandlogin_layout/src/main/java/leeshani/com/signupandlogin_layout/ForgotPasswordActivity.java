package leeshani.com.signupandlogin_layout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView txtErrorEmail, txtEmailInstruction;
    private TextInputLayout tilEmail, tilPassword;
    private EditText etEmail, etPassword;
    private ImageView ivEmail, ivPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        anhXa();
        setToolbar();
        addChangerListenerEmail();
    }
    private void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private void anhXa(){
        tilEmail = (TextInputLayout) findViewById(R.id.tilEmailForgotPassword);
        etEmail = (EditText) findViewById(R.id.etEmailForgot);
        toolbar = (Toolbar) findViewById(R.id.toolbarForgotPassword);
        txtErrorEmail = (TextView) findViewById(R.id.txtErrorEmailForgot);
        txtEmailInstruction = (TextView) findViewById(R.id.txtInstruction);
        ivEmail = (ImageView) findViewById(R.id.imageViewEmailForgot);
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

}