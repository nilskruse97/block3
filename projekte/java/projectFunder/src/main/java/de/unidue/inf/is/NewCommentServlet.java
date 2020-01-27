package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Kommentar;
import de.unidue.inf.is.domain.Kategorie;
import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.stores.KategorieStore;
import de.unidue.inf.is.stores.ProjektStore;
import de.unidue.inf.is.stores.StoreException;

public class NewCommentServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private String USER = HardcodedUser.get();
}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        List<String> report = new ArrayList<>();
        try(ProjektStore projektStore = new ProjektStore())
        {
            int projektKennung = Integer.parseInt(request.getParameter("kennung"));
            Projekt projekt = projektStore.getProjectForViewProject(projektKennung);
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
            report.add("Fehler beim Laden aus der Datenbank!");
            e.printStackTrace();
        }
        catch(NumberFormatException e)
        {
            report.add("Ungültige Kennung!");
        }
        request.setAttribute("report", report);

        request.getRequestDispatcher("/new_comment.ftl").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String text = request.getParameter("text");
        String sichtbarkeit = request.getParameter("sichtbarkeit");
        int projektKennung = Integer.parseInt(request.getParameter("kennung"));

        List<String> report = check(text);
        if(report.isEmpty())
        {
            try(ProjektStore projektStore = new ProjektStore(), )
            {
                Kommentar kommentar = new Kommentar();
                kommentar.setKommentar(text);
                kommentar.setSichtbar(!sichtbarkeit);

                projektStore.addComment(kommentar);
                projektStore.writesComment(kommentar, projektKennung)
                projektStore.complete();
                report.add("Kommentar erfolgreich erstellt!");
            }
            catch(StoreException e)
            {
                report.add("Datenbank Fehler!");
                e.printStackTrace();
            }
        }
        request.setAttribute("report", report);
        doGet(request, response);