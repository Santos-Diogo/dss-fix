package uminho.dss.esideal.business.station;

import java.util.Collection;

import uminho.dss.esideal.business.service.Service;
import uminho.dss.esideal.business.workstation.Workstation;

public interface IStationFacadeM 
{
    public String authenticate (int id, String password, String type);

    public void startShift(int mechanic_id, int workstation_id) throws Exception;
    
    public void endShift(int employeeId);

    public Collection<Service> listJobs(int mechanic_id);

    public Collection<Workstation> listWorkstation() throws Exception;

    public void startJob(int mechanic_id);

    public void endJob(int job_id);
}
