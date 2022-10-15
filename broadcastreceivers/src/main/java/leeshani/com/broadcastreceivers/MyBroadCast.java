package leeshani.com.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class MyBroadCast extends BroadcastReceiver {
    View view;
    OnListener listener;
    public MyBroadCast() {
    }
    public void setOnListener (OnListener onListener){
        this.listener = onListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
         listener.receiver(intent);
    }
    public interface OnListener{
        void receiver(Intent intent);
    }
}
