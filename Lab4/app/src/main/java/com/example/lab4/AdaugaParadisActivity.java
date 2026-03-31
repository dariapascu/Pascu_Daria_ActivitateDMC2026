package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;

public class AdaugaParadisActivity extends AppCompatActivity {

    private EditText     etDenumire, etVizitatori, etTemperatura;
    private CheckBox     cbAccesibil;
    private RadioGroup   rgRating;
    private RadioButton  rb1, rb2, rb3;
    private Spinner      spinnerTip;
    private RatingBar    ratingBar;
    private Switch       switchRecomandat;
    private ToggleButton toggleAccesibil;
    private Button       btnSalveaza;
    private TextView     tvRatingValoare;
    private DatePicker   datePicker;
    private TextView     tvTitlu;

    private Paradis paradisDeEditat = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaugaparadisactivity);

        etDenumire       = findViewById(R.id.etDenumire);
        etVizitatori     = findViewById(R.id.etVizitatori);
        etTemperatura    = findViewById(R.id.etTemperatura);
        cbAccesibil      = findViewById(R.id.cbAccesibil);
        rgRating         = findViewById(R.id.rgRating);
        rb1              = findViewById(R.id.rb1);
        rb2              = findViewById(R.id.rb2);
        rb3              = findViewById(R.id.rb3);
        spinnerTip       = findViewById(R.id.spinnerTip);
        ratingBar        = findViewById(R.id.ratingBar);
        switchRecomandat = findViewById(R.id.switchRecomandat);
        toggleAccesibil  = findViewById(R.id.toggleAccesibil);
        btnSalveaza      = findViewById(R.id.btnSalveaza);
        tvRatingValoare  = findViewById(R.id.tvRatingValoare);
        datePicker       = findViewById(R.id.datePicker);
        tvTitlu          = findViewById(R.id.tvTitlu);

        aplicaSetariText();

        TipParadis[] tipuri = TipParadis.values();
        String[] tipuriStr = new String[tipuri.length];
        for (int i = 0; i < tipuri.length; i++) tipuriStr[i] = tipuri[i].name();

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, tipuriStr);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTip.setAdapter(adapterSpinner);

        cbAccesibil.setOnCheckedChangeListener((btn, isChecked) ->
                toggleAccesibil.setChecked(isChecked));
        toggleAccesibil.setOnCheckedChangeListener((btn, isChecked) ->
                cbAccesibil.setChecked(isChecked));

        ratingBar.setOnRatingBarChangeListener((rb, rating, fromUser) ->
                tvRatingValoare.setText("Rating: " + rating));

        paradisDeEditat = getIntent().getParcelableExtra("paradis");

        if (paradisDeEditat != null) {
            tvTitlu.setText("Modifica Paradis");
            btnSalveaza.setText("Salveaza modificarile");
            precompletareCampuri(paradisDeEditat);
        } else {
            tvTitlu.setText("Adauga Paradis");
        }

        btnSalveaza.setOnClickListener(v -> salveazaParadis());
    }

    private void aplicaSetariText() {
        SharedPreferences prefs = getSharedPreferences("setari_app", Context.MODE_PRIVATE);
        int dimensiune = prefs.getInt("text_size", 16);
        String culoare = prefs.getString("text_color", "#000000");
        int color = Color.parseColor(culoare);

        View root = findViewById(android.R.id.content);
        aplicaSetariPeView(root, dimensiune, color);
    }

    private void aplicaSetariPeView(View view, int dimensiune, int color) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                aplicaSetariPeView(group.getChildAt(i), dimensiune, color);
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setTextSize(dimensiune);
            ((TextView) view).setTextColor(color);
        }
    }

    private void precompletareCampuri(Paradis p) {
        etDenumire.setText(p.getDenumire());
        etVizitatori.setText(String.valueOf(p.getVizitatori()));
        etTemperatura.setText(String.valueOf(p.getTemperaturaMetdie()));

        cbAccesibil.setChecked(p.isEsteAccesibil());
        toggleAccesibil.setChecked(p.isEsteAccesibil());

        TipParadis[] tipuri = TipParadis.values();
        for (int i = 0; i < tipuri.length; i++) {
            if (tipuri[i] == p.getTip()) {
                spinnerTip.setSelection(i);
                break;
            }
        }

        if (p.getSejurDate() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(p.getSejurDate());
            datePicker.updateDate(
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            );
        }
    }

    private void salveazaParadis() {
        String denumire = etDenumire.getText().toString().trim();
        if (denumire.isEmpty()) {
            etDenumire.setError("Introduceti denumirea!");
            return;
        }

        int vizitatori;
        try {
            vizitatori = Integer.parseInt(etVizitatori.getText().toString().trim());
        } catch (NumberFormatException e) {
            etVizitatori.setError("Introduceti un numar valid!");
            return;
        }

        double temperatura;
        try {
            temperatura = Double.parseDouble(etTemperatura.getText().toString().trim());
        } catch (NumberFormatException e) {
            etTemperatura.setError("Introduceti o temperatura valida!");
            return;
        }

        boolean accesibil = cbAccesibil.isChecked();
        TipParadis tip = TipParadis.values()[spinnerTip.getSelectedItemPosition()];

        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date sejurDate = calendar.getTime();

       Paradis paradis = new Paradis(denumire, vizitatori, accesibil, temperatura, tip, sejurDate);

       salveazaInFisier(paradis);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("paradis", paradis);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void salveazaInFisier(Paradis p) {
        try (FileOutputStream fos = openFileOutput("paradisuri.txt", Context.MODE_APPEND);
             OutputStreamWriter osw = new OutputStreamWriter(fos)) {
            osw.write(p.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}