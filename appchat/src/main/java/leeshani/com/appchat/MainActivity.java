package leeshani.com.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
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

        InitUI();

        setToolbar();

        messages = new ArrayList<>();
        messageSendAdapter = new MessageSendAdapter();
        messageSendAdapter.setData(messages);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageSendAdapter);
        setSendMessage();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setSendMessage() {
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputMessage = etInputChat.getText().toString().trim();
                if (inputMessage.isEmpty()) return;
                messages.add(new Message(inputMessage, true));
                etInputChat.setText(null);
                String receiveMessages = "hihi";
                messages.add(new Message(receiveMessages, false));
                messageSendAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size() - 1);
            }
        });

    }

    private void InitUI() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rvChat);
        ivSend = findViewById(R.id.send);
        etInputChat = findViewById(R.id.inputMessage);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}