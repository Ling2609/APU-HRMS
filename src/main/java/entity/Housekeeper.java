package entity;

import entity.User.Role;

public class Housekeeper extends Staff{
        
    public Housekeeper() {}
    
    public Housekeeper(long id, String name, String password, String gender, String identification,
                String phone, String email, String address, Role role, Double salary) {
        
        super(id, name, password, gender, identification, phone, email, address, role, salary);
                
    }
    
    public static Housekeeper isHouseKeeper(Staff staff) {
        
        if(staff.getRole() != User.Role.HOUSEKEEPER) {
            return null;
        }
        
        Housekeeper housekeeper = new Housekeeper(staff.getId(), staff.getName(), staff.getPassword(), staff.getGender(), staff.getIdentification(), 
                staff.getPhone(), staff.getEmail(), staff.getAddress(), staff.getRole(), staff.getSalary());
        
        return housekeeper;
        
    }

}
