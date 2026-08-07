package session;

import entity.SalaryLog;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class SalaryLogFacade extends AbstractFacade<SalaryLog> {

    @PersistenceContext(unitName = "APU-HRMS-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SalaryLogFacade() {
        super(SalaryLog.class);
    }
    
    public SalaryLogFacade(Class<SalaryLog> entityClass) {
        super(entityClass);
    }
    
}
