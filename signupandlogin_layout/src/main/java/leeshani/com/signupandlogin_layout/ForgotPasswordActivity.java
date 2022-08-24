package leeshani.com.signupandlogin_layout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
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
    private void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private void anhXa(){
        cvSend = (CardView) findViewById(R.id.cvSend);
        btnSend = (Button) findViewById(R.id.btnSend) ;
        tilEmail = (TextInputLayout) findViewById(R.id.tilEmailForgotPassword);
        etEmail = (EditText) findViewById(R.id.etEmailForgot);
        toolbar = (Toolbar) findViewById(R.id.toolbarForgotPassword);
        txtErrorEmail = (TextView) findViewById(R.id.txtErrorEmailForgot);
        ivEmail = findViewById(R.id.imageViewEmailForgot);
    }

    private void addChangerListenerEmail() {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @SuppressLint("ResourceType")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!MainActivity.isValidEmail(etEmail.getText())){
                    MainActivity.errorText(tilEmail,txtErrorEmail,"Not a valid email address.Should ne your@email.com",ivEmail);
                    cvSend.setBackgroundResource(R.drawable.btn_not_allow);
                    btnSend.setBackgroundResource(R.drawable.btn_not_allow);
                }else{
                    MainActivity.rightText(tilEmail,txtErrorEmail,ivEmail);
                    cvSend.setBackgroundResource(R.drawable.btn);
                    btnSend.setBackgroundResource(R.drawable.btn);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}