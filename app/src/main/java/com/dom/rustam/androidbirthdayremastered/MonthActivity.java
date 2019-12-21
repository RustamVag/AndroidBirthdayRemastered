package com.dom.rustam.androidbirthdayremastered;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MonthActivity extends AppCompatActivity {

    // Константы
    public static final int COMMAND_NEW = 1;
    public static final int COMMAND_EDIT = 2;
    public  static final int ACTIVITY_BIRTHDAY = 5;

    private static final List<BirthdayItem> birthdays = new ArrayList<BirthdayItem>();
    private static final List<Birthday> birth = new ArrayList<Birthday>();

    private int selectedItem;
    DBHelper dbHelper;
    SQLiteDatabase database;
    ArrayAdapter<BirthdayItem> adapter;
    ListView lv;
    int pos;

    AlertDialog.Builder deleteDialog;
    Context context;

    // --  Получили ответ от вызванных активити --
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Обновляем список
        if (requestCode == ACTIVITY_BIRTHDAY)
        {
            // Определяем месяц
            pos = getIntent().getExtras().getInt("monthNumber");

            Month month = new Month(pos+1);
            //String[] list = new String[month.getCount()];

            TextView tv = (TextView) findViewById(R.id.textView2);
            tv.setText(month.getName());

            Integer d,m,y;
            m = month.getNumber();
            // Чтение из БД
            dbHelper = new DBHelper(this);
            database = dbHelper.getWritableDatabase();
            Cursor cursor = database.query(DBHelper.TABLE_BIRTHDAYS, null, null, null, null, null, null);

            birth.clear();
            if (cursor.moveToFirst()) {
                // int flag = 0;
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                int dayIndex = cursor.getColumnIndex(DBHelper.KEY_DAY);
                int monthIndex = cursor.getColumnIndex(DBHelper.KEY_MONTH);
                int latIndex = cursor.getColumnIndex(DBHelper.KEY_LAT);
                int longIndex = cursor.getColumnIndex(DBHelper.KEY_LONG);

                do {
                    Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                            ", name = " + cursor.getString(nameIndex) +
                            ", day = " + cursor.getInt(dayIndex) +
                            ", month = " + cursor.getInt(monthIndex));
                    birth.add( new Birthday(cursor.getInt(idIndex), cursor.getString(nameIndex), cursor.getInt(dayIndex), cursor.getInt(monthIndex), cursor.getDouble(latIndex), cursor.getDouble(longIndex)) );
                    //flag++;
                } while (cursor.moveToNext());
            } else {
                Log.d("mLog", "0 rows");
                tv.setText("Список дней рождения пуст");

            }

            cursor.close();

            birthdays.clear();
            // Составление списка
            int flag = 0;
            for (Integer i = 1; i <= month.getCount(); i++)
            {
                //list[i-1] = i.toString() + "." + m.toString() + ".2016";

                BirthdayItem bi = new BirthdayItem(" ",i.toString() + "." + m.toString() + ".2016", i, m);
                for(Birthday b: birth)
                {
                    if ((b.getDay() == i) && (b.getMonth() == m))
                    {
                        bi.name = b.getName();
                        bi.state = 1;
                        bi.id = b.getId();
                        bi.latitude = b.getLatitude();
                        bi.longitude = b.getLongitude();
                        birthdays.add(bi);
                        flag++;
                    }
                }
            }
            if (flag < 1) tv.setText("Список дней рождения пуст");

            lv= (ListView) findViewById(R.id.listView2);

            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
            adapter = new BirthdayAdapter(this);
            lv.setAdapter(adapter);
        }
    }

    // ---- Инициализация активити -----
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        context = MonthActivity.this;

        // Определяем месяц
        pos = getIntent().getExtras().getInt("monthNumber");

        Month month = new Month(pos+1);
        //String[] list = new String[month.getCount()];

        TextView tv = (TextView) findViewById(R.id.textView2);
        tv.setText(month.getName());

        Integer d,m,y;
        m = month.getNumber();

        // Чтение из БД
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_BIRTHDAYS, null, null, null, null, null, null);

        birth.clear();
        if (cursor.moveToFirst()) {
            // int flag = 0;
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int dayIndex = cursor.getColumnIndex(DBHelper.KEY_DAY);
            int monthIndex = cursor.getColumnIndex(DBHelper.KEY_MONTH);
            int latIndex = cursor.getColumnIndex(DBHelper.KEY_LAT);
            int longIndex = cursor.getColumnIndex(DBHelper.KEY_LONG);

            do {
                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        ", name = " + cursor.getString(nameIndex) +
                        ", day = " + cursor.getInt(dayIndex) +
                        ", month = " + cursor.getInt(monthIndex));
                birth.add( new Birthday(cursor.getInt(idIndex), cursor.getString(nameIndex), cursor.getInt(dayIndex), cursor.getInt(monthIndex), cursor.getDouble(latIndex), cursor.getDouble(longIndex)) );
                //flag++;
            } while (cursor.moveToNext());
        } else {
            Log.d("mLog", "0 rows");
            tv.setText("Список дней рождения пуст");

        }

        cursor.close();

        birthdays.clear();
        // Составление списка
        int flag = 0;
        for (Integer i = 1; i <= month.getCount(); i++)
        {
            //list[i-1] = i.toString() + "." + m.toString() + ".2016";

            BirthdayItem bi = new BirthdayItem(" ",i.toString() + "." + m.toString() + ".2016", i, m);
            for(Birthday b: birth)
            {
                if ((b.getDay() == i) && (b.getMonth() == m))
                {
                    bi.name = b.getName();
                    bi.state = 1;
                    bi.id = b.getId();
                    bi.latitude = b.getLatitude();
                    bi.longitude = b.getLongitude();
                    birthdays.add(bi);
                    flag++;
                }
            }
        }
        if (flag < 1) tv.setText("Список дней рождения пуст");

        lv= (ListView) findViewById(R.id.listView2);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        adapter = new BirthdayAdapter(this);
        lv.setAdapter(adapter);

        // Обработчик ListView
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = position;
                showPopupMenu(view, selectedItem);
            }
        });

        // Настройка диалога удаления
        deleteDialog = new AlertDialog.Builder(context);
        deleteDialog.setTitle("Удаление дня рождения");  // заголовок
        deleteDialog.setMessage("Вы действительно хотите удалить запись?"); // сообщение
        deleteDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(context, "Запись удалена",
                        Toast.LENGTH_LONG).show();

                BirthdayItem bi = birthdays.get(selectedItem);
                Log.d("Отладка 3:","date = " + bi.date.toString() + ", name = " + bi.name);
                String deleteText = DBHelper.KEY_ID + "=" + bi.id;
                database.delete(DBHelper.TABLE_BIRTHDAYS, deleteText, null);
                birthdays.remove(selectedItem);
                adapter.notifyDataSetChanged();
                lv.setAdapter(adapter);

            }
        });
        deleteDialog.setNegativeButton("Нет", new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        deleteDialog.setCancelable(true);
        deleteDialog.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(context, "Вы ничего не выбрали",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    // Вызов всплывающего меню и его обработчики
    private void showPopupMenu(View v, final int position) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenu); // Для Android 4.0
        // для версии Android 3.0 нужно использовать длинный вариант
        // popupMenu.getMenuInflater().inflate(R.menu.popupmenu,
        // popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Toast.makeText(PopupMenuDemoActivity.this,
                // item.toString(), Toast.LENGTH_LONG).show();
                // return true;
                switch (item.getItemId()) {

                    case R.id.action_edit:
                        Toast.makeText(getApplicationContext(),
                                "Редактирование",
                                Toast.LENGTH_SHORT).show();

                        BirthdayItem bi = birthdays.get(selectedItem);
                        Log.d("Отладка 1:","date = " + bi.date.toString() + ", name = " + bi.name);
                        Intent intent = new Intent(getApplicationContext(), BirthdayActivity.class);
                        intent.putExtra("birthdayId", bi.id);
                        intent.putExtra("monthNumber", pos);
                        intent.putExtra("dayNumber", bi.day);
                        intent.putExtra("birthdayName", bi.name);
                        intent.putExtra("latitude", bi.latitude);
                        intent.putExtra("longitude", bi.longitude);
                        intent.putExtra("command", COMMAND_EDIT);
                        startActivityForResult(intent, ACTIVITY_BIRTHDAY);
                        return true;
                    case R.id.action_delete:
                        //Toast.makeText(getApplicationContext(),
                        //"Удаление",
                        //Toast.LENGTH_SHORT).show();
                        deleteDialog.show();

                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    // Активити в фокусе
    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        lv.setAdapter(adapter);
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
            Intent intent = new Intent(this, BirthdayActivity.class);
            intent.putExtra("monthNumber", pos);
            startActivityForResult(intent, ACTIVITY_BIRTHDAY);
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

    // Класс BirthdayItem
    private static class BirthdayItem
    {
        public  String name;

        public  String date;

        public Integer state;

        public int day;

        public int month;

        public int id;

        public double latitude;

        public double longitude;

        public BirthdayItem(String name, String date, int day, int month)
        {
            this.name = name;
            this.date = date;
            this.state = 0;
            this.id = 0;
            this.day = day;
            this.month = month;
            this.latitude = BirthdayActivity.LOCATION_NULL;
            this.longitude = BirthdayActivity.LOCATION_NULL;

        }
    }

    // Создаем адаптер
    private class BirthdayAdapter extends ArrayAdapter<BirthdayItem>
    {
        public BirthdayAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2, birthdays);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BirthdayItem birthdayItem = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.custom_list_item, null);
            }

            RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.view_background);

            TextView tvName = (TextView) convertView.findViewById(R.id.list_item_name);
            TextView tvDate = (TextView) convertView.findViewById(R.id.list_item_date);
            if (birthdayItem.state == 1)
            {
                //tvName.setBackgroundResource(R.color.colorBirthday);
                //layout.setBackgroundColor(getResources().getColor(R.color.colorBirthday));
                tvDate.setTextColor(getResources().getColor(R.color.colorBirthday));
            }
            tvName.setText(birthdayItem.name);
            tvDate.setText(birthdayItem.date);

            //((TextView) convertView.findViewById(R.id.list_item_name)).setText(birthdayItem.name);
            //((TextView) convertView.findViewById(R.id.list_item_date)).setText(birthdayItem.date);
            return convertView;
        }
    }
}
