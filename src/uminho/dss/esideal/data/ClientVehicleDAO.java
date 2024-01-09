package uminho.dss.esideal.data;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

import uminho.dss.esideal.business.client.Client;
import uminho.dss.esideal.business.client.Vehicle;

public class ClientVehicleDAO 
{
    private static ClientVehicleDAO singleton = null;

    private ClientVehicleDAO() 
    {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) 
        {
            String sql = "CREATE TABLE IF NOT EXISTS clients (" +
                    "Id INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                    "Name VARCHAR(100) NOT NULL," +
                    "Telephone VARCHAR(20) NOT NULL," +
                    "VatNumber VARCHAR(20)," +
                    "Address VARCHAR(255))";
            stm.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS vehicles (" +
                    "Id VARCHAR(8) NOT NULL PRIMARY KEY," +
                    "Owner INT NOT NULL," +
                    "FOREIGN KEY (Owner) REFERENCES clients(Id))";
            stm.executeUpdate(sql);
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public static ClientVehicleDAO getInstance() {
        if (ClientVehicleDAO.singleton == null) {
            ClientVehicleDAO.singleton = new ClientVehicleDAO();
        }
        return ClientVehicleDAO.singleton;
    }

    public int getCountClient() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM clients")) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return 0;
    }

    public boolean clientIsEmpty() {
        return this.getCountClient() == 0;
    }

    public boolean clientExistsById(int clientId) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM clients WHERE Id = ?")) {
            ps.setInt(1, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public Client getClientById(int clientId) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM clients WHERE Id = ?")) {
            ps.setInt(1, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Client client = new Client();
                    client.setId(rs.getInt("Id"));
                    client.setName(rs.getString("Name"));
                    client.setTelephone(rs.getString("Telephone"));
                    client.setVatNumber(rs.getString("VatNumber"));
                    client.setAddress(rs.getString("Address"));
                    return client;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return null;
    }

    public void addOrUpdateClient(Client client) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO clients(Name, Telephone, VatNumber, Address) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, client.getName());
            ps.setString(2, client.getTelephone());
            ps.setString(3, client.getVatNumber());
            ps.setString(4, client.getAddress());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public Client removeClientById(int clientId) {
        Client client = getClientById(clientId);
        if (client != null) {
            try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM clients WHERE Id = ?")) {
                ps.setInt(1, clientId);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new NullPointerException(e.getMessage());
            }
        }
        return client;
    }

    public Collection<Client> getAllClients() {
        Collection<Client> clients = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM clients")) {
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("Id"));
                client.setName(rs.getString("Name"));
                client.setTelephone(rs.getString("Telephone"));
                client.setVatNumber(rs.getString("VatNumber"));
                client.setAddress(rs.getString("Address"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return clients;
    }

    public int getVehicleCount() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM vehicles")) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return 0;
    }

    public boolean isEmptyVehicle() {
        return this.getVehicleCount() == 0;
    }

    public boolean vehicleExistsById(int vehicleId) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM vehicles WHERE Id = ?")) {
            ps.setInt(1, vehicleId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public boolean vehicleExistsByIdAndOwnerId(int vehicleId, int ownerId) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM vehicles WHERE Id = ? AND Owner = ?")) {
            ps.setInt(1, vehicleId);
            ps.setInt(2, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    public Vehicle getVehicleById(int vehicleId) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM vehicles WHERE Id = ?")) {
            ps.setInt(1, vehicleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setlp(rs.getString("Id"));
                    vehicle.setOwner(rs.getInt("Owner"));
                    return vehicle;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return null;
    }

    public void addOrUpdateVehicle(Vehicle vehicle) {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO vehicles VALUES (?, ?)")) {
            ps.setString(1, vehicle.getlp());
            ps.setInt(2, vehicle.getOwner());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    // why do we return the 
    public Vehicle removeVehicleById(int vehicleId) {
        Vehicle vehicle = getVehicleById(vehicleId);
        if (vehicle != null) {
            try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM vehicles WHERE Id = ?")) {
                ps.setInt(1, vehicleId);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new NullPointerException(e.getMessage());
            }
        }
        return vehicle;
    }

    public Collection<Vehicle> getAllVehicles() {
        Collection<Vehicle> vehicles = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM vehicles")) {
            while (rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setlp(rs.getString("Id"));
                vehicle.setOwner(rs.getInt("Owner"));
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return vehicles;
    }

    public Collection<Vehicle> getAllVehiclesByOwnerId(Integer ownerId) {
        Collection<Vehicle> vehicles = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM vehicles WHERE Owner = ?")) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setlp(rs.getString("Id"));
                    vehicle.setOwner(rs.getInt("Owner"));
                    vehicles.add(vehicle);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return vehicles;
    }
}
