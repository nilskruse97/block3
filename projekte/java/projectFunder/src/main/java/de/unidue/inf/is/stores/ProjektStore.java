package de.unidue.inf.is.stores;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.unidue.inf.is.domain.Projekt;
import de.unidue.inf.is.utils.DBUtil;

public class ProjektStore implements Closeable
{
	private Connection connection;
    private boolean complete;
    
    private final String OPEN_PROJECT_QUERY = "select * from dbp064.projekt where status='offen'";
    private final String CLOSED_PROJECT_QUERY = "select * from dbp064.projekt where status='geschlossen'";
	public ProjektStore() throws StoreException {
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }
	public ArrayList<Projekt> getOpenProjects() throws StoreException
	{
		ArrayList<Projekt> returnList = new ArrayList<>();
		try(PreparedStatement preparedStatement = connection
                .prepareStatement(OPEN_PROJECT_QUERY);) {
            
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) 
            {
            	returnList.add(resultSetToProjekt(resultSet));
            }
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
		return returnList;
	}
	
	public ArrayList<Projekt> getClosedProjects() throws StoreException
	{
		ArrayList<Projekt> returnList = new ArrayList<>();
		try(PreparedStatement preparedStatement = connection
                .prepareStatement(CLOSED_PROJECT_QUERY);) {
            
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) 
            {
            	returnList.add(resultSetToProjekt(resultSet));
            }
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
		return returnList;
	}
	private Projekt resultSetToProjekt(ResultSet rs) throws SQLException {
		Projekt projekt = new Projekt();
		projekt.setKennung(rs.getInt(1));
		projekt.setTitel(rs.getString(2));
		projekt.setBeschreibung(rs.getString(3));
		projekt.setStatus(rs.getString(4));
		projekt.setFinanzierungslimit(rs.getDouble(5));
		projekt.setErsteller(rs.getString(6));
		projekt.setVorgaenger(rs.getInt(7));
		projekt.setKategorie(rs.getInt(8));
		return projekt;
	}
	@Override
	public void close() throws IOException
	{
		// TODO Auto-generated method stub
		
	}

}
