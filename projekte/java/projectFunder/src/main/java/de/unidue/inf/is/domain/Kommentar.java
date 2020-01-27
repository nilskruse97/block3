package de.unidue.inf.is.domain;

public class Kommentar
{
    private String benutzer;
    private String text;
    private boolean sichtbar;
    private String nutzer;
    private int id;

    public Kommentar()
    {
        super();
    }

    public Kommentar(String benutzer, String kommentar, boolean sichtbar)
    {
        super();
        this.benutzer = benutzer;
        this.text = kommentar;
        this.sichtbar = sichtbar;
        this.id = id;
    }

    public String getBenutzer()
    {
        return benutzer;
    }

    public void setBenutzer(String benutzer)
    {
        this.benutzer = benutzer;
    }

    public String getKommentar()
    {
        return text;
    }

    public void setKommentar(String kommentar)
    {
        this.text = kommentar;
    }

    public boolean isSichtbar()
    {
        return sichtbar;
    }

    public void setSichtbar(boolean sichtbar)
    {
        this.sichtbar = sichtbar;
    }

    public String getNutzer()
    {
        return nutzer;
    }

    public void setNutzer(String nutzer)
    {
        this.nutzer = nutzer;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

}
