package leeshani.com.broadcastreceivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
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
    private EditText etNhap_a;
    private EditText etNhap_b;
    private TextView tvShowResult;
    private Button btPutData;
    private static final int ACTION_CONG = 3;
    private static final int ACTION_TRU = 5;
    private static final int ACTION_NHAN = 7;
    private static final int ACTION_CHIA = 9;
    private static final String KEY_INTENT_TO_BROADCAST = "action_calculation";
    private static final String KEY_CHECK_WIFI = "networkInfo";
    private static final String ACTION = "cong_tru_nhan_chia";
    private static final int ID_CHANNEL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitUI();

        IntentFilter networkFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceivers, networkFilter);
        btPutData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotify();
            }
        });
        IntentFilter calculationFilter = new IntentFilter(ACTION);

        registerReceiver(calculationReceiver, calculationFilter);

    }

    private final BroadcastReceiver networkReceivers = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            NetworkInfo networkInfo = extras.getParcelable(KEY_CHECK_WIFI);
            if (networkInfo != null) {
                NetworkInfo.State state = networkInfo.getState();
                switchWifi.setChecked(state == NetworkInfo.State.CONNECTED);
            }
        }
    };

    private final BroadcastReceiver calculationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra(KEY_INTENT_TO_BROADCAST, 0) != 0) {
                if (ACTION.equals(intent.getAction())) {
                    int phepTinh = intent.getIntExtra(KEY_INTENT_TO_BROADCAST, 0);
                    int a = Integer.parseInt(etNhap_a.getText().toString());
                    int b = Integer.parseInt(etNhap_b.getText().toString());
                    switch (phepTinh) {
                        case ACTION_CONG:
                            showResult("+", a + b);
                            break;
                        case ACTION_TRU:
                            showResult("-", a - b);
                            break;
                        case ACTION_NHAN:
                            showResult("*", a * b);
                            break;
                        case ACTION_CHIA:
                            if (b == 0) {
                                Toast.makeText(context, R.string.enter_khac_0, Toast.LENGTH_SHORT).show();
                            } else {
                                showResult("/", a / b);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }

        }
    };

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
        String message = "a = " + etNhap_a.getText().toString() + " ,b = " + etNhap_b.getText().toString();
        String title = getString(R.string.title_notify);
        notificationView.setTextViewText(R.id.tvTitle, title);
        notificationView.setTextViewText(R.id.tvMessage, message);
        notificationView.setOnClickPendingIntent(R.id.btCong, getPendingIntent(this, ACTION_CONG));
        notificationView.setOnClickPendingIntent(R.id.btTru, getPendingIntent(this, ACTION_TRU));
        notificationView.setOnClickPendingIntent(R.id.btNhan, getPendingIntent(this, ACTION_NHAN));
        notificationView.setOnClickPendingIntent(R.id.btChia, getPendingIntent(this, ACTION_CHIA));
    }

    private void showResult(String phepTinh, int result) {
        String resultShow = etNhap_a.getText().toString() + " " + phepTinh + " " + etNhap_b.getText().toString() + " = " + result;
        tvShowResult.setText(resultShow);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkReceivers);
        unregisterReceiver(calculationReceiver);
    }

    private void InitUI() {
        switchWifi = findViewById(R.id.sw);
        etNhap_a = findViewById(R.id.etNhap_a);
        etNhap_b = findViewById(R.id.etNhapb);
        tvShowResult = findViewById(R.id.tvShowResult);
        btPutData = findViewById(R.id.btPutData);
    }

}