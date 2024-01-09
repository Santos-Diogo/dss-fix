package uminho.dss.esideal.business.employee;



public class Employee {
    private int id;
    private String name;
    private String password;

    public Employee (int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public Employee() {
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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + "]";
    }
}
