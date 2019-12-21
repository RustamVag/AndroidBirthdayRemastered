package com.dom.rustam.androidbirthdayremastered;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // определяем текущую дату
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        //int month = c.get(Calendar.MONTH);
        int month = getActivity().getIntent().getExtras().getInt("monthNumber");
        int day = c.get(Calendar.DAY_OF_MONTH);

        // создаем DatePickerDialog и возвращаем его
        Dialog picker = new DatePickerDialog(getActivity(), this,
                year, month, day);
        picker.setTitle("Выберите дату");

        return picker;
    }

    @Override
    public void onStart() {
        super.onStart();
        // добавляем кастомный текст для кнопки
        Button nButton =  ((AlertDialog) getDialog())
                .getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText("Готово");

    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        EditText dateEdit = (EditText) getActivity().findViewById(R.id.editBdDate);
        dateEdit.setText(day + "-" + (month+1));
    }
}