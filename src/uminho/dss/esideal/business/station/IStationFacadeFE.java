package uminho.dss.esideal.business.station;

import java.time.Duration;
import java.time.LocalTime;

import uminho.dss.esideal.business.client.Client;
import uminho.dss.esideal.business.service.Service;

public interface IStationFacadeFE 
{
    public String authenticate (int id, String password, String type);

    public void registerClient(Client client);

    public LocalTime scheduleService(Service.Type type, String name, String car, Duration duration) throws Exception;

    public LocalTime scheduleCheckup (String vehicle) throws Exception;

    public void registerVehicle (int owner, String lp);
}
