package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Benutzer;
import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.stores.BenutzerStore;
import de.unidue.inf.is.stores.StoreException;

public class ViewProfileServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private String USER = HardcodedUser.get();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String user = request.getParameter("mail");
        List<Projekt> erstellt = new ArrayList<>();
        List<Projekt> unterstuetzt = new ArrayList<>();
        List<Projekt> all = new ArrayList<>();
        if(user == null)
        {
            user = USER;
        }
        try(BenutzerStore bStore = new BenutzerStore())
        {
            Benutzer b = bStore.getUser(user);
            erstellt.addAll(bStore.getProjectsFromCreator(user));
            all.addAll(bStore.getAllProjectsFunded(user));
            unterstuetzt.addAll(bStore.getProjectsFunded(user));
            request.setAttribute("benutzer", b);
            request.setAttribute("anzahlErstellt", erstellt.size());
            request.setAttribute("anzahlUnterstuetzt", all.size());
            request.setAttribute("erstellte", erstellt);
            request.setAttribute("unterstuetzte", unterstuetzt);
        }
        catch(StoreException e)
        {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/view_profile.ftl").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        doGet(request, response);
    }
}
