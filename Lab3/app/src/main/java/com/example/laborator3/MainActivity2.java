package com.example.laborator3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    private static final String tag = "SecondActivity";

    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    (ActivityResult result) -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            String mesajPrimit  = data.getStringExtra("mesaj_inapoi");
                            int    sumaCalculata = data.getIntExtra("suma", 0);

                            Toast.makeText(
                                    this,
                                    "Mesaj primit: " + mesajPrimit + "\nSuma: " + sumaCalculata,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        Log.i(tag, "activitate 2 deschisa");

        Button btnGoToThird = findViewById(R.id.btnGoToThird);
        btnGoToThird.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putString("mesaj",    "Activity2");
            bundle.putInt("nr1", 15);
            bundle.putInt("nr2", 27);

            Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
            intent.putExtras(bundle);

            launcher.launch(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}