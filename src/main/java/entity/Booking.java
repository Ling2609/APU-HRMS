package entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking implements Serializable {

    public enum BookingStatus {
        UNPAID, BOOKED, LATE, CHECKED_IN, CHECKED_OUT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_seq")
    @SequenceGenerator(name = "booking_seq", sequenceName = "booking_seq", allocationSize = 1)
    private Long id;

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
    private BookingStatus bookingStatus;

    @ManyToOne
    @JoinColumn(name = "housekeeper_id")
    private User checkOutHousekeeper;

    public Booking() {}

    public Booking(User customer, User staff, LocalDateTime estimatedCheckInTime,
                   LocalDateTime estimatedCheckOutTime, double payment,
                   Room room, BookingStatus bookingStatus) {
        this.customer = customer;
        this.staff = staff;
        this.estimatedCheckInTime = estimatedCheckInTime;
        this.estimatedCheckOutTime = estimatedCheckOutTime;
        this.payment = payment;
        this.room = room;
        this.bookingStatus = bookingStatus;
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
    public BookingStatus getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }
    public User getCheckOutHousekeeper() { return checkOutHousekeeper; }
    public void setCheckOutHousekeeper(User u) { this.checkOutHousekeeper = u; }
}