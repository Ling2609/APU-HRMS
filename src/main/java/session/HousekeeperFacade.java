package session;

import entity.Booking;
import entity.Housekeeper;
import entity.Message;
import entity.Room;
import entity.User;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Stateless
public class HousekeeperFacade extends StaffFacade {
    
    @EJB
    private BookingFacade bookingFacade;
    @EJB
    private BookingUserFacade bookingUserFacade;
    @EJB
    private MessageFacade messageFacade;

    @PersistenceContext(unitName = "APU-HRMS-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public HousekeeperFacade() {
        super(User.class);
    }
    
    public HousekeeperFacade(Class<User> entityClass) {
        super(entityClass);
    }
    
    public ArrayList<Booking> getAllHousekeeperBookings(Housekeeper housekeeper) {
        
        List<Booking> allBookings = bookingFacade.findAll();
        ArrayList<Booking> returnArrayList = new ArrayList<>();
        
        for(Booking booking : allBookings) {
            
            try {
                if(Objects.equals(booking.getCheckOutHousekeeper().getId(), housekeeper.getId())) {
                    returnArrayList.add(booking);
                }
            }
            catch(Exception e) {}
            
        }
        
        return returnArrayList;
        
    }
    
    public ArrayList<Message> getAllHousekeeperFeedback(Housekeeper housekeeper) {
        
        List<Message> allMessages = messageFacade.findAll();
        ArrayList<Message> returnArrayList = new ArrayList<>();
        
        for(Message message : allMessages) {
            if(Objects.equals(message.getBookingUser().getUser().getId(), housekeeper.getId())) {
                returnArrayList.add(message);
            }
        }
        
        return returnArrayList;
        
    }
    
    public ArrayList<Booking> getAssignedHousekeeperBookings(Housekeeper housekeeper) {
        
        List<Booking> allBookings = bookingFacade.findAll();
        ArrayList<Booking> returnArrayList = new ArrayList<>();
                    
        for(Booking booking : allBookings) {
            
            try {
                if(Objects.equals(booking.getCheckOutHousekeeper().getId(), housekeeper.getId()) && 
                    booking.getRoom().getRoomStatus() == Room.RoomStatus.CLEANING) {
                    returnArrayList.add(booking);
                }
            }
            catch(Exception e) {}
            
        }
        
        return returnArrayList;
        
    }
    
    public void completeTask(String bookingID) {
        
        Booking booking = bookingFacade.find(Long.valueOf(bookingID));
        booking.getRoom().setRoomStatus(Room.RoomStatus.FREE);
        bookingFacade.edit(booking);

    }
    
}
