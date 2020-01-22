package de.unidue.inf.is.domain;

public class Projekt
{
	private int kennung;
	private String titel;
	private String beschreibung;
	private String status;
	private double finanzierungslimit;
	private String ersteller;
	private int vorgaenger;
	private int kategorie;

	public Projekt()
	{
		super();
	}
	public Projekt(int kennung, String titel, String beschreibung, String status, double finanzierungslimit,
			String ersteller, int vorgaenger, int kategorie)
	{
		super();
		this.kennung = kennung;
		this.titel = titel;
		this.beschreibung = beschreibung;
		this.status = status;
		this.finanzierungslimit = finanzierungslimit;
		this.ersteller = ersteller;
		this.vorgaenger = vorgaenger;
		this.kategorie = kategorie;
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

	public int getVorgaenger()
	{
		return vorgaenger;
	}

	public void setVorgaenger(int vorgaenger)
	{
		this.vorgaenger = vorgaenger;
	}

	public int getKategorie()
	{
		return kategorie;
	}

	public void setKategorie(int kategorie)
	{
		this.kategorie = kategorie;
	}
}
