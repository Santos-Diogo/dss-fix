package uminho.dss.esideal.ui.M;

import java.util.Scanner;
import java.util.Collection;
import java.util.Map;

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
    private boolean checkup = false;
    private final Scanner scan;
    private final Map<String, Integer> universal= Map.of("replacing the tires", 10, "calibrating the wheels", 20, "aligning the steering", 30, "replacing the injectors", 20, "replacing the brake pads", 25, "changing the brake oil", 15, "cleaning the interior and/or exte-rior", 10, "replacing the cabin air filter", 40);


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
            return true;
        } catch (NumberFormatException e) {
            System.out.println("There is no Mechanic with those credentials");
            return false;
        }
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
            System.out.println("ID\tMATRICULA\tDESCRICAO\tWORKSTATION");
            for (Service s: services)
            {
                System.out.println(String.format("%d\t%s\t%s\t\t%d", s.getId(), s.getVehicleId(), s.getName(), s.getWorkstationId()));
            }
        }
    }
    
    public void startJob ()
    {
        try {
            this.checkup = model.startJob(myNum, myWorkstation);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void endJob ()
    {
        if (checkup)
            model.endCheckup(myNum);
            //model.endJob(myNum);
    }
}
