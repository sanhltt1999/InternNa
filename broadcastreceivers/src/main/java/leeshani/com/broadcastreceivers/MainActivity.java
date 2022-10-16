package leeshani.com.broadcastreceivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {
    private SwitchCompat switchWifi;
    private EditText etNumberA;
    private EditText etNumberB;
    private TextView tvShowResult;
    private Button btPutData;
    private static final int ACTION_ADD = 3;
    private static final int ACTION_SUBTRACT = 5;
    private static final int ACTION_MULTIPLE = 7;
    private static final int ACTION_DIVIDE = 9;
    private static final String KEY_INTENT_TO_BROADCAST = "action_calculation";
    private static final String ACTION = "calculation";
    private static final int ID_CHANNEL = 1;
    public static final String CHANNEL_ID = "CHANNEL_1";
    private int numberA;
    private int numberB;
    MyBroadCast myBroadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitUI();
        createNotificationChannel();

        myBroadCast = new MyBroadCast();
        myBroadCast.setOnListener(new MyBroadCast.OnListener() {
            @Override
            public void changedStatus(boolean status) {
                switchWifi.setChecked(status);
            }

            @Override
            public void add() {
                getNumber();
                showResult(getString(R.string.add), numberA + numberB);
            }

            @Override
            public void subtract() {
                getNumber();
                showResult(getString(R.string.subtract), numberA - numberB);
            }

            @Override
            public void multiple() {
                getNumber();
                showResult(getString(R.string.multiple), numberA * numberB);
            }

            @Override
            public void divide() {
                getNumber();
                if (numberB == 0) {
                    Toast.makeText(MainActivity.this, R.string.enter_khac_0, Toast.LENGTH_SHORT).show();
                } else {
                    showResult(getString(R.string.divide), numberA / numberB);
                }
            }
        });

        registerToReceiver();
        btPutData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotify();
            }
        });
    }

    private void sendNotify() {
        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.custom_notification);
        setViewNotification(notificationView);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bell)
                .setCustomContentView(notificationView);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(ID_CHANNEL, builder.build());
    }

    private PendingIntent getPendingIntent(Context context, int action) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(KEY_INTENT_TO_BROADCAST, action);
        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void setViewNotification(RemoteViews notificationView) {
        String message = "a = " + etNumberA.getText().toString() + " ,b = " + etNumberB.getText().toString();
        String title = getString(R.string.title_notify);
        notificationView.setTextViewText(R.id.tvTitle, title);
        notificationView.setTextViewText(R.id.tvMessage, message);
        notificationView.setOnClickPendingIntent(R.id.btCong, getPendingIntent(this, ACTION_ADD));
        notificationView.setOnClickPendingIntent(R.id.btTru, getPendingIntent(this, ACTION_SUBTRACT));
        notificationView.setOnClickPendingIntent(R.id.btNhan, getPendingIntent(this, ACTION_MULTIPLE));
        notificationView.setOnClickPendingIntent(R.id.btChia, getPendingIntent(this, ACTION_DIVIDE));
    }

    private void showResult(String calculate, int result) {
        String resultShow = etNumberA.getText().toString() + " " + calculate + " " + etNumberB.getText().toString() + " = " + result;
        tvShowResult.setText(resultShow);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadCast);
    }

    private void InitUI() {
        switchWifi = findViewById(R.id.sw);
        etNumberA = findViewById(R.id.etNhap_a);
        etNumberB = findViewById(R.id.etNhapb);
        tvShowResult = findViewById(R.id.tvShowResult);
        btPutData = findViewById(R.id.btPutData);
    }

    private void registerToReceiver() {
        IntentFilter networkFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myBroadCast, networkFilter);
        IntentFilter calculationFilter = new IntentFilter(ACTION);
        registerReceiver(myBroadCast, calculationFilter);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void getNumber() {
        numberA = Integer.parseInt(etNumberA.getText().toString());
        numberB = Integer.parseInt(etNumberB.getText().toString());
    }
}