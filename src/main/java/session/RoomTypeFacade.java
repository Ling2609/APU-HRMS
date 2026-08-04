/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package session;

import entity.RoomType;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author Ling
 */
@Stateless
public class RoomTypeFacade extends AbstractFacade<RoomType> {

    @PersistenceContext(unitName = "APU-HRMS-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RoomTypeFacade() {
        super(RoomType.class);
    }
    
    public List<entity.RoomType> findAllRoomTypes() {
        return em.createQuery(
            "SELECT rt FROM RoomType rt ORDER BY rt.roomTypePrice", 
            entity.RoomType.class)
            .getResultList();
    }
}
