package uminho.dss.esideal.business.client;

public class Client {
    private int id;
    private String name;
    private String telephone;
    private String vatNumber;
    private String address;

    public Client() {
    }

    public Client(int id, String name, String telephone, String vatNumber, String address) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.vatNumber = vatNumber;
        this.address = address;
    }

    public Client(String name, String telephone, String vatNumber, String address) {
        this.id = -1;
        this.name = name;
        this.telephone = telephone;
        this.vatNumber = vatNumber;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toString() {
        return "Client(" + this.id + ", " + this.name + ", " + this.telephone + ", " + this.vatNumber + ", " + this.address + ")";
    }
}
