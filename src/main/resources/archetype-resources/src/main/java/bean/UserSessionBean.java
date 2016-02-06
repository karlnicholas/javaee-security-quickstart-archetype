#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.bean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.DatatypeConverter;

import ${package}.model.Role;
import ${package}.model.User;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Session bean for accessing user functions. Register new users with encodeAndSave, which 
 * encodes the password. Some functions are accessible only by ADMIN roles, 
 * such as promote, demote and delete users. encodeAndSave and userCount is globally accessible.
 *   
 * @author Karl Nicholas
 *
 */
@Stateless
public class UserSessionBean {
    @Inject private EntityManager em;
    @Inject private RoleSingletonBean roleBean;

    /**
     * Register new users. Encodes the password and adds the "USER" role to the user's roles.
     * Returns null if user already exists.
     * @param user {@link User} to be encoded.
     * @return {@link User} with role added and password encoded, unless user already exists, then null.
     * @throws NoSuchAlgorithmException If configuration wrong. 
     */
    @PermitAll
    public User encodeAndSave(User user) throws NoSuchAlgorithmException {
        // sanity check to see if user already exists.
        TypedQuery<Long> q = em.createNamedQuery(User.COUNT_EMAIL, Long.class).setParameter("email", user.getEmail());
        if ( q.getSingleResult().longValue() > 0L ) {
            // show error condition
            return null;
        }
        // Encode password
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(user.getPassword().getBytes());
        user.setPassword( DatatypeConverter.printBase64Binary(hash) );
        // Add role "USER" to user.
        Role role = roleBean.getUserRole();
        List<Role> roles = new ArrayList<Role>();
        roles.add(em.merge(role));
        user.setRoles(roles);
        // Persist user.
        em.persist(user);
        em.flush();
        return user;
    }
    
    /**
     * Return the number of registered Users
     * @return number of registered Users
     */
    @PermitAll
    public Long userCount() {
        return em.createNamedQuery(User.USER_COUNT, Long.class).getSingleResult();
    }

    /**
     * Update the User's password
     * @param user User to update
     * @return User Updated User
     * @throws NoSuchAlgorithmException if no implementation for SHA-256 algorithm. 
     */
    @RolesAllowed({"USER"})
    public User updatePassword(User user) throws NoSuchAlgorithmException {
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(user.getPassword().getBytes());
        user.setPassword( DatatypeConverter.printBase64Binary(hash) ); 
        return em.merge(user);
    }

    /**
     * Merge user with Database
     * @param user User to merge
     * @return User Merged User
     */
    @RolesAllowed({"USER"})
    public User merge(User user) {
        return em.merge(user);
    }
    
    /**
     * return User for email.
     * @param email User email to search for.
     * @return User found for email.
     */
    @RolesAllowed({"USER"})
    public User findByEmail(String email) {
        return em.createNamedQuery(User.FIND_BY_EMAIL, User.class)
            .setParameter("email", email)
            .getSingleResult();
    }

    /**
     * Delete User by Database Id
     * @param id of User
     */
    @RolesAllowed({"ADMIN"})
    public void delete(Long id) {
        em.remove( em.find(User.class, id) );
    }
    
    /**
     * Find User by Database Id
     * @param id of user.
     * @return User or null if not exists
     */
    @RolesAllowed({"ADMIN"})
    public User findById(Long id) {
        return em.find(User.class, id);
    }
    
    /**
     * Get List of all Users
     * @return List of all Users
     */
    @RolesAllowed({"ADMIN"})
    public List<User> findAll() {
        return em.createNamedQuery(User.FIND_ALL, User.class).getResultList();
    }
    
    /**
     * Promote User to Admin by Database Id
     * @param id of User to be promoted.
     * @return User or null if not exists.
     */
    @RolesAllowed({"ADMIN"})
    public User promoteUser(Long id) {
        User user = em.find(User.class, id);
        user.getRoles().add(roleBean.getAdminRole());
        return em.merge( user );
    }
    
    /**
     * Demote User to User by Database Id
     * @param id of User to be demoted.
     * @return User or null if not exists
     */
    @RolesAllowed({"ADMIN"})
    public User demoteUser(Long id) {
        User user = em.find(User.class, id);
        Iterator<Role> rIt = user.getRoles().iterator();
        while ( rIt.hasNext()  ) {
            Role role = rIt.next();
            if ( role.getRole().equals("ADMIN")) rIt.remove();
        }
        return em.merge( user );
    }

    /**
     * Manually encode a password.
     * @param args nothing.
     * @throws Exception if anything wrong.
     */
    public static void main(String[] args) throws Exception {
        byte[] hash = MessageDigest.getInstance("SHA-256").digest("admin".getBytes());
        System.out.println( DatatypeConverter.printBase64Binary(hash) );
    }

}
