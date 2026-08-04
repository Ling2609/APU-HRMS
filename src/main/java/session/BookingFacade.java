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
}
