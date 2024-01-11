package uminho.dss.esideal.data;

import uminho.dss.esideal.business.employee.Employee;
import uminho.dss.esideal.business.employee.Mechanic;
import uminho.dss.esideal.business.service.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class EmployeeDAO {
    private static EmployeeDAO singleton = null;

    private EmployeeDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS employee (" +
                    "Id INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                    "Name VARCHAR(100) NOT NULL," + 
                    "Password VARCHAR(100) NOT NULL," +
                    "Type VARCHAR(100) NOT NULL)";
            stm.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS mechanic_skill (" +
                    "mechanic INT NOT NULL," +
                    "type VARCHAR(30) NOT NULL," +
                    "PRIMARY KEY (mechanic, type)," +
                    "FOREIGN KEY (mechanic) REFERENCES employee(Id) ON DELETE CASCADE)";
            stm.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS mechanic_shift (" + 
                    "mechanic_id INT NOT NULL," +
                    "shift_start TIMESTAMP NULL," +
                    "shift_end TIMESTAMP NULL," +
                    "PRIMARY KEY (mechanic_id)," +
                    "FOREIGN KEY (mechanic_id) REFERENCES employee(Id) ON DELETE CASCADE)";
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static EmployeeDAO getInstance() {
        if (EmployeeDAO.singleton == null) {
            EmployeeDAO.singleton = new EmployeeDAO();
        }
        return EmployeeDAO.singleton;
    }

    public int getCount() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM employee")) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return 0;
    }

    public boolean isEmpty() {
        return this.getCount() == 0;
    }

    public boolean existsById(int employeeId) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM employee WHERE Id = ?")) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public Employee getById(int employeeId) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM employee WHERE Id = ?")) {
            ps.setInt(1, employeeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String employeeType = rs.getString("Type"); // Assuming you have a 'Type' column
                    if ("M".equals(employeeType)) {
                        Mechanic mechanic = new Mechanic(rs.getInt("Id"), rs.getString("Name"), rs.getString("Password"));
                        Collection<Service.Type> skillset = new ArrayList<>();
                        try(PreparedStatement psa = conn.prepareStatement("SELECT type FROM mechanic_skill WHERE mechanic = ?")) {
                            psa.setInt(1, employeeId);
                            try (ResultSet rsa = psa.executeQuery()) {
                                while (rsa.next()) {
                                    Service.Type type = Service.Type.valueOf(rsa.getString("type"));
                                    skillset.add(type);
                                }
                            } catch (Exception e){
                                e.printStackTrace(); //Handle exception correctly
                            }
                        }
                        mechanic.setSkillset(skillset);
                        return mechanic;
                    } else {
                        Employee employee = new Employee();
                        employee.setId(rs.getInt("Id"));
                        employee.setName(rs.getString("Name"));
                        employee.setPassword(rs.getString("Password"));
                        return employee;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return null;
    }


    public void addOrUpdate(Employee employee) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            // Update the basic employee information
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO employee (Id, Name, Password, Type) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE Name=VALUES(Name), Password=VALUES(Password), Type=VALUES(Type)")) {
                ps.setInt(1, employee.getId());
                ps.setString(2, employee.getName());
                ps.setString(3, employee.getPassword());
                ps.setString(4, employee instanceof Mechanic ? "Mechanic" : "Employee");
                ps.executeUpdate();
            }
            // Update skillset if the employee is a mechanic
            if (employee instanceof Mechanic) {
                Mechanic mechanic = (Mechanic) employee;
                // Delete existing skills
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM mechanic_skill WHERE mechanic = ?")) {
                    ps.setInt(1, mechanic.getId());
                    ps.executeUpdate();
                }
                // Insert new skills
                String insertSkill = "INSERT INTO mechanic_skill (mechanic, type) VALUES (?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertSkill)) {
                    for (Service.Type skill : mechanic.getSkillset()) {
                        ps.setInt(1, mechanic.getId());
                        ps.setString(2, skill.name());
                        ps.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    

    public void addEmployee (String name, String password, String type) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO employee (Name, Password, Type) VALUES (?, ?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, password);
            ps.setString(3, type);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
    
    public void addEmployee (Integer id, String name, String password, Collection<Service.Type> skillset, String type) {
        this.addEmployee(name, password, type);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)){
            String insertSkills = "INSERT INTO mechanic_skill (mechanic, type) VALUES (?, ?)";
            try (PreparedStatement psa = conn.prepareStatement(insertSkills)) {
                for (Service.Type t : skillset) {
                    psa.setInt(1, id);            
                    psa.setString(2, t.name());   
                    psa.addBatch();
                }
                psa.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Employee removeById(int employeeId) {
        Employee employee = getById(employeeId);
        if (employee != null) {
            try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                PreparedStatement ps = conn.prepareStatement("DELETE FROM employee WHERE Id = ?")) {
                ps.setInt(1, employeeId);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new NullPointerException(e.getMessage());
            }
        }
        return employee;
    }

    public Collection<Employee> getAll() {
        Collection<Employee> employees = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM employee")) {
            while (rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("Name");
                String password = rs.getString("Password");
                String employeeType = rs.getString("Type");
    
                if ("Mechanic".equals(employeeType)) {
                    Mechanic mechanic = new Mechanic(id, name, password);
                    Collection<Service.Type> skillset = new ArrayList<>();
                    try (PreparedStatement ps = conn.prepareStatement("SELECT type FROM mechanic_skill WHERE mechanic = ?")) {
                        ps.setInt(1, id);
                        try (ResultSet rsa = ps.executeQuery()) {
                            while (rsa.next()) {
                                skillset.add(Service.Type.valueOf(rsa.getString("type")));
                            }
                        }
                    }
                    mechanic.setSkillset(skillset);
                    employees.add(mechanic);
                } else {
                    employees.add(new Employee(id, name, password));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return employees;
    }
    
    

    public Map<String, Object> getLeastOccupiedMechanic(Collection<Integer> mechanic) {
        int id = 0;
        Time time = null;
        Map<String, Object> res = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            String sql = "SELECT e.Id AS id, s.EndTime as time FROM employee e JOIN services s ON e.Id = s.MechanicId WHERE e.Id IN (" +
                            String.join(",", mechanic.stream().map(Object::toString).toArray(String[]::new)) +
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
            for(Integer i : mechanic){
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


    public int nextEmployeeID () {
        int id = 0;
        try(Connection connection = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement s = connection.createStatement()) {
                String sql = "SELECT MAX(Id) AS next_id FROM employee";
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

    public String authenticate (int id, String password, String type)
    {
        String employee = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            PreparedStatement stm = conn.prepareStatement("SELECT name FROM employee WHERE id=? AND password=? AND Type=?"))
        {
            stm.setInt(1, id);
            stm.setString(2, password);
            stm.setString(3, type);
            try (ResultSet rs = stm.executeQuery())
            {
                if(rs.next()) employee = rs.getString("name");
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return employee;
    }
    


    public void mechanicStartShift(int mechanicId) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            // Insert or update the shift start time
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO mechanic_shift (mechanic_id, shift_start) VALUES (?, NOW()) ON DUPLICATE KEY UPDATE shift_start = NOW()")) {
                ps.setInt(1, mechanicId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error starting mechanic shift: " + e.getMessage());
        }
    }
   


    public void mechanicEndShift(int mechanicId) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            // Update the shift end time
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE mechanic_shift SET shift_end = NOW() WHERE mechanic_id = ?")) {
                ps.setInt(1, mechanicId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error ending mechanic shift: " + e.getMessage());
        }
    }

    public void mechanicStartService(int service_id){
        ServiceDAO serviceDao = ServiceDAO.getInstance();
        Service service = serviceDao.getById(service_id);

        if (service != null && service.getStatus() != Service.Status.FINISHED) {
            service.setStatus(Service.Status.STARTED);
            service.setStart(LocalDateTime.now()); // Assuming you store the start time
            serviceDao.addOrUpdate(service);
        } else {
            throw new IllegalStateException("Service cannot be started");
        }
    }

    public void mechanicEndService(int service_id) {
        ServiceDAO serviceDao = ServiceDAO.getInstance();
        Service service = serviceDao.getById(service_id);

        if (service != null && service.getStatus() == Service.Status.STARTED) {
            service.setStatus(Service.Status.FINISHED);
            service.setEnd(LocalDateTime.now()); // Assuming you store the end time
            serviceDao.addOrUpdate(service);
        } else {
            throw new IllegalStateException("Service cannot be ended");
        }
    }

    
    public Collection<Integer> getMechanicsAvailableId (Service.Type type, Time last_possible_start_time)
    {
        Collection<Integer> workstations_id= new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = connection.prepareStatement("SELECT e.Id " +
                                                                "FROM employee e " +
                                                                "LEFT JOIN services s ON e.Id = s.MechanicId " +
                                                                "WHERE e.Id IN (" +
                                                                "    SELECT e.Id " +
                                                                "    FROM mechanic_skill skill" +
                                                                "    WHERE skill.type = ? " +
                                                                "    AND NOT EXISTS (" +
                                                                "        SELECT 1 " +
                                                                "        FROM services s2" +
                                                                "        WHERE s2.EndTime > ?" +
                                                                "    ) OR s.MechanicId IS NULL" +
                                                                ") AND e.Type = 'M'")) 
        {
            ps.setString(1, type.name());
            ps.setString(2, last_possible_start_time.toString());
            try (ResultSet rsa = ps.executeQuery()) 
            {
                while (rsa.next()) 
                {
                    System.out.println(rsa.getInt("Id"));
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
}