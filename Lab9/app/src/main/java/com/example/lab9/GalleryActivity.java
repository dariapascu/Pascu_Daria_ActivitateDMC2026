package com.example.lab9;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GalleryActivity extends AppCompatActivity {

    private SpaceAdapter adapter;
    private List<SpaceItem> items;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        items = buildItemList();

        adapter = new SpaceAdapter(this, items);
        ListView listView = findViewById(R.id.listViewGallery);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            SpaceItem item = items.get(position);
            Intent intent = new Intent(GalleryActivity.this, WebViewActivity.class);
            intent.putExtra("url", item.getWebLink());
            intent.putExtra("title", item.getDescription());
            startActivity(intent);
        });

        loadImagesInBackground();
    }

    private List<SpaceItem> buildItemList() {
        List<SpaceItem> list = new ArrayList<>();

        list.add(new SpaceItem(
                "https://images-assets.nasa.gov/image/as11-40-5931/as11-40-5931~thumb.jpg",
                "Apollo 11 – Moon Landing",
                "https://www.nasa.gov/mission/apollo-11/"
        ));

        list.add(new SpaceItem(
                "https://images-assets.nasa.gov/image/PIA04591/PIA04591~thumb.jpg",
                "Mars – The Red Planet",
                "https://mars.nasa.gov/"
        ));

        list.add(new SpaceItem(
                "https://images-assets.nasa.gov/image/PIA06193/PIA06193~thumb.jpg",
                "Saturn and Its Rings",
                "https://solarsystem.nasa.gov/planets/saturn/overview/"
        ));

        list.add(new SpaceItem(
                "https://images-assets.nasa.gov/image/PIA21974/PIA21974~thumb.jpg",
                "Jupiter – Juno Mission",
                "https://science.nasa.gov/jupiter/"
        ));

        list.add(new SpaceItem(
                "https://images-assets.nasa.gov/image/PIA15415/PIA15415~thumb.jpg",
                "Milky Way Galaxy",
                "https://science.nasa.gov/universe/galaxies/"
        ));

        return list;
    }

    private void loadImagesInBackground() {
        executor = Executors.newFixedThreadPool(5);

        for (SpaceItem item : items) {
            executor.execute(() -> {
                Bitmap bitmap = downloadBitmap(item.getImageUrl());
                if (bitmap != null) {
                    item.setBitmap(bitmap);
                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(GalleryActivity.this,
                                    "Failed to load: " + item.getDescription(),
                                    Toast.LENGTH_SHORT).show()
                    );
                }
            });
        }
    }

    private Bitmap downloadBitmap(String urlStr) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Android)");
            conn.setInstanceFollowRedirects(true);
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                final String msg = "HTTP " + responseCode + " for " + urlStr;
                runOnUiThread(() -> Toast.makeText(GalleryActivity.this, msg, Toast.LENGTH_LONG).show());
                return null;
            }

            InputStream is = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return bitmap;
        } catch (Exception e) {
            final String msg = e.getClass().getSimpleName() + ": " + e.getMessage();
            runOnUiThread(() -> Toast.makeText(GalleryActivity.this, msg, Toast.LENGTH_LONG).show());
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
    }
}
