package uminho.dss.esideal.ui.M;

import java.io.Console;
import java.util.Scanner;
import java.util.Collection;
import uminho.dss.esideal.business.workstation.Workstation;
import uminho.dss.esideal.business.service.Service;
import uminho.dss.esideal.business.station.IStationFacadeM;
import uminho.dss.esideal.business.station.StationFacade;
import uminho.dss.esideal.ui.Menu;

public class TextUI 
{
    private final IStationFacadeM model;
    private String name;
    private int myNum;
    private int myWorkstation;
    private final Scanner scan;

    public TextUI() 
    {
        this.model = new StationFacade();
        this.scan = new Scanner(System.in);
        this.name = null;
    }

    public void run () 
    {
        System.out.println("Welcome, Mechanic!");
        if (this.authenticate())
        {
            listWorkstations();
            try
            {
                model.startShift(this.myNum, this.myWorkstation);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            mainMenu();
        }
    }

    private boolean authenticate () {
        String id = "";
        String password = "";
        try {
            System.out.println("Insert your ID: ");
            id = scan.nextLine();
            System.out.println("Insert your password:");
            password = scan.nextLine();
            myNum = Integer.parseInt(id);
            this.name = this.model.authenticate(myNum, password, "M");
            if (this.name == null)
            {
                System.out.println("There is no Mechanic with those credentials");
                return false;
            }

        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    
    public void listWorkstations ()
    {
        try
        {

            System.out.println("\nChoose a Workstation:");
            Collection<Workstation> workstations= model.listWorkstation();
            for (Workstation w: workstations)
            {
                System.out.println(w.getId()+" "+w.getName()+" "+w.getSkillset());
            }
            System.out.print("\nChosen Workstation: ");
            String wks;
            wks= scan.nextLine();
            this.myWorkstation= Integer.parseInt(wks);
        }
        catch (Exception e) {e.printStackTrace();}
    }

    private void mainMenu() 
    {
        Menu menu = new Menu("Mechanic Menu - "+this.name, new String[]{
            "End Shift",
            "List Jobs",
            "Start Job",
            "End Job"        
        });
        
        menu.setHandler(1, ()->endShit());
        menu.setHandler(2, ()->listJobs());
        menu.setHandler(3, ()->startJob());
        menu.setHandler(4, ()->endJob());
        menu.run();
    }

    public void startShit (int mechanic_id, int workstation_id)
    {
        try
        {
            model.startShift(mechanic_id, workstation_id);
        }
        catch (Exception e) {e.printStackTrace();}
    }

    public void endShit ()
    {
        model.endShift(myNum);
    }

    public void listJobs ()
    {
        Collection<Service> services= model.listJobs(myNum);
        if (services.isEmpty())
        {
            System.out.println("No available Jobs");
        }
        else
        {
            System.out.println("Available Jobs:");
            System.out.println("ID\tMATRICULA\tDESCRICAO");
            for (Service s: services)
            {
                System.out.println(s.getId()+"\t"+s.getVehicleId()+"\t"+"NAO ESQUECER DESCRICAO");
            }
        }
    }
    
    public void startJob ()
    {
        model.startJob(myNum);
    }

    public void endJob ()
    {
        model.endJob(myNum);
    }
}
