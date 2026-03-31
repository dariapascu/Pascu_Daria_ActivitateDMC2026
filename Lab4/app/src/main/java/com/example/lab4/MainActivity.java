package com.example.lab4;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_PARADIS     = "paradis";
    private static final String EXTRA_POZITIE     = "pozitie";
    private static final int    MOD_ADAUGA        = -1;

    private TextView tvPlaceholder;
    private ListView listViewParadisuri;
    private Button   btnAdauga;

    private ArrayList<Paradis> listaParadisuri = new ArrayList<>();
    private ParadisAdapter adapter;

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

        adapter = new ParadisAdapter(this, listaParadisuri);
        listViewParadisuri.setAdapter(adapter);

        incarcaParadisuri();

        if (!listaParadisuri.isEmpty()) {
            tvPlaceholder.setVisibility(View.GONE);
            listViewParadisuri.setVisibility(View.VISIBLE);
        }

        listViewParadisuri.setOnItemClickListener((parent, view, position, id) -> {
            pozitieEditare = position;
            Paradis selectat = listaParadisuri.get(position);

            Intent intent = new Intent(MainActivity.this, AdaugaParadisActivity.class);
            intent.putExtra(EXTRA_PARADIS, selectat);
            intent.putExtra(EXTRA_POZITIE, position);
            launcher.launch(intent);
        });

        listViewParadisuri.setOnItemLongClickListener((parent, view, position, id) -> {
            Paradis selectat = listaParadisuri.get(position);
            salveazaFavorit(selectat);

            Toast.makeText(this,
                    "\"" + selectat.getDenumire() + "\" salvat la favorite!",
                    Toast.LENGTH_SHORT).show();
            return true;
        });

        btnAdauga.setOnClickListener(v -> {
            pozitieEditare = MOD_ADAUGA;
            Intent intent = new Intent(MainActivity.this, AdaugaParadisActivity.class);
            launcher.launch(intent);
        });

        Button btnSetari = findViewById(R.id.btnSetari);
        btnSetari.setOnClickListener(v -> {
            startActivity(new Intent(this, SetariActivity.class));
        });
    }

    private void incarcaParadisuri() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try (FileInputStream fis = openFileInput("paradisuri.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                if (linie.trim().isEmpty()) continue;
                String[] parts = linie.split(" \\| ");
                if (parts.length < 6) continue;
                String denumire   = parts[0];
                TipParadis tip    = TipParadis.valueOf(parts[1]);
                int vizitatori    = Integer.parseInt(parts[2].replace(" vizitatori", ""));
                double temp       = Double.parseDouble(parts[3].replace(" C", ""));
                boolean accesibil = parts[4].equals("Accesibil");
                Date data         = null;
                try { data = sdf.parse(parts[5].replace("Sejur: ", "")); } catch (ParseException ignored) {}
                listaParadisuri.add(new Paradis(denumire, vizitatori, accesibil, temp, tip, data));
            }
            adapter.notifyDataSetChanged();
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salveazaFavorit(Paradis p) {
        try (FileOutputStream fos = openFileOutput("favorite.txt", Context.MODE_APPEND);
             OutputStreamWriter osw = new OutputStreamWriter(fos)) {
            osw.write(p.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleResult(ActivityResult result) {
        if (result.getResultCode() != Activity.RESULT_OK || result.getData() == null) return;

        Paradis paradis = result.getData().getParcelableExtra(EXTRA_PARADIS);
        if (paradis == null) return;

        if (pozitieEditare == MOD_ADAUGA) {
            listaParadisuri.add(paradis);
        } else {
            listaParadisuri.set(pozitieEditare, paradis);
            pozitieEditare = MOD_ADAUGA;
        }

        adapter.notifyDataSetChanged();
        tvPlaceholder.setVisibility(View.GONE);
        listViewParadisuri.setVisibility(View.VISIBLE);
    }
}