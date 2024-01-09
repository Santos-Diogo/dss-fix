package uminho.dss.esideal.business.client;

public class Vehicle {
    private String lp;
    private int owner;

    public Vehicle (String lp, int owner)
    {
        this.lp = lp;
        this.owner = owner;
    }

    public Vehicle () 
    {
        this.lp = "";
        this.owner = 0;
    }

    public String getlp() {
        return lp;
    }

    public void setlp(String lp) {
        this.lp = lp;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

}
