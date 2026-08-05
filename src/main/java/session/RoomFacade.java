/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package session;

import entity.Room;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author Ling
 */
@Stateless
public class RoomFacade extends AbstractFacade<Room> {

    @PersistenceContext(unitName = "APU-HRMS-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RoomFacade() {
        super(Room.class);
    }
    
    public Room findByRoomNumber(int roomNumber) {
        try {
            return em.createQuery(
                "SELECT r FROM Room r WHERE r.roomNumber = :num", Room.class)
                .setParameter("num", roomNumber)
                .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<entity.Room> findAvailableByType(Long roomTypeId) {
        return em.createQuery(
            "SELECT r FROM Room r WHERE r.roomType.id = :typeId AND r.roomStatus = :status", 
            entity.Room.class)
            .setParameter("typeId", roomTypeId)
            .setParameter("status", entity.Room.RoomStatus.FREE)
            .getResultList();
    }
    
    public List<entity.Room> findRoomsByStatus(entity.Room.RoomStatus status) {
        return em.createQuery(
            "SELECT r FROM Room r WHERE r.roomStatus = :status ORDER BY r.roomNumber", 
            entity.Room.class)
            .setParameter("status", status)
            .getResultList();
    }
    
    public List<entity.Room> findAvailableByTypeAndDates(Long roomTypeId,
            java.time.LocalDateTime checkIn, java.time.LocalDateTime checkOut) {
        return em.createQuery(
                "SELECT r FROM Room r WHERE r.roomType.id = :typeId "
                + "AND r.id NOT IN ("
                + "  SELECT b.room.id FROM Booking b "
                + "  WHERE b.bookingStatus IN :statuses "
                + "  AND b.estimatedCheckInTime < :checkOut "
                + "  AND b.estimatedCheckOutTime > :checkIn"
                + ")", entity.Room.class)
                .setParameter("typeId", roomTypeId)
                .setParameter("statuses", java.util.Arrays.asList(
                        entity.Booking.BookingStatus.UNPAID,
                        entity.Booking.BookingStatus.BOOKED,
                        entity.Booking.BookingStatus.CHECKED_IN))
                .setParameter("checkIn", checkIn)
                .setParameter("checkOut", checkOut)
                .getResultList();
    }
}
