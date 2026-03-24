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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_PARADIS     = "paradis";
    private static final String EXTRA_POZITIE     = "pozitie";
    private static final int    MOD_ADAUGA        = -1;

    private TextView tvPlaceholder;
    private ListView listViewParadisuri;
    private Button   btnAdauga;

    private ArrayList<Paradis> listaParadisuri = new ArrayList<>();
    private ParadisAdapter adapter;

    // Retine pozitia elementului care se editeaza (-1 = adaugare noua)
    private int pozitieEditare = MOD_ADAUGA;

    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    this::handleResult
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPlaceholder      = findViewById(R.id.tvPlaceholder);
        listViewParadisuri = findViewById(R.id.listViewParadisuri);
        btnAdauga          = findViewById(R.id.btnAdauga);

        // Adapter personalizat in loc de ArrayAdapter<Paradis> generic
        adapter = new ParadisAdapter(this, listaParadisuri);
        listViewParadisuri.setAdapter(adapter);

        // Click simplu -> deschide activitatea de editare cu datele obiectului selectat
        listViewParadisuri.setOnItemClickListener((parent, view, position, id) -> {
            pozitieEditare = position;
            Paradis selectat = listaParadisuri.get(position);

            Intent intent = new Intent(MainActivity.this, AdaugaParadisActivity.class);
            intent.putExtra(EXTRA_PARADIS, selectat);      // trimitem obiectul via Parcelable
            intent.putExtra(EXTRA_POZITIE, position);
            launcher.launch(intent);
        });

        // Long click -> stergere
        listViewParadisuri.setOnItemLongClickListener((parent, view, position, id) -> {
            Paradis sters = listaParadisuri.get(position);
            listaParadisuri.remove(position);
            adapter.notifyDataSetChanged();

            Toast.makeText(this,
                    "\"" + sters.getDenumire() + "\" a fost sters.",
                    Toast.LENGTH_SHORT).show();

            if (listaParadisuri.isEmpty()) {
                listViewParadisuri.setVisibility(View.GONE);
                tvPlaceholder.setVisibility(View.VISIBLE);
            }
            return true;
        });

        // Buton adauga -> mod adaugare (fara obiect preexistent)
        btnAdauga.setOnClickListener(v -> {
            pozitieEditare = MOD_ADAUGA;
            Intent intent = new Intent(MainActivity.this, AdaugaParadisActivity.class);
            // Nu trimitem niciun EXTRA_PARADIS -> activitatea stie ca e adaugare noua
            launcher.launch(intent);
        });
    }

    private void handleResult(ActivityResult result) {
        if (result.getResultCode() != Activity.RESULT_OK || result.getData() == null) return;

        Paradis paradis = result.getData().getParcelableExtra(EXTRA_PARADIS);
        if (paradis == null) return;

        if (pozitieEditare == MOD_ADAUGA) {
            // Adaugare noua
            listaParadisuri.add(paradis);
        } else {
            // Modificare obiect existent la pozitia salvata
            listaParadisuri.set(pozitieEditare, paradis);
            pozitieEditare = MOD_ADAUGA; // reset
        }

        adapter.notifyDataSetChanged();
        tvPlaceholder.setVisibility(View.GONE);
        listViewParadisuri.setVisibility(View.VISIBLE);
    }
}