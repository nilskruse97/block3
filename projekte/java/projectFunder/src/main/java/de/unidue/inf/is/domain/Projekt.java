package de.unidue.inf.is.domain;

import java.util.List;

public class Projekt
{
    private int kennung;
    private String titel;
    private String beschreibung;
    private String status;
    private double finanzierungslimit;
    private String ersteller;
    private int fkVorgaenger;
    private int fkKategorie;
    private double spendenmenge;
    private Projekt vorgaenger;
    private Kategorie kategorie;
    private List<Kommentar> kommentare;
   

    public Projekt()
    {
        super();
    }

    public Projekt(
            int kennung,
            String titel,
            String beschreibung,
            String status,
            double finanzierungslimit,
            String ersteller,
            int vorgaenger,
            int kategorie)
    {
        super();
        this.kennung = kennung;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.status = status;
        this.finanzierungslimit = finanzierungslimit;
        this.ersteller = ersteller;
        this.fkVorgaenger = vorgaenger;
        this.fkKategorie = kategorie;
    }

    public int getKennung()
    {
        return kennung;
    }

    public void setKennung(int kennung)
    {
        this.kennung = kennung;
    }

    public String getTitel()
    {
        return titel;
    }

    public void setTitel(String titel)
    {
        this.titel = titel;
    }

    public String getBeschreibung()
    {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung)
    {
        this.beschreibung = beschreibung;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public double getFinanzierungslimit()
    {
        return finanzierungslimit;
    }

    public void setFinanzierungslimit(double finanzierungslimit)
    {
        this.finanzierungslimit = finanzierungslimit;
    }

    public String getErsteller()
    {
        return ersteller;
    }

    public void setErsteller(String string)
    {
        this.ersteller = string;
    }

    public int getFkVorgaenger()
    {
        return fkVorgaenger;
    }

    public void setFkVorgaenger(int vorgaenger)
    {
        this.fkVorgaenger = vorgaenger;
    }

    public int getFkKategorie()
    {
        return fkKategorie;
    }

    public void setFkKategorie(int kategorie)
    {
        this.fkKategorie = kategorie;
    }

    public Projekt getVorgaenger()
    {
        return vorgaenger;
    }

    public void setVorgaenger(Projekt vorgaenger)
    {
        this.vorgaenger = vorgaenger;
    }

    public Kategorie getKategorie()
    {
        return kategorie;
    }

    public void setKategorie(Kategorie kategorie)
    {
        this.kategorie = kategorie;
    }

    public List<Kommentar> getKommentare()
    {
        return kommentare;
    }

    public void setKommentare(List<Kommentar> kommentare)
    {
        this.kommentare = kommentare;
    }

    public double getSpendenmenge()
    {
        return spendenmenge;
    }

    public void setSpendenmenge(double spendenmenge)
    {
        this.spendenmenge = spendenmenge;
    }
}
