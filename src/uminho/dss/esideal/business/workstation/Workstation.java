package uminho.dss.esideal.business.workstation;

import java.util.ArrayList;
import java.util.Collection;

import uminho.dss.esideal.business.service.Service;
import uminho.dss.esideal.business.service.Service.Type;

public class Workstation {
    
    private int id;
    private String name;
    private Collection<Service.Type> skillset;

    public Workstation () {
        this.id = -1;
        this.name = "";
        this.skillset = new ArrayList<>();
    }
    
    public Workstation(int id, String name, Collection<Type> skillset) 
    {
        this.id = id;
        this.name = name;
        this.skillset = skillset;
    }

    public Workstation(String name, Collection<Type> skillset) 
    {
        this.id = -1;
        this.name = name;
        this.skillset = skillset;
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

    public Collection<Service.Type> getSkillset(){
        return skillset;
    }

    public void setSkillset(Collection<Service.Type> skillset) {
        this.skillset = skillset;
    }
    
}
