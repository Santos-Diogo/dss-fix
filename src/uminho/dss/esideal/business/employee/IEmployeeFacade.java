package uminho.dss.esideal.business.employee;

import java.util.List;
import java.util.Map;
import java.sql.Time;
import java.util.Collection;


import uminho.dss.esideal.business.service.Service; // Mechanic service/job

public interface IEmployeeFacade 
{
    public boolean registerEmployee (String name, String password);
    public boolean registerEmployee (Integer id, String name, String password, Collection<Service.Type> skillset);
    //public void mechanicStartShift (int id);
    public void mechanicEndShift(int mechanic_id);
    public void mechanicStartService(int service_id);
    public void mechanicEndService(int service_id);
    public List<Service> listServicesForMechanic (int mechanic_id);
    public int nextEmployeeID ();
    public String authenticate (int id, String password, String type);
    public Employee getEmployeeById(int id);
    public int getEmployeeCount();
    public boolean isEmployeeEmpty();
    public boolean doesEmployeeExist(int employeeId);
    public Employee removeEmployeeById(int employeeId);
    public Collection<Employee> getAllEmployees();
    public Collection<Integer> getMechanicsAvailableId (Service.Type type, Time last_possible_start_time);
    public Map<String, Object> getLeastOccupiedMechanic(Collection<Integer> mechanic);
}
