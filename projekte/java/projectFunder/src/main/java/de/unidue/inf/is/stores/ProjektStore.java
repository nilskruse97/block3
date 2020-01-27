package de.unidue.inf.is.stores;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.unidue.inf.is.domain.Kategorie;
import de.unidue.inf.is.domain.Kommentar;
import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.domain.Spende;

public class ProjektStore extends AbstractStore
{

    private final String PROJECT_FROM_CREATOR_QUERY = "select * from dbp064.projekt where ersteller=(?)";
    private final String INSERT_PROJECT = "INSERT INTO dbp064.projekt (titel, beschreibung, finanzierungslimit, ersteller, vorgaenger, kategorie) VALUES (?,?,?,?,?,?)";
    private final String PROJECT_FROM_ID_QUERY = "select * from dbp064.projekt where kennung=(?)";
    private final String UPDATE_PROJECT = "update projekt p set p.titel = ?, p.beschreibung = ?, p.finanzierungslimit = ?, p.vorgaenger = ?, p.kategorie = ? where p.kennung = ?";
    private final String OPEN_PROJECTS_FOR_MAIN_QUERY = "select p.titel, p.ersteller, (select SUM(spendenbetrag) from dbp064.spenden s where s.projekt = p.kennung), k.icon, p.kennung from dbp064.projekt p, dbp064.kategorie k where p.kategorie = k.id and p.status='offen'";
    private final String CLOSED_PROJECTS_FOR_MAIN_QUERY = "select p.titel, p.ersteller, (select SUM(spendenbetrag) from dbp064.spenden s where s.projekt = p.kennung), k.icon, p.kennung from dbp064.projekt p, dbp064.kategorie k where p.kategorie = k.id and p.status='geschlossen'";
    private final String PROJECTS_FOR_VIEW_PROJECT_QUERY = "select k.icon, p.titel, p.ersteller, p.beschreibung, p.finanzierungslimit, "
            + "(select SUM(spendenbetrag) from dbp064.spenden s where s.projekt = p.kennung), p.status, p.vorgaenger "
            + "from dbp064.projekt p, dbp064.kategorie k where p.kategorie = k.id and p.kennung = ?";
    private final String GET_COMMENTS_QUERY = "select s.benutzer, k.text, k.sichtbarkeit from dbp064.kommentar k join dbp064.schreibt s on s.kommentar = k.id where s.projekt=? order by k.id desc";
    private final String GET_FUNDS_QUERY = "select s.spender, s.spendenbetrag, s.sichtbarkeit from dbp064.spenden s where s.projekt=? order by s.spendenbetrag desc";

    private final String INSERT_COMMENT = "INSERT INTO dbp064.kommentar (text, sichtbarkeit, id) VALUES (?,?,?)";
    private final String INSERT_WRITES = "INSERT INTO dbp064.schreibt (benutzer, projekt, kommentar) VALUES (?,?,?)";

    private final String DELETE_PROJECT = "delete from dbp064.projekt p where p.kennung=?";
    private final String DELETE_COMMENTS = "delete from dbp064.kommentar k where k.id in (select s.kommentar from dbp064.schreibt s where s.projekt = ?)";
    private final String DELETE_FUNDS = "delete from dbp064.spenden s where s.projekt=?";
    private final String REFUND = "update konto k set k.guthaben = k.guthaben + ? where k.inhaber = ?";

    private final String SET_CLOSED = "update projekt p set p.status = 'geschlossen' where p.kennung = ?";

    public ProjektStore() throws StoreException
    {
        super();
    }

    public Projekt getProjectForViewProject(int kennung) throws StoreException
    {

        Projekt projekt = null;
        try(
            PreparedStatement preparedStatement = connection.prepareStatement(PROJECTS_FOR_VIEW_PROJECT_QUERY);
            PreparedStatement preparedStatement2 = connection.prepareStatement(GET_COMMENTS_QUERY);
            PreparedStatement preparedStatement3 = connection.prepareStatement(GET_FUNDS_QUERY))
        {
            preparedStatement.setInt(1, kennung);
            preparedStatement2.setInt(1, kennung);
            preparedStatement3.setInt(1, kennung);
            try(
                ResultSet rs = preparedStatement.executeQuery();
                ResultSet rs2 = preparedStatement2.executeQuery();
                ResultSet rs3 = preparedStatement3.executeQuery())
            {
                if(rs.next())
                {
                    projekt = new Projekt();
                    projekt.setKennung(kennung);
                    projekt.setKategorie(new Kategorie(0, null, rs.getString(1)));
                    projekt.setTitel(rs.getString(2));
                    projekt.setErsteller(rs.getString(3));
                    projekt.setBeschreibung(rs.getString(4));
                    projekt.setFinanzierungslimit(rs.getDouble(5));
                    projekt.setSpendenmenge(rs.getDouble(6));
                    projekt.setStatus(rs.getString(7));
                    projekt.setFkVorgaenger(rs.getInt(8));
                    while(rs2.next())
                    {
                        Kommentar kommentar = new Kommentar();
                        kommentar.setBenutzer(rs2.getString(3).equals("oeffentlich") ? rs2.getString(1) : "Anonym");
                        kommentar.setNutzer(rs2.getString(1));
                        kommentar.setKommentar(rs2.getString(2));
                        projekt.getKommentare().add(kommentar);
                    }

                    while(rs3.next())
                    {
                        Spende spende = new Spende();
                        spende.setSpender(rs3.getString(3).equals("oeffentlich") ? rs3.getString(1) : "Anonym");
                        spende.setNutzer(rs3.getString(1));
                        spende.setSpendenbetrag(rs3.getDouble(2));
                        projekt.getSpenden().add(spende);
                    }
                }

            }

        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
        return projekt;
    }

    public Map<String, List<Projekt>> getProjectsForMain() throws StoreException
    {
        Map<String, List<Projekt>> returnMap = new HashMap<>();
        List<Projekt> openProjects = new ArrayList<>();
        List<Projekt> closedProjects = new ArrayList<>();
        returnMap.put("offen", openProjects);
        returnMap.put("geschlossen", closedProjects);
        try(
            PreparedStatement preparedStatement = connection.prepareStatement(OPEN_PROJECTS_FOR_MAIN_QUERY);
            PreparedStatement preparedStatement2 = connection.prepareStatement(CLOSED_PROJECTS_FOR_MAIN_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSet resultSet2 = preparedStatement2.executeQuery())
        {
            while(resultSet.next())
            {
                Projekt p = new Projekt();
                p.setTitel(resultSet.getString(1));
                p.setErsteller(resultSet.getString(2));
                p.setSpendenmenge(resultSet.getDouble(3));
                p.setKategorie(new Kategorie(-1, null, resultSet.getString(4)));
                p.setKennung(resultSet.getInt(5));
                openProjects.add(p);
            }

            while(resultSet2.next())
            {
                Projekt p = new Projekt();
                p.setTitel(resultSet2.getString(1));
                p.setErsteller(resultSet2.getString(2));
                p.setSpendenmenge(resultSet2.getDouble(3));
                p.setKategorie(new Kategorie(-1, null, resultSet2.getString(4)));
                p.setKennung(resultSet2.getInt(5));
                closedProjects.add(p);
            }

        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
        return returnMap;
    }

    public void deleteProject(Projekt projekt)
    {
        try(
            PreparedStatement refund = connection.prepareStatement(REFUND);
            PreparedStatement deleteComments = connection.prepareStatement(DELETE_COMMENTS);
            PreparedStatement deleteFunds = connection.prepareStatement(DELETE_FUNDS);
            PreparedStatement deleteProject = connection.prepareStatement(DELETE_PROJECT))
        {
            for(Spende s : projekt.getSpenden())
            {
                refund.setDouble(1, s.getSpendenbetrag());
                refund.setString(2, s.getNutzer());
                refund.executeUpdate();
            }
            deleteComments.setInt(1, projekt.getKennung());
            deleteFunds.setInt(1, projekt.getKennung());
            deleteProject.setInt(1, projekt.getKennung());
            deleteComments.executeUpdate();
            deleteFunds.executeUpdate();
            deleteProject.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
    }

    public void addComment(Kommentar kommentar, int projektKennung)
    {
        Random rand = new Random();
        try(
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COMMENT);
            PreparedStatement preparedStatement2 = connection.prepareStatement(INSERT_WRITES))
        {
            int id = rand.nextInt(20000);
            preparedStatement.setString(1, kommentar.getKommentar());
            preparedStatement.setString(2, (kommentar.isSichtbar() ? "oeffentlich" : "privat"));
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
            preparedStatement2.setString(1, kommentar.getBenutzer());
            preparedStatement2.setInt(2, projektKennung);
            preparedStatement2.setInt(3, id);
            preparedStatement2.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
    }

    public void writesComment(Kommentar kommentar, int projektKennung)
    {
        try(PreparedStatement preparedStatement = connection.prepareStatement(INSERT_WRITES))
        {
            preparedStatement.setString(1, kommentar.getBenutzer());
            preparedStatement.setInt(2, projektKennung);
            preparedStatement.setInt(3, kommentar.getId());
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }

    }

    public List<Kommentar> getComments(Projekt projekt)
    {
        List<Kommentar> kommentare = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(GET_COMMENTS_QUERY))
        {
            preparedStatement.setInt(1, projekt.getKennung());
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                if(resultSet.next())
                {
                    kommentare.add(
                            new Kommentar(
                                    resultSet.getString(1),
                                    resultSet.getString(2),
                                    resultSet.getString(3).equalsIgnoreCase("oeffentlich")));
                }
            }
        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
        return kommentare;
    }

    public void addProject(Projekt projekt)
    {
        try(PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJECT))
        {
            preparedStatement.setString(1, projekt.getTitel());
            preparedStatement.setString(2, projekt.getBeschreibung());
            preparedStatement.setDouble(3, projekt.getFinanzierungslimit());
            preparedStatement.setString(4, projekt.getErsteller());
            if(projekt.getFkVorgaenger() == -1)
            {
                preparedStatement.setNull(5, projekt.getFkVorgaenger());
            }
            else
            {
                preparedStatement.setInt(5, projekt.getFkVorgaenger());
            }
            preparedStatement.setInt(6, projekt.getFkKategorie());
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
    }

    public void updateProject(Projekt projekt, kennung)
    {
        try(PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROJECT))
        {
            preparedStatement.setString(1, projekt.getTitel());
            preparedStatement.setString(2, projekt.getBeschreibung());
            preparedStatement.setDouble(3, projekt.getFinanzierungslimit());
            if(projekt.getFkVorgaenger() == -1)
            {
                preparedStatement.setNull(4, projekt.getFkVorgaenger());
            }
            else
            {
                preparedStatement.setInt(4, projekt.getFkVorgaenger());
            }
            preparedStatement.setInt(5, projekt.getFkKategorie());
            preparedStatement.setInt(6, kennung);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
    }

    public void setClosed(int kennung)
    {
        try(PreparedStatement preparedStatement = connection.prepareStatement(SET_CLOSED))
        {
            preparedStatement.setInt(1, kennung);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
    }

    public Projekt getProject(int kennung) throws StoreException
    {

        Projekt projekt = null;
        try(PreparedStatement preparedStatement = connection.prepareStatement(PROJECT_FROM_ID_QUERY))
        {
            preparedStatement.setInt(1, kennung);
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                if(resultSet.next())
                {
                    projekt = resultSetToProjekt(resultSet);
                }
            }

        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
        return projekt;
    }

    public List<Projekt> getProjectsFromCreator(String creator) throws StoreException
    {

        ArrayList<Projekt> returnList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(PROJECT_FROM_CREATOR_QUERY))
        {
            preparedStatement.setString(1, creator);
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    returnList.add(resultSetToProjekt(resultSet));
                }
            }

        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
        return returnList;
    }

    private Projekt resultSetToProjekt(ResultSet rs) throws SQLException
    {
        Projekt projekt = new Projekt();
        projekt.setKennung(rs.getInt("kennung"));
        projekt.setTitel(rs.getString("titel"));
        projekt.setBeschreibung(rs.getString("beschreibung"));
        projekt.setStatus(rs.getString("status"));
        projekt.setFinanzierungslimit(rs.getDouble("finanzierungslimit"));
        projekt.setErsteller(rs.getString("ersteller"));
        projekt.setFkVorgaenger(rs.getInt("vorgaenger"));
        projekt.setFkKategorie(rs.getInt("kategorie"));
        return projekt;
    }

}
