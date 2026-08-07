package session;

import entity.RoomLog;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class RoomLogFacade extends AbstractFacade<RoomLog> {

    @PersistenceContext(unitName = "APU-HRMS-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RoomLogFacade() {
        super(RoomLog.class);
    }
    
    public RoomLogFacade(Class<RoomLog> entityClass) {
        super(entityClass);
    }
    
}
