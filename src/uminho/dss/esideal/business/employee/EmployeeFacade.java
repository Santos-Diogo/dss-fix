package uminho.dss.esideal.business.employee;

import java.sql.Time;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import uminho.dss.esideal.business.service.Service;
import uminho.dss.esideal.data.EmployeeDAO;
import uminho.dss.esideal.data.ServiceDAO;

public class EmployeeFacade implements IEmployeeFacade {
    private EmployeeDAO employeeDAO;

    public EmployeeFacade() {
        this.employeeDAO = EmployeeDAO.getInstance();
    }

    @Override
    public int getEmployeeCount() {
        return employeeDAO.getCount();
    }

    @Override
    public boolean isEmployeeEmpty() {
        return employeeDAO.isEmpty();
    }

    @Override
    public boolean doesEmployeeExist(int employeeId) {
        return employeeDAO.existsById(employeeId);
    }

    @Override
    public Employee getEmployeeById(int employeeId) {
        return employeeDAO.getById(employeeId);
    }

    @Override
    public boolean registerEmployee(String employee, String password) {
        employeeDAO.addEmployee(employee, password, "FE");
        return true;
    }

    @Override
    public boolean registerEmployee(Integer id, String employee, String password, Collection<Service.Type> skillset) {
        employeeDAO.addEmployee(id, employee, password, skillset, "M");
        return true;
    }   

    @Override
    public Employee removeEmployeeById(int employeeId) {
        return employeeDAO.removeById(employeeId);
    }

    @Override
    public Collection<Employee> getAllEmployees() {
        return employeeDAO.getAll();
    }

    @Override
    public int nextEmployeeID () {
        return employeeDAO.nextEmployeeID();
    }

    /*@Override
    public void mechanicStartShift(int id) {    
        employeeDAO.mechanicStartShift(id);
    }
    */
    @Override
    public void mechanicEndShift (int id)
    {
        employeeDAO.mechanicEndShift(id);
    }

    @Override
    public void mechanicStartService(int service_id) {  //When we figure out the time
        //employeeDAO.mechanicStartService(service_id);
    }

    @Override
    public void mechanicEndService(int service_id) {    //When we figure out the time
        //employeeDAO.mechanicEndService(service_id);
    }

    @Override
    public List<Service> listServicesForMechanic(int mechanic_id) {
        ServiceDAO serviceDao = ServiceDAO.getInstance(); // Get the instance of ServiceDAO
        return serviceDao.getServicesByMechanic(mechanic_id); // Fetch and return the list of services for the mechanic
    }
    
    @Override
    public String authenticate (int id, String password, String type)
    {
        return this.employeeDAO.authenticate(id, password, type);
    }

    @Override
    public Map<String, Object> getLeastOccupiedMechanic(Collection<Integer> mechanic) {
        return this.employeeDAO.getLeastOccupiedMechanic(mechanic);
    }

    public Collection<Integer> getMechanicsAvailableId (Service.Type type, Time last_possible_start_time)
    {
        return this.employeeDAO.getMechanicsAvailableId (type, last_possible_start_time);
    }
}
