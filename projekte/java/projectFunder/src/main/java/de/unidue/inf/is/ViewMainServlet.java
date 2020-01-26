package de.unidue.inf.is;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.stores.ProjektStore;
import de.unidue.inf.is.stores.StoreException;

public class ViewMainServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try(ProjektStore projektStore = new ProjektStore())
        {
            Map<String,List<Projekt>> projects = projektStore.getProjectsForMain();
            request.setAttribute("projekte", projects.get("offen"));
            request.setAttribute("projekte2", projects.get("geschlossen"));
        }catch(StoreException e) {
            request.setAttribute("report", Arrays.asList("Datenbankfehler!"));
            e.printStackTrace();
        }

        request.getRequestDispatcher("/view_main.ftl").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doGet(request, response);
    }
}
