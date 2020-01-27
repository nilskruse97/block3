package de.unidue.inf.is.stores;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.unidue.inf.is.domain.Benutzer;
import de.unidue.inf.is.domain.Projekt;

public class BenutzerStore extends AbstractStore
{
    private final String GET_USER_QUERY = "select * from dbp064.benutzer where email = ?";
    private final String GET_ALL_SUPPORTED_PROJECTS_QUERY = "select p.kennung, p.titel, s.sichtbarkeit from dbp064.projekt p, dbp064.spenden s where p.kennung = s.projekt and s.spender = ?";
    private final String GET_SUPPORTED_PROJECTS_QUERY = "select p.kennung, p.titel, s.sichtbarkeit from dbp064.projekt p, dbp064.spenden s where p.kennung = s.projekt and s.spender = ? and s.sichtbarkeit = 'oeffentlich'";
    private final String GET_PROJECTS_USER_QUERY = "select p.kennung, p.titel from dbp064.projekt p where p.ersteller = ?";

    public List<Projekt> getProjectsFromCreator(String creator) throws StoreException
    {

        ArrayList<Projekt> returnList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(GET_PROJECTS_USER_QUERY))
        {
            preparedStatement.setString(1, creator);
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    Projekt p = new Projekt();
                    p.setKennung(resultSet.getInt(1));
                    p.setTitel(resultSet.getString(2));
                    returnList.add(p);
                }
            }

        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
        return returnList;
    }

    public List<Projekt> getProjectsFunded(String creator) throws StoreException
    {

        ArrayList<Projekt> returnList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(GET_SUPPORTED_PROJECTS_QUERY))
        {
            preparedStatement.setString(1, creator);
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    Projekt p = new Projekt();
                    p.setKennung(resultSet.getInt(1));
                    p.setTitel(resultSet.getString(2));
                    returnList.add(p);
                }
            }

        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
        return returnList;
    }

    public List<Projekt> getAllProjectsFunded(String creator) throws StoreException
    {

        ArrayList<Projekt> returnList = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_SUPPORTED_PROJECTS_QUERY))
        {
            preparedStatement.setString(1, creator);
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    Projekt p = new Projekt();
                    p.setKennung(resultSet.getInt(1));
                    p.setTitel(resultSet.getString(2));
                    returnList.add(p);
                }
            }

        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
        return returnList;
    }

    public Benutzer getUser(String email)
    {
        try(PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_QUERY))
        {
            preparedStatement.setString(1, email);
            try(ResultSet resultSet = preparedStatement.executeQuery())
            {
                if(resultSet.next())
                {
                    Benutzer b = new Benutzer();
                    b.setEmail(resultSet.getString(1));
                    b.setName(resultSet.getString(2));
                    b.setBeschreibung(resultSet.getString(3));
                    return b;
                }
            }

        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
        return null;
    }

}
