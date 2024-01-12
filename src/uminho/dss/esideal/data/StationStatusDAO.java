package uminho.dss.esideal.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import uminho.dss.esideal.business.service.Service;

public class StationStatusDAO 
{
    private static StationStatusDAO singleton = null;
    
    
    private StationStatusDAO() 
    {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
        Statement stm = conn.createStatement()) 
        {
            String sql = "CREATE TABLE IF NOT EXISTS CurrentTime (" +
            "Id INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
            "Time TIME)";
            stm.executeUpdate(sql);
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
    
    public static StationStatusDAO getInstance ()
    {
        if (singleton== null)
            singleton= new StationStatusDAO();
        return singleton;
    }

    public void setTime(Time time) 
    {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                PreparedStatement ps = conn.prepareStatement("UPDATE CurrentTime SET Time = ? WHERE Id = 1")) 
        {
            ps.setTime(1, time);
            ps.executeUpdate();
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public void initialTime(Time time) 
    {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                PreparedStatement ps = conn.prepareStatement("INSERT INTO CurrentTime (Time) VALUES (?)")) 
        {
            ps.setTime(1, time);
            ps.executeUpdate();
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public Time getTime() 
    {
        Time t = null;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                PreparedStatement ps = conn.prepareStatement("SELECT Time FROM CurrentTime WHERE Id = 1")) 
        {
            try (ResultSet rs = ps.executeQuery()) 
            {
                if (rs.next()) 
                {
                    t = rs.getTime("Time");
                }
            }
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }
}
