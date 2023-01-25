package hu.fzks.aukcioprojekt;

import jdk.jfr.Timespan;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    static ArrayList<Festmeny> festmenyek = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    static public void festmenyekBeolvasasa() {
        festmenyek.add(new Festmeny("Ásító inas", "Munkácsy Mihály" , "realizmus"));
        festmenyek.add(new Festmeny("Rőzsehordó nő", "Munkácsy Mihály" , "realizmus"));
        Scanner sc = new Scanner(System.in);
        int ujKepekSzama = 0;
        do {
            System.out.println("Hány darab festményt szeretne aukcióra bocsátani? ");
            String kepekSzama = sc.nextLine();
            try {
                ujKepekSzama = Integer.parseInt(kepekSzama);
                if (ujKepekSzama <= 0)
                    System.out.println("Nem adhat meg 1-nél kisebb számot!");
            } catch (Exception e){
                System.out.println("Nem számot adott meg!");
            }
        } while (ujKepekSzama <= 0);
        for (int i = 0; i < ujKepekSzama; i++) {
            System.out.println("\nKérem, adja meg az " + (i + 1) + ". kép adatait.");
            System.out.print("Kép címe: ");
            String cim = sc.nextLine();
            System.out.print("Festő neve: ");
            String festo = sc.nextLine();
            System.out.print("Stílus: ");
            String stilus = sc.nextLine();
            festmenyek.add(new Festmeny(cim, festo, stilus));
        }
        try {
            FileReader fr = new FileReader("festmenyek.csv");
            Scanner scan = new Scanner(fr);
            while (scan.hasNext()){
                String[] adatok = scan.nextLine().split(";");
                festmenyek.add(new Festmeny(adatok[1], adatok[0], adatok[2]));
            }
            fr.close();
        } catch (Exception e){
            System.out.println(e);
        }
        Random rnd = new Random();
        for (int i = 0; i < 20; i++) {
            int kepSzama = rnd.nextInt(1, festmenyek.size());
            int licit = rnd.nextInt(10, 101);
            festmenyek.get(kepSzama).licit(licit);
        }
    }
    static  public int sorszamBekeres(){
        String bekertSzam;
        int sorszam = 0;
        do {
            System.out.print("Kérem,adja meg a kép sorszámát: ");
            bekertSzam = sc.nextLine();
            try{
                int szam = Integer.parseInt(bekertSzam);
                if (szam < 1 || szam > festmenyek.size()){
                    System.out.println("\nNincs ilyen sorszámú festmény!");
                } else {
                    sorszam = szam;
                    if (festmenyek.get(sorszam).elkelt()){
                        System.out.println("A " + sorszam + ". számú festmény már elkelt!");
                    }
                }

            } catch (Exception e){
                System.out.println("Nem számot adott meg!");
            }

        } while (sorszam == 0);
        return sorszam - 1;
    }
    static public int licitBekeres(){
        int licit = 0;
        do {
            System.out.print("Kérem, adja meg a licitet: ");
            String bekertSzam = sc.nextLine();
            if (bekertSzam.equals("")){
                return 10;
            } else {
                try{
                    licit = Integer.parseInt(bekertSzam);
                } catch (Exception e){
                    System.out.println("Nem számot adott meg!");
                }
            }
        } while (licit < 0);
        return licit;
    }
    static public void licitalas(){
        LocalDateTime licitIdeje;
        System.out.println("Szeretne licitálni? (i/n)");
        String valasz = sc.nextLine();
        if (valasz.equals("i")){
            do {
                int kepSorszam = sorszamBekeres();
                Festmeny kivalsztottFestmeny = festmenyek.get(kepSorszam);
                if (kivalsztottFestmeny.getLegutolsoLicitIdeje() != null && kivalsztottFestmeny.getLegutolsoLicitIdeje().plusMinutes(2).isBefore(LocalDateTime.now())){
                    kivalsztottFestmeny.setElkelt(true);
                    System.out.println("Ez a festmény már elkelt!");
                } else {
                    int licitMerteke = licitBekeres();
                    kivalsztottFestmeny.licit(licitMerteke);
                    System.out.println("Szeretne újra licitálni? (i/n)");
                    valasz = sc.nextLine();
                }

            } while (valasz.equals("i"));
        }

    }

    public static void main(String[] args) {
        festmenyekBeolvasasa();
        licitalas();
        int osszeg = 0;
        int legdragabb = -1;
        boolean tiznelTobb = false;
        int nemKeltElSzama = 0;
        for (Festmeny festmeny: festmenyek) {
            if (festmeny.getLicitekSzama() != 0)
                festmeny.setElkelt(true);
            if (festmeny.elkelt() && festmeny.getLegmagasabbLicit() > osszeg)
                osszeg = festmeny.getLegmagasabbLicit();
                legdragabb = festmenyek.indexOf(festmeny);
            if (festmeny.getLicitekSzama() > 10)
                tiznelTobb = true;
            if (!festmeny.elkelt())
                nemKeltElSzama++;
            System.out.println(festmeny);
        }
        System.out.println("\nA legmagasabb áron elkelt festmény: " + festmenyek.get(legdragabb));
        System.out.println("\n" + (tiznelTobb ? "Volt " : "Nem volt ") + "olyan kép, amire 10-nél többször licitáltak.");
        System.out.println("\nAz aukción el nem kelt festmények száma: " + nemKeltElSzama + "\n");
        System.out.println("\n\nRendezett lista:\n");
        festmenyek.sort(Comparator.comparing(Festmeny::getLegmagasabbLicit).reversed());
        try{
            FileWriter fw = new FileWriter("festmenyek_rendezett.csv");
            for (Festmeny festmeny: festmenyek) {
                fw.write(festmeny.toString() + "\n");
                System.out.println(festmeny);
            }
            fw.close();
        } catch (Exception e){
            System.out.println(e);
        }


    }
}
