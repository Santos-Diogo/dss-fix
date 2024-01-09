package uminho.dss.esideal.ui.FE;

import java.util.Scanner;

import uminho.dss.esideal.business.client.Client;
import uminho.dss.esideal.business.station.*;
import uminho.dss.esideal.ui.Menu;

public class TextUI {

    private final IStationFacadeFE model;
    private String name;
    private final Scanner scan;


    public TextUI() {
        this.model = new StationFacade();
        this.scan = new Scanner(System.in);
        this.name = null;
    }

    public void run () {
        System.out.println("Welcome, Frontdesk Employee!");
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
            this.name = this.model.authenticate(intid, password, "FE");
            if (this.name == null)
            {
                System.out.println("There is no Frontdesk Employee with those credentials");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    private void mainMenu() {
        Menu menu = new Menu("System Manager Menu - "+this.name, new String[]{
                "Register Client",
                "Register Vehicle",
                "Schedule Check-up",
                "Schedule unscheduled"
        });
        
        menu.setHandler(1, ()->registerClient());
        menu.setHandler(2, ()->registerVehicle());
        menu.setHandler(3, ()->scheduleCheckup());
        menu.run();
    }


    private void registerClient() 
    {
        System.out.println("Client name:");
        String name = scan.nextLine();
        System.out.println("Client telephone:");
        String telephone = scan.nextLine();
        System.out.println("Client VAT number:");
        String vat = scan.nextLine();
        System.out.println("Client address:");
        String addr = scan.nextLine();
        this.model.registerClient(new Client(name, telephone, vat, addr));
        System.out.println("Client registered");
    }

    private void registerVehicle()
    {
        System.out.println("Vehicle owner: ");
        int id = Integer.parseInt(scan.nextLine());
        System.out.println("Vehicle License Plate (XX-XX-XX):s");
        String lp = scan.nextLine();
        this.model.registerVehicle(id, lp);
        System.out.println("Vehicle Registered");
    }

    private void scheduleCheckup () {
        String vehicle = "";
        System.out.println("Vehicle License Plate (XX-XX-XX):");
        vehicle = scan.nextLine();
        try {
            this.model.scheduleCheckup(vehicle);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
