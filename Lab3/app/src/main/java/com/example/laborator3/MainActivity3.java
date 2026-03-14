package com.example.laborator3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity3 extends AppCompatActivity {

    public static String tag = "ThirdActivity";
    private int suma = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);

        Log.i(tag, "activitate 3 deschisa");

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String mesajPrimit = bundle.getString("mesaj", "—");
            int numar1         = bundle.getInt("nr1", 0);
            int numar2         = bundle.getInt("nr2", 0);
            suma               = numar1 + numar2;

            Toast.makeText(
                    this,
                    "Mesaj: " + mesajPrimit +
                            " Nr 1: " + numar1 +
                            ", Nr 2: " + numar2,
                    Toast.LENGTH_LONG
            ).show();
        }

        Button btnTrimiteInapoi = findViewById(R.id.btnTrimiteInapoi);
        btnTrimiteInapoi.setOnClickListener(v -> {

            Intent rezultat = new Intent();
            rezultat.putExtra("mesaj_inapoi", "Activity3:");
            rezultat.putExtra("suma", suma);

            setResult(RESULT_OK, rezultat);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}