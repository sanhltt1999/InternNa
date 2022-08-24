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
    public final int maxLengthPassword = 8;

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
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
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
        tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
       tilName = (TextInputLayout) findViewById(R.id.txtInPutLayOutName);
       etName = (EditText) findViewById(R.id.etName);
        txtErrorName = (TextView) findViewById(R.id.txtErrorname);
        txtErrorEmail = (TextView) findViewById(R.id.txtErrorEmail);
        txtErrorPassword = (TextView) findViewById(R.id.txtErrorPassword);
        ivEmail = (ImageView) findViewById(R.id.imageViewEmail);
        ivName = (ImageView) findViewById(R.id.imageViewname);
        ivPassword = (ImageView) findViewById(R.id.imageViewPassword);
        ivIntentLogin = (ImageView) findViewById(R.id.right_arrow);
    }

    private void addTextChangedListenerName (){
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().matches("[a-zA-Z]+")){
                    errorText(tilName,txtErrorName,"Please enter your name",ivName);
                }else{
                    rightText(tilName,txtErrorName,ivName);
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
                if(!isValidEmail(etEmail.getText())){
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

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()<maxLengthPassword){
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

   public static void errorText(TextInputLayout textInputLayout, TextView txtMessageError, String messageError, ImageView imageView){
        textInputLayout.setBackgroundResource(R.drawable.text_input_layout);
        txtMessageError.setText(messageError);
        imageView.setImageResource(R.drawable.ic_clear);
    }
    public static void rightText(TextInputLayout textInputLayout, TextView txtMessageError, ImageView imageView){
        txtMessageError.setText(null);
        textInputLayout.setBackgroundResource(R.drawable.edtsignup);
        imageView.setImageResource(R.drawable.ic_right_text);
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}