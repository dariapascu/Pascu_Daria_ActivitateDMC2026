package com.example.lab4;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvPlaceholder, tvDenumire, tvVizitatori,
            tvAccesibil, tvTemperatura, tvTip;
    private Button btnAdauga;

    private final ActivityResultLauncher<Intent> adaugaLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    this::handleResult
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPlaceholder = findViewById(R.id.tvPlaceholder);
        tvDenumire    = findViewById(R.id.tvDenumire);
        tvVizitatori  = findViewById(R.id.tvVizitatori);
        tvAccesibil   = findViewById(R.id.tvAccesibil);
        tvTemperatura = findViewById(R.id.tvTemperatura);
        tvTip         = findViewById(R.id.tvTip);
        btnAdauga     = findViewById(R.id.btnAdauga);

        btnAdauga.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdaugaParadisActivity.class);
            adaugaLauncher.launch(intent);
        });
    }

    private void handleResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            Paradis paradis = result.getData().getParcelableExtra("paradis");
            if (paradis != null) {
                afiseazaParadis(paradis);
            }
        }
    }

    private void afiseazaParadis(Paradis p) {
        tvPlaceholder.setVisibility(View.GONE);

        tvDenumire.setVisibility(View.VISIBLE);
        tvVizitatori.setVisibility(View.VISIBLE);
        tvAccesibil.setVisibility(View.VISIBLE);
        tvTemperatura.setVisibility(View.VISIBLE);
        tvTip.setVisibility(View.VISIBLE);

        tvDenumire.setText("Denumire: " + p.getDenumire());
        tvVizitatori.setText("Vizitatori/an: " + p.getVizitatori());
        tvAccesibil.setText("Accesibil: " + (p.isEsteAccesibil() ? "Da" : "Nu"));
        tvTemperatura.setText("Temperatura medie: " + p.getTemperaturaMetdie() + " °C");
        tvTip.setText("Tip: " + p.getTip().name());
    }
}