package uminho.dss.esideal.business.workstation;

import java.sql.Time;
import java.util.Collection;
import java.util.Map;

import uminho.dss.esideal.business.service.Service;

public interface IWorkstationFacade 
{
    public void startShift();

    public void registerWorkstation(Workstation workstation);

    public Collection<Service.Type> getSkillset(int id) throws Exception;

    public Collection<Workstation> getWorkstations () throws Exception;

    public Collection<Integer> getWorkstationAvailableId (Service.Type type, Time current_time, Time last_possible_start_time);

    public Map<String, Object> getLeastOccupiedWorkstation(Collection<Integer> workstations);

    public int nextWorkstationID ();
}
