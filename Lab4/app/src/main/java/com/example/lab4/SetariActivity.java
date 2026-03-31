package com.example.lab4;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetariActivity extends AppCompatActivity {

    private SeekBar seekBarDimensiune;
    private RadioGroup rgCuloare;
    private TextView tvPreview;
    private Button btnSalveaza;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setari);

        prefs = getSharedPreferences("setari_app", Context.MODE_PRIVATE);

        seekBarDimensiune = findViewById(R.id.seekBarDimensiune);
        rgCuloare         = findViewById(R.id.rgCuloare);
        tvPreview         = findViewById(R.id.tvPreview);
        btnSalveaza       = findViewById(R.id.btnSalveaza);

        int dimensiune = prefs.getInt("text_size", 16);
        String culoare = prefs.getString("text_color", "#000000");

        seekBarDimensiune.setProgress(dimensiune);
        aplicaSetari(dimensiune, culoare);

        seekBarDimensiune.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
                tvPreview.setTextSize(progress);
            }
            public void onStartTrackingTouch(SeekBar s) {}
            public void onStopTrackingTouch(SeekBar s) {}
        });

        btnSalveaza.setOnClickListener(v -> {
            int dim = seekBarDimensiune.getProgress();
            String col = culoareSelectata();

            prefs.edit()
                    .putInt("text_size", dim)
                    .putString("text_color", col)
                    .apply();

            Toast.makeText(this, "Setari salvate!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private String culoareSelectata() {
        int id = rgCuloare.getCheckedRadioButtonId();
        if (id == R.id.rbRosu)  return "#C62828";
        if (id == R.id.rbAlbastru) return "#1A237E";
        return "#000000";
    }

    private void aplicaSetari(int dimensiune, String culoare) {
        tvPreview.setTextSize(dimensiune);
        tvPreview.setTextColor(Color.parseColor(culoare));
    }
}