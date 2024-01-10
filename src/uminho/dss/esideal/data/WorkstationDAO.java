package uminho.dss.esideal.data;

import uminho.dss.esideal.business.service.Service;
import uminho.dss.esideal.business.service.Service.Type;
import uminho.dss.esideal.business.workstation.Workstation;
import uminho.dss.esideal.data.Exceptions.MissingEmployeeException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class WorkstationDAO {
    private static WorkstationDAO singleton = null;

    private WorkstationDAO ()
    {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS workstation (" +
                    "Id INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                    "Name VARCHAR(100) NOT NULL)";
            stm.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS workstation_skill (" +
                "workstation INT NOT NULL," +
                "type VARCHAR(30) NOT NULL," +
                "PRIMARY KEY (workstation, type)," +
                "FOREIGN KEY (workstation) REFERENCES workstation(Id) ON DELETE CASCADE)";
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static WorkstationDAO getInstance ()
    {
        if (WorkstationDAO.singleton == null)
            WorkstationDAO.singleton = new WorkstationDAO();
        return WorkstationDAO.singleton;
    }

    public Workstation getWorkstationById(int id) {
        Workstation workstation = new Workstation();
        try (Connection connection = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM workstation WHERE id = ?")) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    workstation.setId(rs.getInt("id"));
                    Collection<Service.Type> skillset = new ArrayList<>();
                    try(PreparedStatement psa = connection.prepareStatement("SELECT type FROM workstation_skill WHERE workstation = ?")) {
                        psa.setInt(1, id);
                        try (ResultSet rsa = psa.executeQuery()) {
                            while (rsa.next()) {
                                Service.Type type = Service.Type.valueOf(rsa.getString("type"));
                                skillset.add(type);
                            }
                        } catch (Exception e){
                            e.printStackTrace(); //Handle exception correctly
                        }
                    }
                    workstation.setSkillset(skillset);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
        return workstation;
    }

    public void addWorkstation (Workstation workstation) {
        int id = workstation.getId();
        try(Connection connection = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            String name = workstation.getName();
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO workstation (name) VALUES (?)")) {
                ps.setString(1, name);
                ps.executeUpdate();
            }
            Collection<Service.Type> types = workstation.getSkillset();
            String insertSkills = "INSERT INTO workstation_skill (workstation, type) VALUES (?, ?)";
            try (PreparedStatement psa = connection.prepareStatement(insertSkills)) {
                for (Service.Type t : types) {
                    psa.setInt(1, id);            // Assuming id is an int variable
                    psa.setString(2, t.name());   // Assuming type.name() returns a String
                    psa.addBatch();
                }
                psa.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int nextWorkstationID () {
        int id = 0;
        try(Connection connection = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement s = connection.createStatement()) {
                String sql = "SELECT MAX(Id) AS next_id FROM workstation";
                try(ResultSet rs = s.executeQuery(sql)) {
                    if (rs.next()) {
                        id = rs.getInt("next_id");
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id + 1;
    }

    public Collection<Service.Type> getSkillset (int id) throws Exception{
        Collection<Service.Type> skillset = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement("SELECT type FROM workstation_skill WHERE workstation = ?")) {

            ps.setInt(1, id);
            try (ResultSet rsa = ps.executeQuery()) 
            {
                while (rsa.next()) {
                    Service.Type type = Service.Type.valueOf(rsa.getString("type"));
                    skillset.add(type);
                }
                if (skillset.isEmpty()) throw new Exception ("No workstation with id "+ id);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
        return skillset;
    }

    public Map<String, Object> getLeastOccupiedWorkstation(Collection<Integer> workstations) {
        int id = 0;
        Time time = null;
        Map<String, Object> res= new HashMap<>();
        try (Connection connection = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            String sql = "SELECT w.Id AS id, s.EndTime AS time FROM workstation w JOIN services s ON w.Id = s.WorkstationId WHERE w.Id IN (" +
                            String.join(",", workstations.stream().map(Object::toString).toArray(String[]::new)) +
                            ") ORDER BY s.EndTime LIMIT 1;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    id = resultSet.getInt("id");
                    time = resultSet.getTime("time");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (id == 0)
        {
            for(Integer i : workstations){
                res.put("id", i);
                break;
            }
        } else
            res.put("id", id);
        if (time == null)
            res.put("time", LocalTime.MIN);
        else
            res.put("time", time.toLocalTime());

        return res;
    }

    public Collection<Integer> getWorkstationAvailable(Service.Type type, Time last_possible_start_time)
    {
        Collection<Integer> workstations_id= new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement("SELECT w.Id "+
                                                                "FROM workstation w "+
                                                                "LEFT JOIN services s ON w.Id = s.WorkstationId "+
                                                                "WHERE w.Id IN ("+
                                                                "    SELECT w.Id "+
                                                                "    FROM workstation_skill skill"+
                                                                "    WHERE skill.type = ? "+
                                                                "    AND NOT EXISTS ("+
                                                                "        SELECT 1 "+
                                                                "        FROM services s2 "+
                                                                "        WHERE s2.WorkstationId = w.Id "+
                                                                "        AND s2.EndTime > ?"+
                                                                "    )"+
                                                                "OR s.WorkstationId IS NULL)")) 
        {
            ps.setString(1, type.name());
            ps.setString(2, last_possible_start_time.toString());
            try (ResultSet rsa = ps.executeQuery()) 
            {
                while (rsa.next()) 
                {
                    workstations_id.add(rsa.getInt("Id"));
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace(); // Handle or log the exception appropriately
        }

        return workstations_id;
    }

    public Collection<Workstation> getWorkstations() throws Exception
    {
        Collection<Workstation> workstations= new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM workstation"))
        {
            try (ResultSet rsa = ps.executeQuery()) 
            {
                Workstation w;
                
                while (rsa.next()) 
                {
                    int id= rsa.getInt("Id");
                    String name= rsa.getString("Name");
                    Collection<Service.Type> skillset= getSkillset(id);
                    w= new Workstation(id, name, skillset);
                    workstations.add(w);
                }
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace(); // Handle or log the exception appropriately
        }

        return workstations;
    }
}
