package com.example.lab8;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CarteDao dao;
    private ArrayAdapter<String> adapter;
    private final List<String> listItems = new ArrayList<>();

    private EditText etTitlu, etAutor, etAn;
    private EditText etCautaTitlu;
    private EditText etAnMin, etAnMax;
    private EditText etStergeAn;
    private EditText etLitera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = AppDatabase.getDatabase(this).carteDao();

        etTitlu       = findViewById(R.id.etTitlu);
        etAutor       = findViewById(R.id.etAutor);
        etAn          = findViewById(R.id.etAn);
        etCautaTitlu  = findViewById(R.id.etCautaTitlu);
        etAnMin       = findViewById(R.id.etAnMin);
        etAnMax       = findViewById(R.id.etAnMax);
        etStergeAn    = findViewById(R.id.etStergeAn);
        etLitera      = findViewById(R.id.etLitera);
        ListView listView = findViewById(R.id.listView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);

        ((Button) findViewById(R.id.btnAdauga)).setOnClickListener(v -> {
            String titlu = etTitlu.getText().toString().trim();
            String autor = etAutor.getText().toString().trim();
            String anStr = etAn.getText().toString().trim();
            if (titlu.isEmpty() || autor.isEmpty() || anStr.isEmpty()) {
                toast("Completati toate campurile!");
                return;
            }
            dao.insert(new Carte(titlu, autor, Integer.parseInt(anStr)));
            etTitlu.setText(""); etAutor.setText(""); etAn.setText("");
            toast("Carte adaugata!");
        });

        ((Button) findViewById(R.id.btnToate)).setOnClickListener(v ->
                showList(dao.getAll()));

        ((Button) findViewById(R.id.btnCautaTitlu)).setOnClickListener(v -> {
            String titlu = etCautaTitlu.getText().toString().trim();
            if (titlu.isEmpty()) { toast("Introduceti un titlu!"); return; }
            showList(dao.getByTitlu(titlu));
        });

        ((Button) findViewById(R.id.btnInterval)).setOnClickListener(v -> {
            String minStr = etAnMin.getText().toString().trim();
            String maxStr = etAnMax.getText().toString().trim();
            if (minStr.isEmpty() || maxStr.isEmpty()) { toast("Introduceti intervalul!"); return; }
            showList(dao.getByAnInterval(Integer.parseInt(minStr), Integer.parseInt(maxStr)));
        });

        ((Button) findViewById(R.id.btnSterge)).setOnClickListener(v -> {
            String anStr = etStergeAn.getText().toString().trim();
            if (anStr.isEmpty()) { toast("Introduceti un an!"); return; }
            dao.deleteWhereAnGreaterThan(Integer.parseInt(anStr));
            showList(dao.getAll());
            toast("Inregistrari sterse. Lista actualizata.");
        });

        ((Button) findViewById(R.id.btnIncrement)).setOnClickListener(v -> {
            String litera = etLitera.getText().toString().trim();
            if (litera.isEmpty()) { toast("Introduceti o litera!"); return; }
            dao.incrementAnWhereTitluStartsWith(litera + "%");
            showList(dao.getAll());
            toast("Valori incrementate. Lista actualizata.");
        });
    }

    private void showList(List<Carte> carti) {
        listItems.clear();
        for (Carte c : carti) listItems.add(c.toString());
        adapter.notifyDataSetChanged();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
