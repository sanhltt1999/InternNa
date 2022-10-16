package leeshani.com.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class MyBroadCast extends BroadcastReceiver {
    OnListener listener;
    private static final int ACTION_ADD = 3;
    private static final int ACTION_SUBTRACT = 5;
    private static final int ACTION_MULTIPLE = 7;
    private static final int ACTION_DIVIDE = 9;
    private static final String ACTION = "calculation";
    private static final String KEY_INTENT_TO_BROADCAST = "action_calculation";
    private static final String KEY_CHECK_WIFI = "networkInfo";

    public MyBroadCast() {
    }

    public void setOnListener(OnListener onListener) {
        this.listener = onListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ConnectivityManager.CONNECTIVITY_ACTION:
                Bundle extras = intent.getExtras();
                NetworkInfo networkInfo = extras.getParcelable(KEY_CHECK_WIFI);
                if (networkInfo != null) {
                    NetworkInfo.State state = networkInfo.getState();
                    listener.changedStatus(state == NetworkInfo.State.CONNECTED);
                }

            case ACTION:
                int calculate = intent.getIntExtra(KEY_INTENT_TO_BROADCAST, 0);
                switch (calculate) {
                    case ACTION_ADD:
                        listener.add();
                        break;
                    case ACTION_SUBTRACT:
                        listener.subtract();
                        break;
                    case ACTION_MULTIPLE:
                        listener.multiple();
                        break;
                    case ACTION_DIVIDE:
                        listener.divide();
                        break;
                    default:
                        break;
                }
        }
    }

    public interface OnListener {
        void changedStatus(boolean status);

        void add();

        void subtract();

        void multiple();

        void divide();
    }
}
