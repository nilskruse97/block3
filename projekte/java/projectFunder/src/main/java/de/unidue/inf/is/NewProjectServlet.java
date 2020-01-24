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

public class NewProjectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProjektStore projektStore = new ProjektStore();
        KategorieStore kategorieStore = new KategorieStore();
        
        
        List<Projekt> eigeneProjekte = projektStore.getProjectsFromCreator("dummy@dummy.com");
        List<Kategorie> kategorien = kategorieStore.getAll();

        request.setAttribute("vorgaenger", eigeneProjekte);
        request.setAttribute("kategorien", kategorien);
        request.getRequestDispatcher("/new_project.ftl").forward(request, response);

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        String titel = request.getParameter("titel");
        String finanzierungslimit = request.getParameter("finanzierungslimit");
        String kategorie = request.getParameter("kategorie");
        String vorgaenger = request.getParameter("vorgaenger");
        String beschreibung = request.getParameter("beschreibung");
        
        List<String> fehler = check(titel,finanzierungslimit,kategorie, vorgaenger);
        if(!fehler.isEmpty()) {
            request.setAttribute("fehler", fehler);
        }
        
        doGet(request, response);
    }
    
    private List<String> check(String titel, String finanzierungslimit, String kategorie, String vorgaenger){
        List<String> fehler = new ArrayList<>();
        if(titel == null || titel.isEmpty()) {
            fehler.add("Kein Titel angegeben.");
        }
        
        if(titel != null && titel.length() > 30) {
            fehler.add("Titel zu lang (max. 30).");
        }
        try {
            double limit = Double.parseDouble(finanzierungslimit);
            if(limit < 100) {
                fehler.add("Zu kleines Finanzierungslimit (min. 100).");
            }
        }catch (NumberFormatException e) {
            fehler.add("Ungültige Eingabe für Finanzierungslimit.");
        }
        
        if(kategorie == null || kategorie.isEmpty()) {
            fehler.add("Keine Kategorie ausgewählt.");
        }
        
        if(vorgaenger == null || vorgaenger.isEmpty()) {
            fehler.add("Keine Option für Vorgänger ausgewählt.");
        }
       
        return fehler;
    }
}
