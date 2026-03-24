package com.example.lab4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ParadisAdapter extends ArrayAdapter<Paradis> {

    private final Context context;
    private final ArrayList<Paradis> lista;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public ParadisAdapter(Context context, ArrayList<Paradis> lista) {
        super(context, R.layout.item_paradis, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_paradis, parent, false);
            holder = new ViewHolder();
            holder.tvNume        = convertView.findViewById(R.id.tvNume);
            holder.tvTip         = convertView.findViewById(R.id.tvTip);
            holder.tvTemperatura = convertView.findViewById(R.id.tvTemperatura);
            holder.tvVizitatori  = convertView.findViewById(R.id.tvVizitatori);
            holder.tvData        = convertView.findViewById(R.id.tvData);
            holder.tvAccesibil   = convertView.findViewById(R.id.tvAccesibil);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Paradis p = lista.get(position);

        holder.tvNume.setText(p.getDenumire());
        holder.tvTip.setText(p.getTip().name());
        holder.tvTemperatura.setText("Temperatura " + p.getTemperaturaMetdie() + " C");
        holder.tvVizitatori.setText("Vizitatori " + String.format(Locale.getDefault(), "%,d", p.getVizitatori()));
        holder.tvData.setText("Data " + (p.getSejurDate() != null ? sdf.format(p.getSejurDate()) : "N/A"));

        if (p.isEsteAccesibil()) {
            holder.tvAccesibil.setText("✓ Accesibil");
            holder.tvAccesibil.getBackground().setTint(0xFF2E7D32);
        } else {
            holder.tvAccesibil.setText("✗ Inaccesibil");
            holder.tvAccesibil.getBackground().setTint(0xFFC62828);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tvNume, tvTip, tvTemperatura, tvVizitatori, tvData, tvAccesibil;
    }
}
