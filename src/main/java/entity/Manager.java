package entity;

import entity.User.Role;

public class Manager extends Staff{
        
    public Manager() {}
    
    public Manager(long id, String name, String password, String gender, String identification,
                String phone, String email, String address, Role role, Double salary) {
        
        super(id, name, password, gender, identification, phone, email, address, role, salary);
                
    }
    
    public static Manager isManager(Staff staff) {
        
        if(staff.getRole() != User.Role.MANAGER) {
            return null;
        }
        
        Manager manager = new Manager(staff.getId(), staff.getName(), staff.getPassword(), staff.getGender(), staff.getIdentification(), 
                staff.getPhone(), staff.getEmail(), staff.getAddress(), staff.getRole(), staff.getSalary());
        
        return manager;
        
    }

}
