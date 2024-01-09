package uminho.dss.esideal.Test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uminho.dss.esideal.business.client.Client;
import uminho.dss.esideal.business.client.ClientFacade;

public class BusinessTest {
    
    @Test
    public void clientAddTest() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.addClient(new Client(1, "name", "12334569", "999999990", "Main Street, 2340 Mödling"));
        assertEquals(1, clientFacade.getClients().size());
    }
}
