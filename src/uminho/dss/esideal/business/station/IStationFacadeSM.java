package uminho.dss.esideal.business.station;

import java.time.LocalTime;

import uminho.dss.esideal.business.workstation.Workstation;

public interface IStationFacadeSM 
{
    public String authenticate (int id, String password, String type);

    public void registerWorkstation(Workstation workstation);

    public void registerMechanic(Integer id, String name, String password, String skillset) throws IllegalArgumentException;

    public void registerFrontdesk(String name, String password);

    public void registerOpeningHour(LocalTime t);

    public int nextWorkstationID ();

    public int nextEmployeeID ();
}
