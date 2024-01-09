package uminho.dss.esideal.business.client;

import java.util.ArrayList;
import java.util.Collection;

import uminho.dss.esideal.data.ClientVehicleDAO;

public class ClientFacade implements IClientFacade {
    private final ClientVehicleDAO clientVehicles;

    public ClientFacade() {
        this.clientVehicles = ClientVehicleDAO.getInstance();
    }

    public Collection<Vehicle> getVehicles() {
        return new ArrayList<>(this.clientVehicles.getAllVehicles());
    }

    public void removeVehicleFromClient(int vehicleId) {
        clientVehicles.removeVehicleById(vehicleId);
    }

    public Collection<Vehicle> getVehicles(int clientId) {
        return clientVehicles.getAllVehiclesByOwnerId(clientId);
    }

    public Collection<Client> getClients() {
        return clientVehicles.getAllClients();
    }

    public void addVehicle(Vehicle vehicle) {
        this.clientVehicles.addOrUpdateVehicle(vehicle);
    }

    public void addClient(Client client) {
        this.clientVehicles.addOrUpdateClient(client);
    }

    public boolean clientExists(int clientId) {
        return clientVehicles.clientExistsById(clientId);
    }

    public Vehicle findVehicle(int vehicleId) {
        return this.clientVehicles.getVehicleById(vehicleId);
    }

    public boolean vehicleExists(int vehicleId) {
        return this.clientVehicles.vehicleExistsById(vehicleId);
    }

    public boolean vehicleExistsForClient(int clientId, int vehicleId) {
        return this.clientVehicles.vehicleExistsByIdAndOwnerId(clientId, vehicleId);
    }

    public boolean hasClients() {
        return !this.clientVehicles.clientIsEmpty();
    }

    public boolean hasVehicles() {
        return !this.clientVehicles.isEmptyVehicle();
    }
}
