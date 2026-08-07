package session;

import entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class StaffFacade extends UserFacade {
    
    @PersistenceContext(unitName = "APU-HRMS-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public StaffFacade() {
        super(User.class);
    }
    
    public StaffFacade(Class<User> entityClass) {
        super(entityClass);
    }
    
}
