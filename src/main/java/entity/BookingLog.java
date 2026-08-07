package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookinglog")
public class BookingLog implements Serializable  { 
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookinglog_seq")
    @SequenceGenerator(name = "bookinglog_seq", sequenceName = "bookinglog_seq", allocationSize = 1)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
            
    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private User staff;

    @Column(nullable = false)
    private LocalDateTime estimatedCheckInTime;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    @Column(nullable = false)
    private LocalDateTime estimatedCheckOutTime;

    private double payment;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Booking.BookingStatus bookingStatus;

    @ManyToOne
    @JoinColumn(name = "housekeeper_id")
    private User checkOutHousekeeper;

    public BookingLog() {}

    public BookingLog(Booking booking, Report report) {
        this.customer = booking.getCustomer();
        this.staff = booking.getStaff();
        this.estimatedCheckInTime = booking.getEstimatedCheckInTime();
        this.estimatedCheckOutTime = booking.getEstimatedCheckOutTime();
        this.checkInTime = booking.getCheckInTime();
        this.checkOutTime = booking.getCheckOutTime();
        this.payment = booking.getPayment();
        this.room = booking.getRoom();
        this.bookingStatus = booking.getBookingStatus();
        this.report = report;
        this.booking = booking;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }
    
    public User getStaff() { return staff; }
    public void setStaff(User staff) { this.staff = staff; }
    
    public LocalDateTime getEstimatedCheckInTime() { return estimatedCheckInTime; }
    public void setEstimatedCheckInTime(LocalDateTime t) { this.estimatedCheckInTime = t; }
    
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime t) { this.checkInTime = t; }
    
    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime t) { this.checkOutTime = t; }
    
    public LocalDateTime getEstimatedCheckOutTime() { return estimatedCheckOutTime; }
    public void setEstimatedCheckOutTime(LocalDateTime t) { this.estimatedCheckOutTime = t; }
    
    public double getPayment() { return payment; }
    public void setPayment(double payment) { this.payment = payment; }
    
    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }
    
    public Booking.BookingStatus getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(Booking.BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }
    
    public User getCheckOutHousekeeper() { return checkOutHousekeeper; }
    public void setCheckOutHousekeeper(User u) { this.checkOutHousekeeper = u; }
    
    public Long getBookingID() { return this.booking.getId(); }
    public void setBookingID(Long input) { this.booking.setId(input); }
    
    public Long getReportID() { return this.report.getId(); }
    public void setReportID(Long input) { this.report.setId(input); }
    
}