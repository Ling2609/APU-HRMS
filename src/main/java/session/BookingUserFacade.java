/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package session;

import entity.BookingUser;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 *
 * @author Ling
 */
@Stateless
public class BookingUserFacade extends AbstractFacade<BookingUser> {

    @PersistenceContext(unitName = "APU-HRMS-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BookingUserFacade() {
        super(BookingUser.class);
    }
    
    public boolean hasHousekeeperAssigned(Long bookingId) {
        Long count = em.createQuery(
            "SELECT COUNT(bu) FROM BookingUser bu WHERE bu.booking.id = :bookingId AND bu.role = :role", 
            Long.class)
            .setParameter("bookingId", bookingId)
            .setParameter("role", entity.BookingUser.BookingUserRole.HOUSEKEEPER)
            .getSingleResult();
        return count > 0;
    }
    
    public BookingUser findByBookingAndRole(Long bookingId, BookingUser.BookingUserRole role) {
        try {
            return em.createQuery(
                "SELECT bu FROM BookingUser bu WHERE bu.booking.id = :bookingId AND bu.role = :role",
                BookingUser.class)
                .setParameter("bookingId", bookingId)
                .setParameter("role", role)
                .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
