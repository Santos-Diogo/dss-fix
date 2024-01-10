package uminho.dss.esideal.business.service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Collection;

import uminho.dss.esideal.business.service.Service.Status;
import uminho.dss.esideal.data.ServiceDAO;

public class ServiceFacade implements IServiceFacade{
    private ServiceDAO serviceDAO;

    public ServiceFacade() {
        this.serviceDAO = ServiceDAO.getInstance();
    }

    public int getServiceCount() {
        return serviceDAO.getCount();
    }

    public boolean isServiceEmpty() {
        return serviceDAO.isEmpty();
    }

    public boolean doesServiceExist(int serviceId) {
        return serviceDAO.existsById(serviceId);
    }

    public Service getServiceById(int serviceId) {
        return serviceDAO.getById(serviceId);
    }

    public void addOrUpdateService(Service service) {
        serviceDAO.addOrUpdate(service);
    }

    public Service removeServiceById(int serviceId) {
        return serviceDAO.removeById(serviceId);
    }

    public Collection<Service> getAllServices() {
        return serviceDAO.getAll();
    }

    public Collection<Service> getServicesByWorkstation(int workstationId) {
        return serviceDAO.getServicesByWorkstationId(workstationId);
    }

    public Collection<Service> getServicesByMechanic(int mechanic_id)
    {
        return serviceDAO.getServicesByMechanic(mechanic_id);
    }

    public void startService(int workstationId, int employeeId, LocalTime current){
        // CHANGE to Workstation and Employee
        if (serviceDAO.hasDueServiceTodayByWorkstation(current, workstationId, employeeId)) {
            int service = serviceDAO.getFirstDueServiceOfDayByWorkstation(current, workstationId, employeeId);
            serviceDAO.updateStatusByID(service, Status.STARTED);
        } else {
            return;
        }
    }

    public void endService(int workstationId){
        /* // CHANGE to Workstation and Employee
        if (serviceDAO.hasStartedServiceTodayByWorkstation(LocalDate.now(), workstationId)) {
            Service service = serviceDAO.getFirstStartedServiceOfDayByWorkstation(LocalDate.now(), workstationId);;
            service.setStatus(Status.FINISHED);
            serviceDAO.addOrUpdate(service);
        } else {
            return;
        } */
    }

    @Override
    public void updateTime(Time time) 
    {
        this.serviceDAO.updateTime (time);    
    }
}
