package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

public class EditProjectServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private String USER = HardcodedUser.get();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	List<String> report = new ArrayList<>();
        try(ProjektStore projektStore = new ProjektStore(); KategorieStore kategorieStore = new KategorieStore())
        {
            List<Projekt> eigeneProjekte = projektStore.getProjectsFromCreator(USER);
            List<Kategorie> kategorien = kategorieStore.getAll();

            request.setAttribute("vorgaenger", eigeneProjekte);
            request.setAttribute("kategorien", kategorien);

            int kennung = Integer.parseInt(request.getParameter("kennung"));
            Projekt projekt = projektStore.getProjectForViewProject(kennung);

            if(projekt == null)
            {
                report.add("Projekt nicht gefunden!");
            }
            else
            {
                request.setAttribute("projekt", projekt);
            }

        }
        catch(StoreException e)
        {
            request.setAttribute("report", Arrays.asList("Fehler beim Laden aus der Datenbank!"));
            e.printStackTrace();
        }
        catch(NumberFormatException e)
        {
            report.add("Ungültige Kennung!");
        }
        request.setAttribute("report", report);
        request.getRequestDispatcher("/edit_project.ftl").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int kennung = Integer.parseInt(request.getParameter("kennung"));
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
                projekt.setFkKategorie(Integer.parseInt(kategorie));
                projekt.setErsteller(USER);
                projekt.setFkVorgaenger(Integer.parseInt(vorgaenger));
                projekt.setBeschreibung(beschreibung);
                projektStore.updateProject(projekt, kennung);
                projektStore.complete();
                report.add("Projekt erfolgreich editiert!");
                response.sendRedirect("/view_project?kennung=" + kennung);
            }
            catch(StoreException e)
            {
                report.add("Datenbank Fehler!");
                e.printStackTrace();
            }
        }
        request.setAttribute("report", report);
        response.sendRedirect("/view_project?kennung=" + kennung);
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
