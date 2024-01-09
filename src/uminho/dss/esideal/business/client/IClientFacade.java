package uminho.dss.esideal.business.client;

import java.util.Collection;

public interface IClientFacade 
{
    public Collection<Vehicle> getVehicles();

    public void removeVehicleFromClient(int vehicleId);

    public Collection<Vehicle> getVehicles(int clientId);

    public Collection<Client> getClients();

    public void addVehicle(Vehicle vehicle);

    public void addClient(Client client);

    public boolean clientExists(int clientId);

    public Vehicle findVehicle(int vehicleId);

    public boolean vehicleExists(int vehicleId);

    public boolean vehicleExistsForClient(int clientId, int vehicleId);

    public boolean hasClients();

    public boolean hasVehicles();
}
