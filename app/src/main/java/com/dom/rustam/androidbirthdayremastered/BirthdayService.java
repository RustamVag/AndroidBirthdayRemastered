package com.dom.rustam.androidbirthdayremastered;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BirthdayService extends Service {

    // Звук
    MediaPlayer mediaPlayer;


    // Таймеры
    private Integer state = 0;
    private Timer mTimer;
    private MyTimerTask tt;

    DBHelper dbHelper;
    NotificationManager nm;
    private final int NOTIFICATION_ID = 252;

    // Конструктор
    public BirthdayService()
    {
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Служба остановлена",
                Toast.LENGTH_SHORT).show();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Служба запущена",
                Toast.LENGTH_SHORT).show();
        nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Инициализация таймера

        if (mTimer != null) {
            mTimer.cancel();
        }

        mTimer = new Timer();

        tt = new MyTimerTask(this);
        mTimer.schedule(tt, 10000, 1000*60*30);

        // Подготовка плеера
        mediaPlayer = MediaPlayer.create(this, R.raw.notification);
        mediaPlayer.setLooping(false);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onCreate() {
        this.state = 1;
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    // Класс Таймер таск
    private class MyTimerTask extends TimerTask {
        private Context context;
        public MyTimerTask(Context context) {
            this.context = context;
        }

        // Обработка таймера, второй поток
        @Override
        public void run() {

            // Чтение из БД
            dbHelper = new DBHelper(getApplicationContext());
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            Cursor cursor = database.query(DBHelper.TABLE_BIRTHDAYS, null, null, null, null, null, null);
            int day;
            int month;

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            day = c.get(Calendar.DAY_OF_MONTH);
            month = c.get(Calendar.MONTH) + 1;
            Log.d("Current time", "month = " + month + "   day = " + day);

            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                int dayIndex = cursor.getColumnIndex(DBHelper.KEY_DAY);
                int monthIndex = cursor.getColumnIndex(DBHelper.KEY_MONTH);
                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        ", name = " + cursor.getString(nameIndex) +
                        ", day = " + cursor.getInt(dayIndex) +
                        ", month = " + cursor.getInt(monthIndex));
                do {
                    int dayDB = cursor.getInt(dayIndex);
                    int monthDB = cursor.getInt(monthIndex);
                    String nameDB = cursor.getString(nameIndex);

                    if ((monthDB == month) && (dayDB == day))
                    {
                        Birthday birthday = new Birthday(nameDB, dayDB, monthDB);
                        showNotification("Сегодня " + birthday.getName() + " отмечает день рождения!");
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                    }

                } while (cursor.moveToNext());
            } else
                Log.d("mLog","0 rows");
            cursor.close();

            //mStreamID = playSound(mNotifySound);

        }
    }

    // UI Поток
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), "Дни рождения: новое уведомление", Toast.LENGTH_SHORT).show();
            // Механизм хранения настроек
            String APP_PREFERENCES = "mysettings";
            String APP_PREFERENCES_NOTIFY = "notify";
            String APP_PREFERENCES_SOUND = "sound";
            SharedPreferences mSettings;
            mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

            if (mSettings.contains(APP_PREFERENCES_NOTIFY)) {
                // Получаем число из настроек
                Boolean bSound = mSettings.getBoolean(APP_PREFERENCES_SOUND, false);

                if (bSound) {
                    mediaPlayer.start();
                }
            }
        }
    };


    // Уведомление
    public void showNotification(String msg)
    {
        Notification.Builder builder = new Notification.Builder(getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        builder
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_louncher)
                .setLargeIcon(BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.star_icon))
                .setTicker("Сегодня день рождения у...")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("День рождения")
                .setContentText(msg);
        Notification notification = builder.getNotification();

        nm.notify(NOTIFICATION_ID, notification);
    }

}


