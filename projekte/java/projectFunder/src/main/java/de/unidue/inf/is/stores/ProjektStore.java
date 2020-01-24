package de.unidue.inf.is.stores;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.unidue.inf.is.domain.Projekt;

public class ProjektStore extends AbstractStore
{

    private final String OPEN_PROJECT_QUERY = "select * from dbp064.projekt where status='offen'";
    private final String CLOSED_PROJECT_QUERY = "select * from dbp064.projekt where status='geschlossen'";
    private final String PROJECT_FROM_CREATOR_QUERY = "select * from dbp064.projekt where ersteller=(?)";
    private final String INSERT_PROJECT = "INSERT INTO projekt (titel, beschreibung, finanzierungslimit, ersteller, vorgaenger, kategorie) VALUES (?,?,?,?,?,?)";

    public ProjektStore() throws StoreException
    {
        super();
    }

    public void addProject(Projekt projekt)
    {
        try(PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJECT))
        {
            preparedStatement.setString(1, projekt.getTitel());
            preparedStatement.setString(2, projekt.getBeschreibung());
            preparedStatement.setDouble(3, projekt.getFinanzierungslimit());
            preparedStatement.setString(4, projekt.getErsteller());
            preparedStatement.setInt(5, projekt.getVorgaenger());
            preparedStatement.setInt(6, projekt.getKategorie());
            preparedStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
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
        projekt.setVorgaenger(rs.getInt("vorgaenger"));
        projekt.setKategorie(rs.getInt("kategorie"));
        return projekt;
    }

}