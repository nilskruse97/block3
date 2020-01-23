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

public class NewProjectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Projekt> eigeneProjekte = new ArrayList<>();
        eigeneProjekte.add(new Projekt(1, "titel1", "", "", 3.0, "", 1, 2));
        eigeneProjekte.add(new Projekt(2, "titel2", "", "", 3.0, "", 3, 4));
        List<Kategorie> kategorien = new ArrayList<>();
        kategorien.add(new Kategorie(1,"Kat1","pfad1"));
        kategorien.add(new Kategorie(2,"Kat2","pfad2"));
        
        request.setAttribute("vorgaenger", eigeneProjekte);
        request.setAttribute("kategorien", kategorien);
        request.getRequestDispatcher("/new_project.ftl").forward(request, response);
        
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException {

        String titel = request.getParameter("titel");
        String finanzierungslimit = request.getParameter("finanzierungslimit");

        doGet(request, response);
    }
}
