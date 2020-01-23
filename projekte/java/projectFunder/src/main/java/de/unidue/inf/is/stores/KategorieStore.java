package de.unidue.inf.is.stores;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import de.unidue.inf.is.domain.Kategorie;
import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.utils.DBUtil;

public class KategorieStore implements Closeable {
    private final String GET_ALL_QUERY = "select * from dbp064.kategorie";
    private Connection connection;
    
    public KategorieStore() throws StoreException {
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new StoreException(e);
        }
    }
    
    public List<Kategorie> getAll() throws StoreException {

        ArrayList<Kategorie> returnList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_QUERY);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                returnList.add(new Kategorie(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3)));
            }

        } catch (SQLException e) {
            throw new StoreException(e);
        }
        return returnList;
    }
    

    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub
        
    }

}
