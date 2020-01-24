package de.unidue.inf.is.stores;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import de.unidue.inf.is.utils.DBUtil;

public abstract class AbstractStore implements Closeable
{
    protected Connection connection;
    private boolean complete;

    public AbstractStore() throws StoreException
    {
        try
        {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
        }
        catch(SQLException e)
        {
            throw new StoreException(e);
        }
    }

    public void complete()
    {
        complete = true;
    }

    @Override
    public void close() throws IOException
    {
        if(connection != null)
        {
            try
            {
                if(complete)
                {
                    connection.commit();
                }
                else
                {
                    connection.rollback();
                }
            }
            catch(SQLException e)
            {
                throw new StoreException(e);
            }
            finally
            {
                try
                {
                    connection.close();
                }
                catch(SQLException e)
                {
                    throw new StoreException(e);
                }
            }
        }
    }
}
