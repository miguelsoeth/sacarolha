package com.example.sacarolha;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base); // Set the base layout

        // Get the frame layout where child content will be placed
        FrameLayout contentFrame = findViewById(R.id.content_frame);

        // Inflate and add the child content layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View childLayout = inflater.inflate(getLayoutResourceId(), contentFrame, false);
        contentFrame.addView(childLayout);
    }

    // Abstract method to be overridden by child activities
    protected abstract int getLayoutResourceId();

}
