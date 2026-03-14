package com.example.laborator3;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String tag = "Lab3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Log.v(tag,"onCreate a fost apelat - verbose");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.e(tag, "onStart a fost apelat - eroare");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.w(tag, "onResume a fost apelat - warning");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(tag, "onPause a fost apelat - debug");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i(tag, "onStop a fost apelat - info");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(tag, "onRestart a fost apelat - debug - repornire");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.e(tag, "onDestroy a fost apelat - eroare -  distrus");
    }
}