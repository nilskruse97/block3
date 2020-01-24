package de.unidue.inf.is.stores;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.unidue.inf.is.domain.Kategorie;

public class KategorieStore extends AbstractStore
{
    private final String GET_ALL_QUERY = "select * from dbp064.kategorie";

    public KategorieStore() throws StoreException
    {
        super();
    }

    public List<Kategorie> getAll() throws StoreException
    {

        ArrayList<Kategorie> returnList = new ArrayList<>();
        try(
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery())
        {
            while(resultSet.next())
            {
                returnList.add(new Kategorie(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
            }

        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
        return returnList;
    }

}
