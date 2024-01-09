package uminho.dss.esideal.business.workstation;

import uminho.dss.esideal.business.service.Service;
import uminho.dss.esideal.business.service.Service.Type;
import uminho.dss.esideal.data.WorkstationDAO;

import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class WorkstationFacade implements IWorkstationFacade{

    private final WorkstationDAO workstations;

    public WorkstationFacade () {
        this.workstations = WorkstationDAO.getInstance();
    }

    
    @Override
    public void startShift() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startShitf'");
    }

    @Override
    public void registerWorkstation(Workstation workstation) {
        this.workstations.addWorkstation(workstation);
    }

    /** Need to add update the exception to our own
     * @param id
     * @return Collection<Object>
     */
    @Override
    public Collection<Service.Type> getSkillset(int id) throws Exception {
        return new ArrayList<>(workstations.getSkillset(id));
    }

    @Override
    public int nextWorkstationID () {
        return this.workstations.nextWorkstationID();
    }

    @Override
    public Map<String, Object> getLeastOccupiedWorkstation(Collection<Integer> workstations) {
        return this.workstations.getLeastOccupiedWorkstation(workstations);
    }

    @Override
    public Collection<Integer> getWorkstationAvailableId (Type type, Time last_possible_start_time) 
    {
        return this.workstations.getWorkstationAvailable(type, last_possible_start_time);
    }


    @Override
    public Collection<Workstation> getWorkstations() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWorkstations'");
    }
}
