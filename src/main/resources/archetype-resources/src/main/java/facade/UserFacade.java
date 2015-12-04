#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.facade;

import java.io.IOException;
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

@Stateless
public class UserFacade {
    @Inject EntityManager em;

    /**
     * Register new users. Encodes the password and adds the "USER" role to the user's roles.
     * Returns null if user already exists.
     * @param user
     * @return user with role added and password encoded, unless user already exists, then null.
     * @throws NoSuchAlgorithmException 
     * @throws IOException 
     */
    @PermitAll
    public User encodeAndSave(User user) throws NoSuchAlgorithmException, IOException {
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
        Role role = RoleFacade.getInstance(em).getUserRole();
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
     * @param user
     * @return Updated User
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @RolesAllowed({"USER"})
    public User updatePassword(User user) throws IOException, NoSuchAlgorithmException {
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(user.getPassword().getBytes());
        user.setPassword( DatatypeConverter.printBase64Binary(hash) ); 
        return em.merge(user);
    }

    /**
     * Merge user with Database
     * @param user
     * @return Merged User
     */
    @RolesAllowed({"USER"})
    public User merge(User user) {
        return em.merge(user);
    }
    
    /**
     * return User for email.
     * @param email
     * @return User
     */
    @RolesAllowed({"USER"})
    public User findByEmail(String email) {
        return em.createNamedQuery(User.FIND_BY_EMAIL, User.class)
            .setParameter("email", email)
            .getSingleResult();
    }

    /**
     * Delete User by Database Id
     * @param id
     */
    @RolesAllowed({"ADMIN"})
    public void delete(Long id) {
        em.remove( em.find(User.class, id) );
    }
    
    /**
     * Find User by Database Id
     * @param id
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
     * Promote User by Database Id
     * @param id
     * @return User or null if not exists
     */
    @RolesAllowed({"ADMIN"})
    public User promoteUser(Long id) {
        User user = em.find(User.class, id);
        user.getRoles().add(RoleFacade.getInstance(em).getAdminRole());
        return em.merge( user );
    }
    
    /**
     * Demote User by Database Id
     * @param id
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
     * Manually encode a password
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        byte[] hash = MessageDigest.getInstance("SHA-256").digest("admin".getBytes());
        System.out.println( DatatypeConverter.printBase64Binary(hash) );
    }

}
