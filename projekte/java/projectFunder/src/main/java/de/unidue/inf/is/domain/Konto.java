package de.unidue.inf.is.domain;

public class Konto
{
    private String inhaber;
    private double guthaben;
    private String geheimzahl;

    public Konto()
    {
        super();
    }

    public Konto(String inhaber, double guthaben, String geheimzahl)
    {
        super();
        this.inhaber = inhaber;
        this.guthaben = guthaben;
        this.geheimzahl = geheimzahl;
    }

    public String getInhaber()
    {
        return inhaber;
    }

    public void setInhaber(String inhaber)
    {
        this.inhaber = inhaber;
    }

    public double getGuthaben()
    {
        return guthaben;
    }

    public void setGuthaben(double guthaben)
    {
        this.guthaben = guthaben;
    }

    public String getGeheimzahl()
    {
        return geheimzahl;
    }

    public void setGeheimzahl(String geheimzahl)
    {
        this.geheimzahl = geheimzahl;
    }

}
