package com.dom.rustam.androidbirthdayremastered;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {


    private Switch notifySwitch;
    private Switch soundSwitch;
    private Button button;

    // Механизм хранения настроек
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NOTIFY = "notify";
    public static final String APP_PREFERENCES_SOUND = "sound";
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        notifySwitch = (Switch) findViewById(R.id.notify_switch);
        soundSwitch = (Switch) findViewById(R.id.sound_switch);
        button = (Button) findViewById(R.id.settings_button);

        // Обработчик кнопки
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean(APP_PREFERENCES_NOTIFY, notifySwitch.isChecked());
                editor.putBoolean(APP_PREFERENCES_SOUND, soundSwitch.isChecked());
                editor.apply();
                powerService(notifySwitch.isChecked());
            }

        });
    }

    // Чтение/сохранение настроек в фвйле
    @Override
    protected void onResume() {
        super.onResume();

        if (mSettings.contains(APP_PREFERENCES_NOTIFY)) {
            // Получаем число из настроек
            Boolean bNotify = mSettings.getBoolean(APP_PREFERENCES_NOTIFY, false);
            Boolean bSound = mSettings.getBoolean(APP_PREFERENCES_SOUND, false);
            notifySwitch.setChecked(bNotify);
            soundSwitch.setChecked(bSound);
            powerService(bNotify);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_NOTIFY, notifySwitch.isChecked());
        editor.putBoolean(APP_PREFERENCES_SOUND, soundSwitch.isChecked());
        editor.apply();
        powerService(notifySwitch.isChecked());
    }

    public void powerService(Boolean state)
    {
        if (state)
        {
            startService(new Intent(SettingsActivity.this, BirthdayService.class));
        }
        else
        {
            stopService(new Intent(SettingsActivity.this, BirthdayService.class));
        }
    }



}
