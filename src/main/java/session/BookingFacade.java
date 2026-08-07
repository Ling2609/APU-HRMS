/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package session;

import entity.Booking;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author Ling
 */
@Stateless
public class BookingFacade extends AbstractFacade<Booking> {

    @PersistenceContext(unitName = "APU-HRMS-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BookingFacade() {
        super(Booking.class);
    }
    
    public List<entity.Booking> findByCustomer(Long customerId) {
        return em.createQuery(
            "SELECT b FROM Booking b WHERE b.customer.id = :id ORDER BY b.estimatedCheckInTime DESC", 
            entity.Booking.class)
            .setParameter("id", customerId)
            .getResultList();
    }
    
    public List<Booking> findByStatus(Booking.BookingStatus status) {
        return em.createQuery(
            "SELECT b FROM Booking b WHERE b.bookingStatus = :status ORDER BY b.estimatedCheckInTime", 
            Booking.class)
            .setParameter("status", status)
            .getResultList();
    }
    
    public List<Booking> findAllBookings() {
        return em.createQuery(
            "SELECT b FROM Booking b ORDER BY b.id DESC", Booking.class)
            .getResultList();
    }
    
    public Booking findLatestByRoom(Long roomId) {
        try {
            return em.createQuery(
                "SELECT b FROM Booking b WHERE b.room.id = :roomId AND b.bookingStatus = :status ORDER BY b.id DESC", 
                Booking.class)
                .setParameter("roomId", roomId)
                .setParameter("status", Booking.BookingStatus.CHECKED_OUT)
                .setMaxResults(1)
                .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Booking> findCheckedOutByCustomer(Long customerId) {
        return em.createQuery(
            "SELECT b FROM Booking b WHERE b.customer.id = :id AND b.bookingStatus = :status ORDER BY b.id DESC",
            Booking.class)
            .setParameter("id", customerId)
            .setParameter("status", Booking.BookingStatus.CHECKED_OUT)
            .getResultList();
    }
    
    public boolean hasDuplicateRoomTypeBooking(Long customerId, Long roomTypeId,
            java.time.LocalDateTime checkIn, java.time.LocalDateTime checkOut) {
        Long count = em.createQuery(
                "SELECT COUNT(b) FROM Booking b WHERE b.customer.id = :customerId "
                + "AND b.room.roomType.id = :roomTypeId "
                + "AND b.bookingStatus IN :statuses "
                + "AND b.estimatedCheckInTime < :checkOut "
                + "AND b.estimatedCheckOutTime > :checkIn",
                Long.class)
                .setParameter("customerId", customerId)
                .setParameter("roomTypeId", roomTypeId)
                .setParameter("statuses", java.util.Arrays.asList(
                        Booking.BookingStatus.UNPAID,
                        Booking.BookingStatus.BOOKED,
                        Booking.BookingStatus.LATE,
                        Booking.BookingStatus.CHECKED_IN))
                .setParameter("checkIn", checkIn)
                .setParameter("checkOut", checkOut)
                .getSingleResult();
        return count > 0;
    }
    
    public void updateLateBookings() {
        List<Booking> booked = em.createQuery(
                "SELECT b FROM Booking b WHERE b.bookingStatus = :status",
                Booking.class)
                .setParameter("status", Booking.BookingStatus.BOOKED)
                .getResultList();

        java.time.LocalDate today = java.time.LocalDate.now();

        for (Booking b : booked) {
            if (b.getEstimatedCheckInTime()
                    .toLocalDate()
                    .isBefore(today)) {

                b.setBookingStatus(Booking.BookingStatus.LATE);
                em.merge(b);
            }
        }
    }
    
    public List<Booking> findActiveByCustomer(Long customerId) {
        return em.createQuery(
            "SELECT b FROM Booking b WHERE b.customer.id = :id AND b.bookingStatus IN :statuses",
            Booking.class)
            .setParameter("id", customerId)
            .setParameter("statuses", java.util.Arrays.asList(
                Booking.BookingStatus.UNPAID,
                Booking.BookingStatus.BOOKED,
                Booking.BookingStatus.CHECKED_IN,
                Booking.BookingStatus.LATE))
            .getResultList();
    }
}
