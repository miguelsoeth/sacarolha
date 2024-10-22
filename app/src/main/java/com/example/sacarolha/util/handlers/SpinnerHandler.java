package com.example.sacarolha.util.handlers;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sacarolha.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpinnerHandler {

    public interface SpinnerItemListener {
        void onSpinnerItemSelected(int pos);
    }

    public <E extends Enum<E>> void configureSpinnerWithEnum(Spinner spinner, Class<E> enumClass, Context context) {

        List<E> enumList = new ArrayList<>(Arrays.asList(enumClass.getEnumConstants()));
        ArrayAdapter<E> adapter = new ArrayAdapter<>(context, R.layout.spinner_item_rounded, enumList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    ((TextView) view).setTextColor(context.getResources().getColor(R.color.transparent_white));
                } else {
                    ((TextView) view).setTextColor(context.getResources().getColor(R.color.white));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setSelection(0);
    }

    public <E extends Enum<E>> void configureSpinnerWithEnum_light(Spinner spinner, Class<E> enumClass, Context context) {

        List<E> enumList = new ArrayList<>(Arrays.asList(enumClass.getEnumConstants()));
        ArrayAdapter<E> adapter = new ArrayAdapter<>(context, R.layout.spinner_item_rounded_light, enumList);
        adapter.setDropDownViewResource(R.layout.spinner_item_light);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    ((TextView) view).setTextColor(context.getResources().getColor(R.color.light_gray));
                } else {
                    ((TextView) view).setTextColor(context.getResources().getColor(R.color.dark_purple));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public static <E extends Enum<E>> void configureSpinnerWithEnum_basic(Spinner spinner, Class<E> enumClass, Context context, SpinnerItemListener listener) {

        List<E> enumList = new ArrayList<>(Arrays.asList(enumClass.getEnumConstants()));

        ArrayAdapter<E> adapter = new ArrayAdapter<>(context, R.layout.spinner_basic, enumList);
        adapter.setDropDownViewResource(R.layout.spinner_basic_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onSpinnerItemSelected(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
