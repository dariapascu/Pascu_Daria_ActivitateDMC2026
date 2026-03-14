package com.example.lab4;

import android.os.Parcel;
import android.os.Parcelable;

public class Paradis implements Parcelable {

    private String denumire;

    private int vizitatori;

    private boolean esteAccesibil;

    private double temperaturaMetdie;

    private TipParadis tip;

    public Paradis() {
        this.denumire = "Necunoscut";
        this.vizitatori = 0;
        this.esteAccesibil = true;
        this.temperaturaMetdie = 25.0;
        this.tip = TipParadis.TROPICAL;
    }

    public Paradis(String denumire, int vizitatori,
                   boolean esteAccesibil, double temperaturaMetdie,
                   TipParadis tip) {
        this.denumire = denumire;
        this.vizitatori = vizitatori;
        this.esteAccesibil = esteAccesibil;
        this.temperaturaMetdie = temperaturaMetdie;
        this.tip = tip;
    }


    protected Paradis(Parcel in) {
        denumire         = in.readString();
        vizitatori       = in.readInt();
        esteAccesibil    = in.readByte() != 0;
        temperaturaMetdie = in.readDouble();
        tip              = TipParadis.valueOf(in.readString());
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

    @Override
    public String toString() {
        return "Paradis {" +
                "\n  Denumire: " + denumire +
                "\n  Vizitatori/an: " + vizitatori +
                "\n  Accesibil: " + (esteAccesibil ? "Da" : "Nu") +
                "\n  Temperatura medie: " + temperaturaMetdie + " C" +
                "\n  Tip: " + tip +
                "\n}";
    }
}