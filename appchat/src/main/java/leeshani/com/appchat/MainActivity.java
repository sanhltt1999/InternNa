package leeshani.com.appchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MessageSendAdapter messageSendAdapter;
    private List<Message> messages;
    private ImageView ivSend;
    private EditText etInputChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();

        setToolbar();

        messages = new ArrayList<>();
        messageSendAdapter = new MessageSendAdapter();
        messageSendAdapter.setData(messages);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageSendAdapter);

    }

    private void AnhXa() {
        toolbar =  findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rvChat);
        ivSend = findViewById(R.id.send);
        etInputChat = findViewById(R.id.inputMessage);
    }

    private void setToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    

}