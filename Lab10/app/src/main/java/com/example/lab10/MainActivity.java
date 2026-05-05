package com.example.lab10;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "";

    private EditText editTextCity;
    private TextView textViewResult;
    private Spinner spinnerDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        textViewResult = findViewById(R.id.textViewResult);
        spinnerDays = findViewById(R.id.spinnerDays);
        Button buttonSearch = findViewById(R.id.buttonSearch);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"1 zi", "5 zile"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDays.setAdapter(adapter);

        buttonSearch.setOnClickListener(v -> {
            String city = editTextCity.getText().toString().trim();
            if (!city.isEmpty()) {
                new CitySearchTask().execute(city);
            }
        });
    }

    private class CitySearchTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            try {
                String urlString = "https://dataservice.accuweather.com/locations/v1/cities/search"
                        + "?apikey=" + API_KEY
                        + "&q=" + city;

                URL url = URI.create(urlString).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                connection.disconnect();

                JSONArray jsonArray = new JSONArray(response.toString());
                if (jsonArray.length() > 0) {
                    JSONObject firstCity = jsonArray.getJSONObject(0);
                    return firstCity.getString("Key");
                } else {
                    return "Orasul nu a fost gasit";
                }
            } catch (JSONException e) {
                return "Eroare parsare JSON: " + e.getMessage();
            } catch (Exception e) {
                return "Eroare: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            textViewResult.setText("Cod oras: " + result);
            try {
                Integer.parseInt(result);
                String days = spinnerDays.getSelectedItemPosition() == 0 ? "1day" : "5day";
                new WeatherTask().execute(result, days);
            } catch (NumberFormatException e) {
               
            }
        }
    }

    private class WeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String cityKey = params[0];
            String days = params[1];
            try {
                String urlString = "https://dataservice.accuweather.com/forecasts/v1/daily/" + days + "/"
                        + cityKey
                        + "?apikey=" + API_KEY
                        + "&metric=true";

                URL url = URI.create(urlString).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                connection.disconnect();

                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray dailyForecasts = jsonObject.getJSONArray("DailyForecasts");

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < dailyForecasts.length(); i++) {
                    JSONObject forecast = dailyForecasts.getJSONObject(i);
                    String date = forecast.getString("Date").substring(0, 10);
                    JSONObject temperature = forecast.getJSONObject("Temperature");
                    double minTemp = temperature.getJSONObject("Minimum").getDouble("Value");
                    double maxTemp = temperature.getJSONObject("Maximum").getDouble("Value");
                    sb.append("Ziua ").append(i + 1).append(" (").append(date).append("): ")
                      .append("Min: ").append(minTemp).append(" C, ")
                      .append("Max: ").append(maxTemp).append(" C\n");
                }
                return sb.toString().trim();
            } catch (JSONException e) {
                return "Eroare parsare JSON: " + e.getMessage();
            } catch (Exception e) {
                return "Eroare: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            textViewResult.append("\nTemperatura:\n" + result);
        }
    }
}
