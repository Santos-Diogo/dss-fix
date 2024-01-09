package uminho.dss.esideal.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import uminho.dss.esideal.business.service.Service;
import uminho.dss.esideal.business.service.Service.Status;

public class ServiceDAO {
    private static ServiceDAO singleton = null;

    private ServiceDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS services (" +
            "Id INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
            "StartTime TIME NOT NULL," +
            "EndTime TIME NOT NULL," +
            "Type VARCHAR(20) NOT NULL," +
            "WorkstationId INT," +
            "MechanicId INT," +
            "VehicleId VARCHAR(8)," +
            "Status VARCHAR(20) NOT NULL," +
            "FOREIGN KEY (WorkstationId) REFERENCES workstation(Id)," +
            "FOREIGN KEY (VehicleId) REFERENCES vehicles(Id),"+
            "FOREIGN KEY (MechanicId) REFERENCES employee(Id))";
            stm.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static ServiceDAO getInstance() {
        if (ServiceDAO.singleton == null) {
            ServiceDAO.singleton = new ServiceDAO();
        }
        return ServiceDAO.singleton;
    }

    public int getCount() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM services")) {
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

    public boolean existsById(int serviceId) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM services WHERE Id = ?")) {
            ps.setInt(1, serviceId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public Service getById(int serviceId) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM services WHERE Id = ?")) {
            ps.setInt(1, serviceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Service service = new Service();
                    service.setId(rs.getInt("Id"));
                    service.setStart(rs.getTime("StartTime").toLocalTime());
                    service.setEnd(rs.getTime("EndTime").toLocalTime());
                    service.setType(Service.Type.valueOf(rs.getString("Type")));
                    service.setWorkstationId(rs.getInt("WorkstationId"));
                    service.setVehicleId(rs.getString("VehicleId"));
                    service.setStatus(Service.Status.valueOf(rs.getString("Status"))); // New line for status
                    return service;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return null;
    }

    public void addOrUpdate(Service service) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO services (StartTime, EndTime, Type, WorkstationId, MechanicId, VehicleId, Status) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE StartTime=VALUES(StartTime), EndTime=VALUES(EndTime), Type=VALUES(Type), WorkstationId=VALUES(WorkstationId), VehicleId=VALUES(VehicleId), Status=VALUES(Status)")) {
            ps.setTime(1, Time.valueOf(service.getStart()));
            ps.setTime(2, Time.valueOf(service.getEnd()));
            ps.setString(3, service.getType().name());
            ps.setInt(4, service.getWorkstationId());
            ps.setInt(5, service.getEmployeeId());
            ps.setString(6, service.getVehicleId());
            ps.setString(7, service.getStatus().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public Service removeById(int serviceId) {
        Service service = getById(serviceId);
        if (service != null) {
            try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM services WHERE Id = ?")) {
                ps.setInt(1, serviceId);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new NullPointerException(e.getMessage());
            }
        }
        return service;
    }

    public Collection<Service> getAll() {
        Collection<Service> services = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM services")) {
            while (rs.next()) {
                Service service = new Service();
                service.setId(rs.getInt("Id"));
                service.setStart(rs.getTime("StartTime").toLocalTime());
                service.setEnd(rs.getTime("EndTime").toLocalTime());
                service.setType(Service.Type.valueOf(rs.getString("Type")));
                service.setWorkstationId(rs.getInt("WorkstationId"));
                service.setVehicleId(rs.getString("VehicleId"));
                service.setStatus(Service.Status.valueOf(rs.getString("Status"))); // New line for status
                services.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return services;
    }

    public Collection<Service> getServicesByWorkstationId(int workstationId) {
        Collection<Service> services = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM services WHERE WorkstationId = ?")) {
            ps.setInt(1, workstationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Service service = new Service();
                    service.setId(rs.getInt("Id"));
                    service.setStart(rs.getTime("StartTime").toLocalTime());
                    service.setEnd(rs.getTime("EndTime").toLocalTime());
                    service.setType(Service.Type.valueOf(rs.getString("Type")));
                    service.setWorkstationId(rs.getInt("WorkstationId"));
                    service.setVehicleId(rs.getString("VehicleId"));
                    services.add(service);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return services;
    }    

    public Integer getFirstDueServiceOfDayByWorkstation(LocalTime current, int workstationId, int employeeID) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT Id FROM services WHERE WorkstationId = ? AND MechanidId = ? AND Status = 'DUE' ORDER BY StartTime ASC LIMIT 1")) {

            
            ps.setInt(1, workstationId);
            ps.setInt(2, employeeID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return null;
    }

    /* public Service getFirstStartedServiceOfDayByWorkstation(LocalTime givenDate, int workstationId) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM services WHERE StartTime >= ? AND StartTime < ? AND WorkstationId = ? AND Status = 'STARTED' ORDER BY StartTime ASC LIMIT 1")) {

            Time startDate = Time.valueOf(givenDate.atStartOfDay());
            Time endDate = Time.valueOf(givenDate.plusDays(1).atStartOfDay());

            ps.setTime(1, startDate);
            ps.setTime(2, endDate);
            ps.setInt(3, workstationId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Service service = new Service();
                    service.setId(rs.getInt("Id"));
                    service.setStart(rs.getTime("StartTime").toLocalTime());
                    service.setEnd(rs.getTime("EndTime").toLocalTime());
                    service.setType(Service.Type.valueOf(rs.getString("Type")));
                    service.setWorkstationId(rs.getInt("WorkstationId"));
                    service.setVehicleId(rs.getInt("VehicleId"));
                    service.setStatus(Service.Status.valueOf(rs.getString("Status"))); // New line for status
                    return service;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return null;
    } */

    public boolean hasDueServiceTodayByWorkstation(LocalTime current, int workstationId, int employeeID) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT COUNT(*) FROM services WHERE WorkstationId = ? AND MechanicId = ? AND Status = 'DUE'")) {
    
            ps.setInt(1, workstationId);
            ps.setInt(2, employeeID);
    
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return false;
    }

    public void updateStatusByID (Integer id, Status status) {
        try (Connection connection = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD)) {
            connection.setAutoCommit(false);

            try {
                String updateSql = "UPDATE service SET Status = ? WHERE id = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                    updateStatement.setString(1, status.name());
                    updateStatement.setInt(2, id);

                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        connection.commit();
                    } else {
                        connection.rollback();
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* public boolean hasStartedServiceTodayByWorkstation(LocalTime givenDate, int workstationId) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT COUNT(*) FROM services WHERE StartTime >= ? AND StartTime < ? AND WorkstationId = ? AND Status = 'STARTED'")) {
    
            Time startDate = Time.valueOf(givenDate.atStartOfDay());
            Time endDate = Time.valueOf(givenDate.plusDays(1).atStartOfDay());
    
            ps.setTime(1, startDate);
            ps.setTime(2, endDate);
            ps.setInt(3, workstationId);
    
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return false;
    } */


    public List<Service> getServicesByMechanic(int mechanicId) {
        List<Service> services = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM services WHERE MechanicId = ?")) {
            ps.setInt(1, mechanicId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Service service = new Service(
                        rs.getInt("Id"),
                        rs.getTime("StartTime").toLocalTime(),
                        rs.getTime("EndTime").toLocalTime(),
                        Service.Type.valueOf(rs.getString("Type")),
                        rs.getInt("WorkstationId"),
                        rs.getString("VehicleId"),
                        Service.Status.valueOf(rs.getString("Status"))
                        );
                    services.add(service);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return services;
    }
}
