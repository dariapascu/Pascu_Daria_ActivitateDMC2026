package com.example.lab4;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Paradis implements Parcelable {

    private String denumire;

    private int vizitatori;

    private boolean esteAccesibil;

    private double temperaturaMetdie;

    private TipParadis tip;

    private Date sejurDate;

    public Paradis() {
        this.denumire = "Necunoscut";
        this.vizitatori = 0;
        this.esteAccesibil = true;
        this.temperaturaMetdie = 25.0;
        this.tip = TipParadis.TROPICAL;
        this.sejurDate = new Date();
    }

    public Paradis(String denumire, int vizitatori,
                   boolean esteAccesibil, double temperaturaMetdie,
                   TipParadis tip, Date sejurDate) {
        this.denumire = denumire;
        this.vizitatori = vizitatori;
        this.esteAccesibil = esteAccesibil;
        this.temperaturaMetdie = temperaturaMetdie;
        this.tip = tip;
        this.sejurDate = sejurDate;
    }


    protected Paradis(Parcel in) {
        denumire         = in.readString();
        vizitatori       = in.readInt();
        esteAccesibil    = in.readByte() != 0;
        temperaturaMetdie = in.readDouble();
        tip              = TipParadis.valueOf(in.readString());
        long millis       = in.readLong();
        sejurDate         = millis == -1 ? null : new Date(millis);
    }

    public static final Creator<Paradis> CREATOR = new Creator<Paradis>() {
        @Override
        public Paradis createFromParcel(Parcel in) { return new Paradis(in); }

        @Override
        public Paradis[] newArray(int size) { return new Paradis[size]; }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(denumire);
        dest.writeInt(vizitatori);
        dest.writeByte((byte) (esteAccesibil ? 1 : 0));
        dest.writeDouble(temperaturaMetdie);
        dest.writeString(tip.name());
        dest.writeLong(sejurDate != null ? sejurDate.getTime() : -1);
    }


    public String getDenumire() { return denumire; }
    public void setDenumire(String denumire) { this.denumire = denumire; }

    public int getVizitatori() { return vizitatori; }
    public void setVizitatori(int vizitatori) {
        if (vizitatori >= 0) this.vizitatori = vizitatori;
    }

    public boolean isEsteAccesibil() { return esteAccesibil; }
    public void setEsteAccesibil(boolean esteAccesibil) { this.esteAccesibil = esteAccesibil; }

    public double getTemperaturaMetdie() { return temperaturaMetdie; }
    public void setTemperaturaMetdie(double temperaturaMetdie) { this.temperaturaMetdie = temperaturaMetdie; }

    public TipParadis getTip() { return tip; }
    public void setTip(TipParadis tip) { this.tip = tip; }

    public Date getSejurDate() { return sejurDate; }
    public void setSejurDate(Date sejurDate) { this.sejurDate = sejurDate; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataStr = sejurDate != null ? sdf.format(sejurDate) : "N/A";
        return denumire + " | " + tip.name() +
                " | " + vizitatori + " vizitatori" +
                " | " + temperaturaMetdie + " C" +
                " | " + (esteAccesibil ? "Accesibil" : "Inaccesibil") +
                " | Sejur: " + dataStr;
    }
}