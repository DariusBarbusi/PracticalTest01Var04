package ro.pub.cs.systems.eim.practicaltest01var04;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class PracticalTest01Var04Service extends Service {

    private final int interval = 5000; // 5 secunde
    private Handler handler = new Handler();
    private String name; // Declara variabila pentru nume
    private String group; // Declara variabila pentru grup

    private Runnable sendMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            // Trimite broadcast cu numele
            Intent intentName = new Intent();
            intentName.setAction("ro.pub.cs.systems.eim.practicaltest01var04.NAME_ACTION");
            intentName.putExtra("name", name); // Folosește variabila name
            sendBroadcast(intentName);

            // Trimite broadcast cu grupa, după o pauză
            handler.postDelayed(() -> {
                Intent intentGroup = new Intent();
                intentGroup.setAction("ro.pub.cs.systems.eim.practicaltest01var04.GROUP_ACTION");
                intentGroup.putExtra("group", group); // Folosește variabila group
                sendBroadcast(intentGroup);
            }, interval / 2);

            // Reprogramare la fiecare 5 secunde
            handler.postDelayed(this, interval);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Inițializează variabilele name și group cu valorile primite prin Intent
        name = intent.getStringExtra("name");
        group = intent.getStringExtra("group");
        handler.post(sendMessagesRunnable);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(sendMessagesRunnable);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
