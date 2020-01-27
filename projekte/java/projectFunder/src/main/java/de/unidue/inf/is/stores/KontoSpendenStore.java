package de.unidue.inf.is.stores;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KontoSpendenStore extends AbstractStore
{
    private final String GET_BALANCE_QUERY = "select k.guthaben from dbp064.konto k where k.inhaber=?";
    private final String GET_FUND_QUERY = "select * from dbp064.spenden s where s.spender=? and s.projekt=?";
    private final String INSERT_FUND = "insert into dbp064.spenden (spender,projekt,spendenbetrag,sichtbarkeit) VALUES(?,?,?,?)";
    private final String UPDATE_BALANCE = "update dbp064.konto k set k.guthaben = k.guthaben - ? where k.inhaber = ?";

    public double getBalance(String inhaber) throws StoreException
    {
        try(PreparedStatement preparedStatement = connection.prepareStatement(GET_BALANCE_QUERY))
        {
            preparedStatement.setString(1, inhaber);
            try(ResultSet rs = preparedStatement.executeQuery())
            {
                if(rs.next())
                {
                    return rs.getDouble(1);
                }
            }
        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
        return -1;
    }

    public boolean fundExists(String spender, int projekt) throws StoreException
    {
        try(PreparedStatement preparedStatement = connection.prepareStatement(GET_FUND_QUERY))
        {
            preparedStatement.setString(1, spender);
            preparedStatement.setInt(2, projekt);
            try(ResultSet rs = preparedStatement.executeQuery())
            {
                if(rs.next())
                {
                    return true;
                }
            }
        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
        return false;
    }

    public void addFund(String spender, int projekt, double spendenbetrag, String sichtbarkeit) throws StoreException
    {
        try(
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FUND);
            PreparedStatement preparedStatement2 = connection.prepareStatement(UPDATE_BALANCE))
        {
            preparedStatement.setString(1, spender);
            preparedStatement.setInt(2, projekt);
            preparedStatement.setDouble(3, spendenbetrag);
            System.out.println(sichtbarkeit);
            preparedStatement.setString(4, sichtbarkeit);

            preparedStatement2.setDouble(1, spendenbetrag);
            preparedStatement2.setString(2, spender);
            preparedStatement.executeUpdate();
            preparedStatement2.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
    }
}
