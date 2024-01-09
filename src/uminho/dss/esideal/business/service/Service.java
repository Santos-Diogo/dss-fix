package uminho.dss.esideal.business.service;

import java.time.LocalTime;

public class Service {
    public enum Type {
    UNIVERSAL,
    COMBUSTION,
    DIESEL,
    PETROL,
    ELECTRICAL
    }

    public enum Status {
        NOTSCHEDULED,
        DUE,
        STARTED,
        FINISHED
    }

    private int id;
    private LocalTime start;
    private LocalTime end;
    private Type type;
    private Status status;
    private int workstationId;
    // Add functionality for employeeId
    private int employeeId;
    private String vehicleId;

    public Service(){}

    public Service(int id, LocalTime start, LocalTime end, Type type, int workstation, String vehicle, Status status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.type = type;
        this.workstationId = workstation;
        this.vehicleId = vehicle;
        this.status = status;
    }

    public Service(LocalTime start, LocalTime end, Type type, int workstation, int employeeID, String vehicle, Status status) {
        this.id = -1;
        this.start = start;
        this.end = end;
        this.type = type;
        this.workstationId = workstation;
        this.employeeId = employeeID;
        this.vehicleId = vehicle;
        this.status = status;
    }

    public int getId() {
        return id;
    }
    public LocalTime getStart() {
        return start;
    }
    public LocalTime getEnd() {
        return end;
    }
    public Type getType() {
        return type;
    }
    public int getWorkstationId() {
        return workstationId;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setStart(LocalTime start) {
        this.start = start;
    }
    public void setEnd(LocalTime end) {
        this.end = end;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public void setWorkstationId(int employee) {
        this.workstationId = employee;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Service [id=" + id + ", start=" + start + ", end=" + end + ", type=" + type + ", employee=" + workstationId
                + "]";
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicle) {
        this.vehicleId = vehicle;
    }

    
}