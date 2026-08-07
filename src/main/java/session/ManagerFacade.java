package session;

import entity.BookingLog;
import entity.Message;
import entity.Report;
import entity.RoomLog;
import entity.RoomType;
import entity.SalaryLog;
import entity.Staff;
import entity.User;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ManagerFacade extends StaffFacade {
    
    @EJB
    private RoomTypeFacade roomtypeFacade;

    @PersistenceContext(unitName = "APU-HRMS-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public ManagerFacade() {
        super(User.class);
    }
    
    public ManagerFacade(Class<User> entityClass) {
        super(entityClass);
    }
    
    public ArrayList<Staff> findAllStaff() {
        
        List<User> userList = em.createQuery(
            "SELECT u FROM User u WHERE u.role != :role ORDER BY u.name", User.class)
            .setParameter("role", User.Role.CUSTOMER)
            .getResultList();
        
        ArrayList<Staff> staffList = new ArrayList<>();
        
        for(User user: userList) {
            Staff staff = Staff.isStaff(user);
            if(staff != null) {
                staffList.add(staff);
            }
        }
        
        return staffList;
        
    }
    
    public Staff getStaffByID(String id) {
        
        List<User> userList = em.createQuery(
            "SELECT u FROM User u WHERE u.role != :role AND u.id = :id", User.class)
            .setParameter("role", User.Role.CUSTOMER)
            .setParameter("id", Long.valueOf(id))
            .getResultList();
        
        return Staff.isStaff(userList.getFirst());
        
    }
    
    public ArrayList<Report> findReportList() {
        
        List<Report> reportList = em.createQuery(
            "SELECT r FROM Report r ORDER BY r.startTime DESC", Report.class)
            .getResultList();
        
        ArrayList<Report> reportArrayList = new ArrayList<>();
        
        for(Report report: reportList) {
            if(report != null) {
                reportArrayList.add(report);
            }
        }
        
        return reportArrayList;
        
    }
    
    public Report getReportById(String id) {
        
        List<Report> reportList = em.createQuery(
            "SELECT r FROM Report r WHERE r.id = :id", Report.class)
            .setParameter("id", Long.valueOf(id))
            .getResultList();
        
        return reportList.getFirst();
        
    }
    
    public ArrayList<BookingLog> getReportBookings(Report report) {
        
        List<BookingLog> queryList = em.createQuery(
            "SELECT b FROM BookingLog b WHERE b.report.id = :id ORDER BY b.id", BookingLog.class)
            .setParameter("id", report.getId())
            .getResultList();
        
        ArrayList<BookingLog> returnArrayList = new ArrayList<>();
        
        for(BookingLog bookingLog : queryList) {
            if(bookingLog != null) {
                returnArrayList.add(bookingLog);
            }
        }
        
        return returnArrayList;
        
    }
    
    public ArrayList<RoomLog> getReportRoom(Report report) {
        
        List<RoomLog> queryList = em.createQuery(
            "SELECT r FROM RoomLog r WHERE r.report.id = :id ORDER BY r.id", RoomLog.class)
            .setParameter("id", report.getId())
            .getResultList();
        
        ArrayList<RoomLog> returnArrayList = new ArrayList<>();
        
        for(RoomLog roomlog : queryList) {
            if(roomlog != null) {
                returnArrayList.add(roomlog);
            }
        }
        
        return returnArrayList;
        
    }
    
    public ArrayList<SalaryLog> getReportSalary(Report report) {
        
        List<SalaryLog> queryList = em.createQuery(
            "SELECT s FROM SalaryLog s WHERE s.report.id = :id ORDER BY s.id", SalaryLog.class)
            .setParameter("id", report.getId())
            .getResultList();
        
        ArrayList<SalaryLog> returnArrayList = new ArrayList<>();
        
        for(SalaryLog salaryLog : queryList) {
            if(salaryLog != null) {
                returnArrayList.add(salaryLog);
            }
        }
        
        return returnArrayList;
        
    }
    
    public ArrayList<Message> getAllMessages(Report report) {
        
        List<Message> queryList = em.createQuery(
            "SELECT m FROM Message m ORDER BY m.id", Message.class)
            .getResultList();
        
        ArrayList<Message> returnArrayList = new ArrayList<>();
        
        for(Message message : queryList) {
            if(message != null) {
                returnArrayList.add(message);
            }
        }
        
        return returnArrayList;
        
    }
    
    public ArrayList<Message> getMessagesByBookingID(long id) {
        
        List<Message> queryList = em.createQuery(
            "SELECT m FROM Message m WHERE m.bookingUser.booking.id = :id ORDER BY m.id", Message.class)
            .setParameter("id", id)
            .getResultList();
        
        ArrayList<Message> returnArrayList = new ArrayList<>();
        
        for(Message message : queryList) {
            if(message != null) {
                returnArrayList.add(message);
            }
        }
        
        return returnArrayList;
        
    }
    
    public ArrayList<RoomType> findAllRoomTypes() {
        
        List<RoomType> queryList = roomtypeFacade.findAllRoomTypes();
        
        ArrayList<RoomType> returnArrayList = new ArrayList<>();
        
        for(RoomType type: queryList) {
            if(type != null) {
                returnArrayList.add(type);
            }
        }
        
        return returnArrayList;
        
    }
    
    public RoomType getRoomTypeByName (RoomType.RoomTypeName roomType) {
                
        List<RoomType> reportList = em.createQuery(
            "SELECT r FROM RoomType r WHERE r.roomTypeName = :name", RoomType.class)
            .setParameter("name", roomType)
            .getResultList();
        
        return reportList.getFirst();
        
    }
    
    public ArrayList<RoomType> updateRoomPrices(ArrayList<RoomType> updatedRoomTypes) {
        
        for(RoomType roomType: updatedRoomTypes) {
            roomtypeFacade.edit(roomType);
        }
        
        return updatedRoomTypes;
        
    }
    
}
