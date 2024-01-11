package uminho.dss.esideal.business.station;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import uminho.dss.esideal.business.client.Client;
import uminho.dss.esideal.business.client.ClientFacade;
import uminho.dss.esideal.business.client.IClientFacade;
import uminho.dss.esideal.business.client.Vehicle;
import uminho.dss.esideal.business.employee.Employee;
import uminho.dss.esideal.business.employee.EmployeeFacade;
import uminho.dss.esideal.business.employee.IEmployeeFacade;
import uminho.dss.esideal.business.employee.Mechanic;
import uminho.dss.esideal.business.service.IServiceFacade;
import uminho.dss.esideal.business.service.Service;
import uminho.dss.esideal.business.service.ServiceFacade;
import uminho.dss.esideal.business.service.Service.Status;
import uminho.dss.esideal.business.service.Service.Type;
import uminho.dss.esideal.business.workstation.IWorkstationFacade;
import uminho.dss.esideal.business.workstation.Workstation;
import uminho.dss.esideal.business.workstation.WorkstationFacade;
import uminho.dss.esideal.data.StationStatusDAO;

public class StationFacade implements IStationFacadeSM, IStationFacadeFE, IStationFacadeM
{
    private IEmployeeFacade modelEmployee;
    private IClientFacade modelClient;
    private IWorkstationFacade modelWorkstation;
    private IServiceFacade modelService;
    private StationStatusDAO statusDAO;

    private LocalTime opening_time; 
    private LocalTime closing_time;

    public StationFacade () {
        this.modelEmployee= new EmployeeFacade();
        this.modelWorkstation= new WorkstationFacade();
        this.modelClient= new ClientFacade();
        this.modelService= new ServiceFacade();
        this.statusDAO= StationStatusDAO.getInstance();
        this.opening_time= LocalTime.of(9, 0, 0);
        this.closing_time= LocalTime.of(19, 0, 0);
    }

    public StationFacade (IEmployeeFacade modelEmployee, IClientFacade modelClient, IWorkstationFacade modelWorkstation, IServiceFacade modelService)
    {
        this.modelEmployee= modelEmployee;
        this.modelClient= modelClient;
        this.modelWorkstation= modelWorkstation;
        this.modelService = modelService;

        this.opening_time = LocalTime.of(9, 0, 0);
        this.closing_time = LocalTime.of(19, 0, 0);
    }

    // All

    public String authenticate (int id, String password, String type)
    {
        return modelEmployee.authenticate(id, password, type);
    }

    // Mechanic

    @Override
    public void startShift(int mechanic_id, int workstation_id) throws Exception
    {
        Mechanic employee = (Mechanic) modelEmployee.getEmployeeById(mechanic_id);
        if(employee.getSkillset().containsAll(this.modelWorkstation.getSkillset(workstation_id)))
        {
            employee.setOnShift(true);
        }
    }

    @Override
    public Collection<Service> listJobs(int mechanic_id) 
    {
        return this.modelService.getServicesByMechanic(mechanic_id);
    }

    @Override
    // also delegating work to modelService
    public void startJob(int mechanic_id) 
    {
        this.modelEmployee.mechanicStartService(mechanic_id);
    }

    @Override
    public void endJob(int mechanic_id) 
    {
        this.modelEmployee.mechanicEndService(mechanic_id);
    }

    @Override
    public void endShift(int employeeId) 
    {
        this.modelEmployee.mechanicEndShift(employeeId);
    }

    public Collection<Workstation> listWorkstation() throws Exception
    {
        return this.modelWorkstation.getWorkstations();
    }

    //Front-Desk Employee

    @Override
    public void registerClient(Client client) 
    {
        modelClient.addClient(client);
    }

    /**
     * @TODO
     */
    @Override
    public LocalTime scheduleService(Service.Type type, String car, String name, Duration duration) throws Exception
    {
        LocalTime last_poss= this.closing_time.minus(duration);
        Time last_possible_start_time= Time.valueOf(last_poss);

        Collection<Integer> workstations= modelWorkstation.getWorkstationAvailableId (type, last_possible_start_time);
        if (workstations.isEmpty())
            throw new Exception("No workstation available today");

        Collection<Integer> employees= modelEmployee.getMechanicsAvailableId (type, last_possible_start_time);
        if (employees.isEmpty())
            throw new Exception("No mechanic available today");
            
        return scalonateService (workstations, employees, duration, type, name, car, Status.NOTSCHEDULED);
    }

    // System Manager
    
    @Override
    public void registerWorkstation(Workstation workstation) 
    {
        modelWorkstation.registerWorkstation(workstation);
    }

    @Override
    public void registerFrontdesk (String name, String password) {
        modelEmployee.registerEmployee(name, password);
    }
    
    @Override
    public void registerMechanic(Integer id, String name, String password, String skillset) throws IllegalArgumentException
    {
        modelEmployee.registerEmployee(id, name, password, parseSkillset(skillset));
    }
    
    @Override
    public void registerOpeningHour(LocalTime t) 
    {
        this.opening_time= t;
    }
    
    @Override
    public int nextWorkstationID (){
        return this.modelWorkstation.nextWorkstationID();
    }

    @Override
    public int nextEmployeeID (){
        return this.modelEmployee.nextEmployeeID();
    }

    @Override
    public LocalTime scheduleCheckup (String vehicle) throws Exception
    {
        Duration duration = Duration.ofHours(1);
        Type type = Type.UNIVERSAL;
        LocalTime last_poss= this.closing_time.minus(duration);
        Time last_possible_start_time= Time.valueOf(last_poss);

        Collection<Integer> workstations= modelWorkstation.getWorkstationAvailableId (type, last_possible_start_time);
        if (workstations.isEmpty())
            throw new Exception("No workstation available today");
        Collection<Integer> employees= modelEmployee.getMechanicsAvailableId (type, last_possible_start_time);
        if (employees.isEmpty())
            throw new Exception("No mechanic available today");
            
        return scalonateService (workstations, employees, duration, type, "Checkup", vehicle, Status.DUE);
    }

    @Override
    public void registerVehicle(int owner, String lp)
    {
        this.modelClient.addVehicle(new Vehicle(lp, owner));
    }

    private LocalTime scalonateService (Collection<Integer> workstations, Collection<Integer> mechanics, Duration duration, Type type, String name, String car, Status status) {
        Map<String, Object> workstationIDtime = this.modelWorkstation.getLeastOccupiedWorkstation(workstations);
        Map<String, Object> mechanicIDtime = this.modelEmployee.getLeastOccupiedMechanic(mechanics);
        LocalTime workstationTime =(LocalTime) workstationIDtime.get("time");
        LocalTime mechanicTime =(LocalTime) mechanicIDtime.get("time");
        LocalTime minTime = (workstationTime.isBefore(mechanicTime)) ? workstationTime : mechanicTime;
        if (minTime.isBefore(this.opening_time)) minTime = this.opening_time;

        LocalTime current_time= this.statusDAO.getTime().toLocalTime();
        minTime= (minTime.isBefore(current_time)) ? current_time : minTime;

        Service service = new Service(minTime, minTime.plus(duration), type, name, (Integer) workstationIDtime.get("id"), (Integer) mechanicIDtime.get("id"), car, status);
        this.modelService.addOrUpdateService(service);
        return minTime;
    }

    private Collection<Service.Type> parseSkillset (String skillset) throws IllegalArgumentException{
        List<Service.Type> res = Arrays.stream(skillset.split(",\\s*"))
                .map(String :: trim)
                .map(Service.Type :: valueOf)
                .collect(Collectors.toList());
        return res;
    }

    @Override
    public void setTime (Time time)
    {
        this.statusDAO.setTime(time);
    }

    @Override
    public void setOpeningHour(Time time) 
    {
        this.opening_time= time.toLocalTime();
    }
}