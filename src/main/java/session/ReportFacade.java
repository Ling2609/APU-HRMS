package session;

import entity.Booking;
import entity.BookingLog;
import entity.Report;
import entity.Room;
import entity.RoomLog;
import entity.SalaryLog;
import entity.User;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ReportFacade extends AbstractFacade<Report> {
        
    @EJB
    private BookingLogFacade bookingLogFacade;
    
    @EJB
    private SalaryLogFacade salaryLogFacade;
    
    @EJB
    private RoomLogFacade roomLogFacade;

    @PersistenceContext(unitName = "APU-HRMS-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReportFacade() {
        super(Report.class);
    }
    
    public ReportFacade(Class<Report> entityClass) {
        super(entityClass);
    }
    
    public Report createFinancialReport(Report report) {
        
        List<Booking> allBookings = em.createQuery(
            "SELECT b FROM Booking b "
                    + "WHERE b.estimatedCheckInTime >= :startTime "
                    + "AND b.estimatedCheckInTime <= :endTime "
                    + "AND b.bookingStatus != :status", 
                Booking.class)
            .setParameter("startTime", report.getStartTime().atStartOfDay())
            .setParameter("endTime", report.getEndTime().atStartOfDay())
            .setParameter("status", Booking.BookingStatus.UNPAID)
            .getResultList();
        
        System.out.println(allBookings.size());
        
        for(Booking booking : allBookings) {
            BookingLog bookingLog = new BookingLog(booking, report);
            bookingLogFacade.create(bookingLog);
        }
        
        List<User> allUsers = em.createQuery(
            "SELECT u FROM User u WHERE u.role != :role", User.class)
            .setParameter("role", User.Role.CUSTOMER)
            .getResultList();
        
        for(User user : allUsers) {
            SalaryLog salaryLog = new SalaryLog(user, report);
            salaryLogFacade.create(salaryLog);
        }
        
        return report;
        
    }
        
    public Report createTransactionReport(Report report) {
        
        List<Booking> allBookings = em.createQuery(
            "SELECT b FROM Booking b "
                    + "WHERE b.estimatedCheckInTime >= :startTime "
                    + "AND b.estimatedCheckInTime <= :endTime "
                    + "AND b.bookingStatus != :status", 
                Booking.class)
            .setParameter("startTime", report.getStartTime().atStartOfDay())
            .setParameter("endTime", report.getEndTime().atStartOfDay())
            .setParameter("status", Booking.BookingStatus.UNPAID)
            .getResultList();
                        
        for(Booking booking : allBookings) {
            BookingLog bookingLog = new BookingLog(booking, report);
            bookingLogFacade.create(bookingLog);
        }
        
        return report;
        
    }
    
    public Report createArrivalDepartureReport(Report report) {
        
        List<Booking> allBookings = em.createQuery(
            "SELECT b FROM Booking b "
                    + "WHERE b.estimatedCheckInTime >= :startTime "
                    + "AND b.estimatedCheckInTime <= :endTime "
                    + "AND (b.bookingStatus = :checkInStatus OR b.bookingStatus = :checkOutStatus OR b.bookingStatus = :lateStatus)"
                , Booking.class)
            .setParameter("startTime", report.getStartTime().atStartOfDay())
            .setParameter("endTime", report.getEndTime().atStartOfDay())
            .setParameter("checkInStatus", Booking.BookingStatus.CHECKED_IN)
            .setParameter("checkOutStatus", Booking.BookingStatus.CHECKED_OUT)
            .setParameter("lateStatus", Booking.BookingStatus.LATE)
            .getResultList();
                
        for(Booking booking : allBookings) {
            BookingLog bookingLog = new BookingLog(booking, report);
            bookingLogFacade.create(bookingLog);
        }
        
        List<Room> allRooms = em.createQuery(
            "SELECT r FROM Room r",
                Room.class)
            .getResultList();
        
        for(Room room : allRooms) {
            RoomLog roomLog = new RoomLog(room, report);
            roomLogFacade.create(roomLog);
        }
        
        return report;
        
    }
    
    public Report createNotesReport(Report report) {
        
        List<Booking> allBookings = em.createQuery(
            "SELECT b FROM Booking b "
                    + "WHERE b.estimatedCheckInTime >= :startTime "
                    + "AND b.estimatedCheckInTime <= :endTime "
                    + "AND b.bookingStatus != :status", 
                Booking.class)
            .setParameter("startTime", report.getStartTime().atStartOfDay())
            .setParameter("endTime", report.getEndTime().atStartOfDay())
            .setParameter("status", Booking.BookingStatus.UNPAID)
            .getResultList();
                
        for(Booking booking : allBookings) {
            
            BookingLog bookingLog = new BookingLog(booking, report);
            bookingLogFacade.create(bookingLog);
        }
                
        return report;
        
    }
    
    public Report createRoomStatusReport(Report report) {
      
        List<Booking> allBookings = em.createQuery(
            "SELECT b FROM Booking b "
                    + "WHERE b.estimatedCheckInTime >= :startTime "
                    + "AND b.estimatedCheckInTime <= :endTime", 
                Booking.class)
            .setParameter("startTime", report.getStartTime().atStartOfDay())
            .setParameter("endTime", report.getEndTime().atStartOfDay())
            .getResultList();
        
        for(Booking booking : allBookings) {
            
            BookingLog bookingLog = new BookingLog(booking, report);
            bookingLogFacade.create(bookingLog);
            
        }
        
        List<Room> allRooms = em.createQuery(
            "SELECT r FROM Room r",
                Room.class)
            .getResultList();
        
        for(Room room : allRooms) {
            RoomLog roomLog = new RoomLog(room, report);
            roomLogFacade.create(roomLog);
        }
        
        return report;
        
    }
    
    public void deleteReport(Report report) {
                
        List<BookingLog> allBookingLogs = em.createQuery(
            "SELECT b FROM BookingLog b WHERE b.report.id = :reportID", BookingLog.class)
            .setParameter("reportID", report.getId())
            .getResultList();
        
        List<RoomLog> allRoomLogs = em.createQuery(
            "SELECT r FROM RoomLog r WHERE r.report.id = :reportID", RoomLog.class)
            .setParameter("reportID", report.getId())
            .getResultList();
        
        List<SalaryLog> allSalaryLogs = em.createQuery(
            "SELECT s FROM SalaryLog s WHERE s.report.id = :reportID", SalaryLog.class)
            .setParameter("reportID", report.getId())
            .getResultList();
        
        for(BookingLog log : allBookingLogs) {
            bookingLogFacade.remove(log);
        }
        
        for(RoomLog log : allRoomLogs) {
            roomLogFacade.remove(log);
        }
        
        for(SalaryLog log : allSalaryLogs) {
            salaryLogFacade.remove(log);
        }
        
        this.remove(report);
        
    }
    
}
