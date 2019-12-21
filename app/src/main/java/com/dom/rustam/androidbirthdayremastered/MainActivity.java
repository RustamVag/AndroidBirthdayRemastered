package com.dom.rustam.androidbirthdayremastered;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    String[] firstList = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, firstList);
        lv.setAdapter(adapter);


        // Обработчик ListView
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goMonthView(position);
            }
        });

        lv.setItemChecked(0, true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_add)
        {
            Intent intent = new Intent(this, BirthdayActivity.class); // добавление дня рождения
            intent.putExtra("monthNumber", 0);
            startActivity(intent);
        }

        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_about)
        {
            Intent intent = new Intent(this,AboutActivity.class);
            startActivity(intent);
        }

        return true;
    }

    // Запуск активити месяца
    public void goMonthView(int position) {
        Intent intent = new Intent(this, MonthActivity.class);
        intent.putExtra("monthNumber", position);
        startActivity(intent);
    }
}

