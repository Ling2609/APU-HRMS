package entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "booking_user")
public class BookingUser implements Serializable {

    public enum BookingUserRole {
        HOUSEKEEPER, CUSTOMER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookinguser_seq")
    @SequenceGenerator(name = "bookinguser_seq", sequenceName = "bookinguser_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingUserRole role;

    public BookingUser() {}

    public BookingUser(Booking booking, User user, BookingUserRole role) {
        this.booking = booking;
        this.user = user;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public BookingUserRole getRole() { return role; }
    public void setRole(BookingUserRole role) { this.role = role; }
}