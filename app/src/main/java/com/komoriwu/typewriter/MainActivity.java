package com.komoriwu.typewriter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private TypeWriteTextView mTypeWriteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTypeWriteTextView = (TypeWriteTextView) findViewById(R.id.tv);
    }
}
