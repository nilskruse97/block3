package de.unidue.inf.is.domain;

public class Spende
{
    private String spender;
    private int projekt;
    private double spendenbetrag;
    private boolean sichtbar;
    private String nutzer;

    public Spende()
    {
        super();
    }

    public Spende(String spender, int projekt, double spendenbetrag, boolean sichtbar)
    {
        super();
        this.spender = spender;
        this.projekt = projekt;
        this.spendenbetrag = spendenbetrag;
        this.sichtbar = sichtbar;
    }

    public String getSpender()
    {
        return spender;
    }

    public void setSpender(String spender)
    {
        this.spender = spender;
    }

    public int getProjekt()
    {
        return projekt;
    }

    public void setProjekt(int projekt)
    {
        this.projekt = projekt;
    }

    public double getSpendenbetrag()
    {
        return spendenbetrag;
    }

    public void setSpendenbetrag(double spendenbetrag)
    {
        this.spendenbetrag = spendenbetrag;
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
}
