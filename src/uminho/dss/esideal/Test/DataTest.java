package uminho.dss.esideal.Test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uminho.dss.esideal.business.client.Client;
import uminho.dss.esideal.data.ClientVehicleDAO;

public class DataTest {
    
    @Test
    public void clientAddTest() {
        ClientVehicleDAO clientDAO = ClientVehicleDAO.getInstance();
    /*     clientDAO.addOrUpdate(new Client(1, "name", "12334569", "999999990", "Main Street, 2340 Mödling"));
        assertEquals(1, clientDAO.getAll().size()); */
    }
}
