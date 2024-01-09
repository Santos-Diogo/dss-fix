package uminho.dss.esideal.business.employee;

import java.util.ArrayList;
import java.util.Collection;

import uminho.dss.esideal.business.service.Service;
import uminho.dss.esideal.business.service.Service.Type;

public class Mechanic extends Employee {


    private Collection<Service.Type> skillset;
    private boolean onShift;

    public Mechanic(int id, String name, String password, Collection<Type> skillset) {
        super(id, name, password);
        this.skillset = skillset;
    }

    public Mechanic(int id, String name, String password) {
        super(id, name, password);
        this.skillset = new ArrayList<>();
    }

    public Collection<Service.Type> getSkillset() {
        return skillset;
    }

    public void setSkillset(Collection<Service.Type> skillset) {
        this.skillset = skillset;
    }
    
    public void addSkillset (Service.Type skill)
    {
        this.skillset.add(skill);
    }

    public boolean isOnShift() {
        return onShift;
    }

    public void setOnShift(boolean onShift) {
        this.onShift = onShift;
    }

    public String toString() {
        return "Employee [id=" + this.getId() + ", name=" + getName() + ", skillset=" + skillset + "]";
    }

}

