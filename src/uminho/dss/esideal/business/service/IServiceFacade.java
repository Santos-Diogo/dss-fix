package uminho.dss.esideal.business.service;

import java.time.LocalTime;
import java.util.Collection;

public interface IServiceFacade {
    int getServiceCount();

    boolean isServiceEmpty();

    boolean doesServiceExist(int serviceId);

    Service getServiceById(int serviceId);

    void addOrUpdateService(Service service);

    Service removeServiceById(int serviceId);

    Collection<Service> getAllServices();

    Collection<Service> getServicesByMechanic (int mechanic_id);

    public void startService(int workstationId, int employeeID, LocalTime current);

    void endService(int id);
}
