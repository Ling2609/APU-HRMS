/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package session;

import entity.Message;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author Ling
 */
@Stateless
public class MessageFacade extends AbstractFacade<Message> {

    @PersistenceContext(unitName = "APU-HRMS-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MessageFacade() {
        super(Message.class);
    }
    
    public boolean hasCommented(Long bookingUserId) {
        Long count = em.createQuery(
            "SELECT COUNT(m) FROM Message m WHERE m.bookingUser.id = :id",
            Long.class)
            .setParameter("id", bookingUserId)
            .getSingleResult();
        return count > 0;
    }
    
    public List<entity.Message> findCommentsByCustomer(Long userId) {
        return em.createQuery(
            "SELECT m FROM Message m WHERE m.bookingUser.user.id = :userId AND m.messageType = :type ORDER BY m.id DESC",
            entity.Message.class)
            .setParameter("userId", userId)
            .setParameter("type", entity.Message.MessageType.COMMENT)
            .getResultList();
    }
    
}
