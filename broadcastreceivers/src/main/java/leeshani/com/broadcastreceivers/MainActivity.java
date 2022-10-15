package leeshani.com.broadcastreceivers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    private static final String KEY_CHECK_WIFI = "networkInfo";
    private static final String ACTION = "calculation";
    private static final int ID_CHANNEL = 1;
    MyBroadCast myBroadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitUI();

        myBroadCast = new MyBroadCast();

        myBroadCast.setOnListener(new MyBroadCast.OnListener() {
            @Override
            public void receiver(Intent intent) {
                switch (intent.getAction()) {
                    case ConnectivityManager.CONNECTIVITY_ACTION:
                        Bundle extras = intent.getExtras();
                        NetworkInfo networkInfo = extras.getParcelable(KEY_CHECK_WIFI);
                        if (networkInfo != null) {
                            NetworkInfo.State state = networkInfo.getState();
                            switchWifi.setChecked(state == NetworkInfo.State.CONNECTED);
                        }
                        break;
                    case ACTION:
                        int calculate = intent.getIntExtra(KEY_INTENT_TO_BROADCAST, 0);
                        int a = Integer.parseInt(etNumberA.getText().toString());
                        int b = Integer.parseInt(etNumberB.getText().toString());
                        switch (calculate) {
                            case ACTION_ADD:
                                showResult(getString(R.string.add), a + b);
                                break;
                            case ACTION_SUBTRACT:
                                showResult(getString(R.string.subtract), a - b);
                                break;
                            case ACTION_MULTIPLE:
                                showResult(getString(R.string.multiple), a * b);
                                break;
                            case ACTION_DIVIDE:
                                if (b == 0) {
                                    Toast.makeText(MainActivity.this, R.string.enter_khac_0, Toast.LENGTH_SHORT).show();
                                } else {
                                    showResult(getString(R.string.divide), a / b);
                                }
                                break;
                            default:
                                break;
                        }
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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MyApp.CHANNEL_ID)
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
}