package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Kategorie;
import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.stores.KategorieStore;
import de.unidue.inf.is.stores.ProjektStore;
import de.unidue.inf.is.stores.StoreException;

public class NewProjectServlet extends HttpServlet
{
    private String HARDCODED_USER = "dummy@dummy.com";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        ProjektStore projektStore = new ProjektStore();
        KategorieStore kategorieStore = new KategorieStore();

        List<Projekt> eigeneProjekte = projektStore.getProjectsFromCreator(HARDCODED_USER);
        List<Kategorie> kategorien = kategorieStore.getAll();

        request.setAttribute("vorgaenger", eigeneProjekte);
        request.setAttribute("kategorien", kategorien);
        request.getRequestDispatcher("/new_project.ftl").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        String titel = request.getParameter("titel");
        String finanzierungslimit = request.getParameter("finanzierungslimit");
        String kategorie = request.getParameter("kategorie");
        String vorgaenger = request.getParameter("vorgaenger");
        String beschreibung = request.getParameter("beschreibung");

        List<String> report = check(titel, finanzierungslimit, kategorie, vorgaenger);
        if(report.isEmpty())
        {
            try(ProjektStore projektStore = new ProjektStore())
            {
                Projekt projekt = new Projekt();
                projekt.setTitel(titel);
                projekt.setFinanzierungslimit(Double.parseDouble(finanzierungslimit));
                projekt.setKategorie(Integer.parseInt(kategorie));
                projekt.setErsteller(HARDCODED_USER);
                projekt.setVorgaenger(Integer.parseInt(vorgaenger));
                projekt.setBeschreibung(beschreibung);
                projektStore.addProject(projekt);
                projektStore.complete();
                report.add("Projekt erfolgreich erstellt!");
            }
            catch(StoreException e)
            {
                report.add("Datenbank Fehler!");
                e.printStackTrace();
            }
        }
        request.setAttribute("report", report);
        doGet(request, response);
    }

    private List<String> check(String titel, String finanzierungslimit, String kategorie, String vorgaenger)
    {
        List<String> report = new ArrayList<>();
        if(titel == null || titel.isEmpty())
        {
            report.add("Kein Titel angegeben.");
        }

        if(titel != null && titel.length() > 30)
        {
            report.add("Titel zu lang (max. 30).");
        }
        try
        {
            double limit = Double.parseDouble(finanzierungslimit);
            if(limit < 100)
            {
                report.add("Zu kleines Finanzierungslimit (min. 100).");
            }
        }
        catch(NumberFormatException e)
        {
            report.add("Ungültige Eingabe für Finanzierungslimit.");
        }

        if(kategorie == null || kategorie.isEmpty())
        {
            report.add("Keine Kategorie ausgewählt.");
        }

        if(vorgaenger == null || vorgaenger.isEmpty())
        {
            report.add("Keine Option für Vorgänger ausgewählt.");
        }

        return report;
    }
}
