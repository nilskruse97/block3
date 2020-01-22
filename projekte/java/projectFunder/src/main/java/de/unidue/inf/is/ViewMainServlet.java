package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.ProjektStore;

public class ViewMainServlet extends HttpServlet {
	ArrayList<Projekt> openProjects = new ArrayList<>();
	ArrayList<Projekt> closedProjects = new ArrayList<>();
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProjektStore projektStore = new ProjektStore();
		openProjects = projektStore.getOpenProjects();
		System.out.println(openProjects);
		request.setAttribute("projekte", projektStore.getOpenProjects());
		request.setAttribute("projekte2", projektStore.getClosedProjects());
        request.getRequestDispatcher("/view_main.ftl").forward(request, response);
    }
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException {
        doGet(request, response);
    }
}
