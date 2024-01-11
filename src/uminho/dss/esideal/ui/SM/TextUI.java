package uminho.dss.esideal.ui.SM;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.mysql.cj.protocol.x.SyncFlushDeflaterOutputStream;

import uminho.dss.esideal.business.service.Service;
import uminho.dss.esideal.business.station.*;
import uminho.dss.esideal.business.workstation.Workstation;
import uminho.dss.esideal.ui.Menu;
import uminho.dss.esideal.ui.Parse.Parse;

public class TextUI {

    private final IStationFacadeSM model;
    private String name;
    private final Scanner scan;


    public TextUI() {
        this.model = new StationFacade();
        this.scan = new Scanner(System.in);
        this.name = null;
    }

    public void run () {
        System.out.println("Welcome, System Manager!");
        if (this.authenticate())
            this.mainMenu();
    }

    private boolean authenticate () {
        String id = "";
        String password = "";
        try {
            System.out.println("Insert your ID: ");
            id = scan.nextLine();
            System.out.println("Insert your password:");
            password = scan.nextLine();
            int intid = Integer.parseInt(id);
            this.name = this.model.authenticate(intid, password, "SM");
            if (this.name == null)
            {
                System.out.println("There is no System Manager with those credentials");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            System.out.println("There is no System Manager with those credentials");
            return false;
        }
    }

    private void mainMenu() {
        Menu menu = new Menu("System Manager Menu - "+this.name, new String[]{
                "Manage workstations",
                "Manage employees",
                "Register opening hours",
                "Select Current Time"
        });
        
        menu.setHandler(1, ()->manageWorkstations());
        menu.setHandler(2, ()->manageEmployees());
        menu.setHandler(3, ()->registerOpeningHour());
        menu.setHandler(4, ()->selectCurrentTime());
        menu.run();
    }

    
    private void manageWorkstations () {
        Menu menu = new Menu("Workstations management", new String[]{
            "Register Workstation",
            "Remove Workstation"
        });
        
        menu.setHandler(1, ()->registerWorkstation());
        menu.run();
    }
    
    private void manageEmployees () {
        Menu menu = new Menu("Employees management", new String[]{
            "Register Frontdesk",
            "Remove Frontdesk",
            "Register Mechanic",
            "Remove Mechanic"
        });
        
        /* menu.setPreCondition(2, null);
        menu.setPreCondition(3, null); */
        
        
        menu.setHandler(1, ()->registerFrontdesk());
        menu.setHandler(3, ()->registerMechanic());
        /* menu.setHandler(3, null);
        menu.setHandler(4, null); */
        menu.run();
    }
    
    private void registerOpeningHour ()
    {
        try
        {
            System.out.println("Input opening hour:");
            System.out.print("Time in format 'hh:mm:ss : ");
            String in= scan.nextLine();
            Time time= Parse.parseInputTime(in);
            model.setOpeningHour(time);
        }
        catch (Exception e)
        {
            System.err.println("Please input a valid time format");
        }
    }

    private void selectCurrentTime ()
    {
        try
        {
            System.out.println("Input time you want to skip to:");
            System.out.print("Time in format 'hh:mm:ss : ");
            String in= scan.nextLine();
            Time time= Parse.parseInputTime(in);
            model.setTime(time);
        }
        catch (Exception e)
        {
            System.err.println("Please input a valid time format");
        }
    }

    private void registerFrontdesk () {
        System.out.println("Name of the frontdesk employee");
        String name = scan.nextLine();
        System.out.println("Password of "+name);
        String password = scan.nextLine();
        this.model.registerFrontdesk(name, password);
    }

    private void registerMechanic() {
        System.out.println("Name of the mechanic");
        String name = scan.nextLine();
        System.out.println("Password of "+name);
        String password = scan.nextLine();
        System.out.println("Skillset of "+name+" ('UNIVERSAL', 'COMBUSTION', 'DIESEL', 'PETROL', 'ELECTRICAL'):");
        System.out.println("Please use a comma to seperate each skill");
        String skillset = scan.nextLine();
        int id = this.model.nextEmployeeID();
        try {
            this.model.registerMechanic(id, name, password, skillset);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void registerWorkstation() {
        String name = "";
        String serviceType = "";
        try {
            System.out.println("Name of the workstation");
            name = scan.nextLine();
            System.out.println("Type of service it supports ('UNIVERSAL', 'COMBUSTION', 'DIESEL', 'PETROL', 'ELECTRICAL'):");
            serviceType = scan.nextLine();
            Service.Type type = Service.Type.valueOf(serviceType);
            int id = this.model.nextWorkstationID();
            this.model.registerWorkstation(new Workstation(id, name, new ArrayList<Service.Type>(Arrays.asList(type))));
            System.out.println("Workstation '"+name+"' successfully registered");
        } catch (IllegalArgumentException e) {
            System.out.println(serviceType+" is not an available service type");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
