/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package session;

import entity.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author Ling
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "APU-HRMS-PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
    public User findByNameAndPassword(String name, String password) {
        try {
            return em.createQuery(
                "SELECT u FROM User u WHERE u.name = :name AND u.password = :password", User.class)
                .setParameter("name", name)
                .setParameter("password", password)
                .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public User findByIdentification(String identification) {
        try {
            return em.createQuery(
                "SELECT u FROM User u WHERE u.identification = :ic", User.class)
                .setParameter("ic", identification)
                .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<User> findAllCustomers() {
        return em.createQuery(
            "SELECT u FROM User u WHERE u.role = :role ORDER BY u.name", User.class)
            .setParameter("role", User.Role.CUSTOMER)
            .getResultList();
    }

    public List<User> searchCustomers(String keyword) {
        return em.createQuery(
            "SELECT u FROM User u WHERE u.role = :role AND (LOWER(u.name) LIKE :kw OR LOWER(u.identification) LIKE :kw) ORDER BY u.name", User.class)
            .setParameter("role", User.Role.CUSTOMER)
            .setParameter("kw", "%" + keyword.toLowerCase() + "%")
            .getResultList();
    }
    
    public List<User> findAllHousekeepers() {
        return em.createQuery(
            "SELECT u FROM User u WHERE u.role = :role ORDER BY u.name", User.class)
            .setParameter("role", User.Role.HOUSEKEEPER)
            .getResultList();
    }
}
