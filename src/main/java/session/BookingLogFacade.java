package session;

import entity.BookingLog;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class BookingLogFacade extends AbstractFacade<BookingLog> {

    @PersistenceContext(unitName = "APU-HRMS-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BookingLogFacade() {
        super(BookingLog.class);
    }
    
    public BookingLogFacade(Class<BookingLog> entityClass) {
        super(entityClass);
    }
    
}
