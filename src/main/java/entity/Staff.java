package entity;

public class Staff extends User{
    
    private double salary;
    
    public Staff() {}
    
    public Staff(long id, String name, String password, String gender, String identification,
                String phone, String email, String address, Role role, Double salary) {
        
        super(id, name, password, gender, identification, phone, email, address, role, salary);
        this.salary = salary;
                
    }
    
    public static Staff isStaff(User user) {
        
        if(user.getRole() == User.Role.CUSTOMER) {
            return null;
        }
        
        Staff staff = new Staff(user.getId(), user.getName(), user.getPassword(), user.getGender(), user.getIdentification(), 
                user.getPhone(), user.getEmail(), user.getAddress(), user.getRole(), user.getSalary());
        
        return staff;
        
    }
    
//    public User getUser(Staff staff) {
//        
//        User user = new User(staff.getId(), staff.getName(), staff.getPassword(), staff.getGender(), staff.getIdentification(), 
//                staff.getPhone(), staff.getEmail(), staff.getAddress(), staff.getRole(), staff.getSalary());
//        
//        return user;
//        
//    }
    
    @Override
    public String toString() {
        return "Staff: " + this.getName() + " " + this.getPassword() + " " + this.getGender() + " " + this.getIdentification() + " " +
            this.getPhone() + " " + this.getEmail() + " " + this.getAddress() + " " + this.getRole().toString() + " " + this.getSalary();
    }
    
}
