package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.stores.KontoSpendenStore;
import de.unidue.inf.is.stores.ProjektStore;
import de.unidue.inf.is.stores.StoreException;

public class NewProjectFundServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    private String USER = HardcodedUser.get();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        List<String> report = new ArrayList<>();
        try(ProjektStore projektStore = new ProjektStore())
        {
            int kennung = Integer.parseInt(request.getParameter("kennung"));
            Projekt projekt = projektStore.getProjectForViewProject(kennung);
            if(projekt == null)
            {
                report.add("Projekt nicht gefunden!");
            }
            else
            {
                if(projekt.getStatus().equalsIgnoreCase("offen"))
                {
                    request.setAttribute("projekt", projekt);
                }
                else
                {
                    report.add("Projekt nicht aktiv!");
                }

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
        request.getRequestDispatcher("/new_project_fund.ftl").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String spendenbetrag = request.getParameter("spendenbetrag");
        String sichtbarkeit = request.getParameter("sichtbarkeit");
        String kennung = request.getParameter("kennung");
        List<String> report = new ArrayList<>();
        Double spende = Double.parseDouble(spendenbetrag);
        int projekt = Integer.parseInt(kennung);
        if(spende <= 0)
        {
            report.add("Spende muss größer als 0 sein.");
        }
        else
        {
            try(KontoSpendenStore kStore = new KontoSpendenStore(); ProjektStore pStore = new ProjektStore())
            {
                if(kStore.getBalance(USER) < spende)
                {
                    report.add("Nicht genug Geld.");
                }
                else
                {
                    if(kStore.fundExists(USER, projekt))
                    {
                        report.add("Projekt bereits unterstützt.");
                    }
                    else
                    {
                        kStore.addFund(USER, projekt, spende, sichtbarkeit == null ? "oeffentlich" : "privat");
                        kStore.complete();
                        Projekt projekt2 = pStore.getProjectForViewProject(projekt);
                        if(projekt2.getSpendenmenge() >= projekt2.getFinanzierungslimit())
                        {
                            pStore.setClosed(projekt);
                            pStore.complete();
                        }
                    }
                }

            }
            catch(StoreException e)
            {

                report.add("Datenbankfehler.");
                e.printStackTrace();
            }

        }
        request.setAttribute("report", report);
        response.sendRedirect("/view_project?kennung=" + kennung);
    }
}
