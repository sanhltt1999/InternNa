package leeshani.com.downloadfile;

import static leeshani.com.downloadfile.MainActivity.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadService extends android.app.Service {
    private static final String KEY_INTENT = "process";
    private static final String ACTION = "send_download";
    private static final String FILE_COPY = "eng_dictionary.db";
    private static final String FOLDER = "copyFile";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        copyAsset();
        return START_NOT_STICKY;
    }

    private void copyAsset() {

        String dstPath = Environment.getExternalStorageDirectory() +
                File.separator + FOLDER;
        File dst = new File(dstPath);

        if (!dst.exists()) {
            if (!dst.mkdir()) {
                return;
            }
        }

        AssetManager assetManager = getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(FILE_COPY);
            long size = assetManager.open(FILE_COPY).available();
            File outFile = new File(dstPath,FILE_COPY);
            out = new FileOutputStream(outFile);
            copyFile(in, out, size);
            sendNotification();
            Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(getString(R.string.error),e.toString());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out, long size) throws IOException {
        long total = 0;
        int process;
        byte[] buff = new byte[1024];
        int read;
        while ((read = in.read(buff)) > 0) {
            out.write(buff, 0, read);
            total += read;
            process = (int) (total * 100 / size);
            sendActionActivity(process);
        }
    }

    private void sendNotification() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.download_file))
                .setSmallIcon(R.drawable.ic_bell)
                .setContentText(getString(R.string.download_success))
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }
    }

    private void sendActionActivity(int process) {
        Intent intent = new Intent(ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_INTENT, process);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
