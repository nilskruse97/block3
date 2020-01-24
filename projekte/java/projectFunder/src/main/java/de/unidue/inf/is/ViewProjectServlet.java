package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.stores.ProjektStore;
import de.unidue.inf.is.stores.StoreException;

public class ViewProjectServlet extends HttpServlet
{
    private String HARDCODED_USER = "dummy@dummy.com";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        List<String> report = new ArrayList<>();
        try(ProjektStore projektStore = new ProjektStore())
        {
            int kennung = Integer.parseInt(request.getParameter("kennung"));
            Projekt projekt = projektStore.getProject(kennung);
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
            e.printStackTrace();
        }
        request.setAttribute("report", report);
        request.getRequestDispatcher("/view_project.ftl").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        doGet(request, response);
    }
}
