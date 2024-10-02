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

}
