package hu.fzks.aukcioprojekt;

import java.util.Locale;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

public class Festmeny {
    private String cim;
    private String festo;
    private String stilus;
    private int licitekSzama;
    private int legmagasabbLicit;
    private LocalDateTime legutolsoLicitIdeje;
    private boolean elkelt;

    public Festmeny(String cim, String festo, String stilus) {
        this.cim = cim;
        this.festo = festo;
        this.stilus = stilus;
        this.licitekSzama = 0;
        this.legmagasabbLicit = 0;
        this.elkelt = false;
    }

    public String getFesto() {
        return festo;
    }

    public String getStilus() {
        return stilus;
    }

    public int getLicitekSzama() {
        return licitekSzama;
    }

    public int getLegmagasabbLicit() {
        int licitKerekitve = 0;
        if (legmagasabbLicit > 999){
            licitKerekitve = ((legmagasabbLicit / 100) * 10) * 10;
        } else {
            licitKerekitve = ((legmagasabbLicit / 10) * 10);
        }
        return legmagasabbLicit;
    }

    public LocalDateTime getLegutolsoLicitIdeje() {
        return legutolsoLicitIdeje;
    }

    public boolean elkelt() {
        return elkelt;
    }

    public void setElkelt(boolean elkelt) {
        this.elkelt = elkelt;
    }

    public void licit(){
        if (this.elkelt){
            System.out.println("Ez a festmény már elkelt!");
        }
        if (licitekSzama == 0){
            this.legmagasabbLicit = 100;
            this.licitekSzama++;
            this.legutolsoLicitIdeje = LocalDateTime.now();
        } else {
            licit(10);
        }

    }

    public void licit(int mertek){
        if (mertek < 10 || mertek > 100){
            System.out.println("Csak 10-100 közti értéket adhat meg!");
            return;
        }
        if (this.elkelt){
            System.out.println("Ez a festmény már elkelt!");
        }
        if (licitekSzama == 0){
            this.legmagasabbLicit = 100;

            this.licitekSzama++;
            this.legutolsoLicitIdeje = LocalDateTime.now();
        } else {
            this.legmagasabbLicit += (legmagasabbLicit/100) * mertek;

            this.licitekSzama++;
            this.legutolsoLicitIdeje = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "\n" + this.festo + ": " + this.cim + " (" + this.stilus + ")\n" +
                (this.elkelt ? "elkelt\n" : "") +
                this.legmagasabbLicit + "$ - " + this.legutolsoLicitIdeje + " (összesen: " + this.licitekSzama + " db)";
    }
}
