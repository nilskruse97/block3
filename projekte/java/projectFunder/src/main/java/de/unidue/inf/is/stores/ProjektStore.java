package de.unidue.inf.is.stores;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.unidue.inf.is.domain.Kommentar;
import de.unidue.inf.is.domain.Projekt;

public class ProjektStore extends AbstractStore
{

    private final String OPEN_PROJECT_QUERY = "select * from dbp064.projekt where status='offen'";
    private final String CLOSED_PROJECT_QUERY = "select * from dbp064.projekt where status='geschlossen'";
    private final String PROJECT_FROM_CREATOR_QUERY = "select * from dbp064.projekt where ersteller=(?)";
    private final String INSERT_PROJECT = "INSERT INTO dbp064.projekt (titel, beschreibung, finanzierungslimit, ersteller, vorgaenger, kategorie) VALUES (?,?,?,?,?,?)";
    private final String PROJECT_FROM_ID_QUERY = "select * from dbp064.projekt where kennung=(?)";
    private final String PROJECT_WITH_CATEGORY = "select * from dbp064.projekt";
    private final String GET_COMMENTS_QUERY = "select dbp064.schreibt.benutzer, dbp064.kommentar.text, dbp064.kommentar.sichtbarkeit from dbp064.kommentar join dbp064.schreibt on dbp064.schreibt.kommentar = dbp064.kommentar.id  where dbp064.schreibt.projekt=?";

    public ProjektStore() throws StoreException
    {
        super();
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
                    System.out.println(resultSet.getString(2));
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

    public List<Projekt> getOpenProjects() throws StoreException
    {

        ArrayList<Projekt> returnList = new ArrayList<>();
        try(
            PreparedStatement preparedStatement = connection.prepareStatement(OPEN_PROJECT_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery())
        {
            while(resultSet.next())
            {
                returnList.add(resultSetToProjekt(resultSet));
            }

        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
        return returnList;
    }

    public List<Projekt> getClosedProjects() throws StoreException
    {
        ArrayList<Projekt> returnList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(CLOSED_PROJECT_QUERY);)
        {
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
