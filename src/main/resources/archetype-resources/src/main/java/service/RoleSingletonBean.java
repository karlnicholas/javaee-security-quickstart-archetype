#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import model.Role;

/**
 * This class is a singleton that loads and holds all Role definitions from 
 * the database. Call RoleFacade.getInstance(EntityManager em) and then
 * getUserRole() or getAdminRole(). Very specific to this particular 
 * application. Should really only be used by UserFacade.
 * 
 * @author Karl Nicholas
 *
 */
@Singleton
public final class RoleSingletonBean {
    private List<Role> allRoles;
    @Inject private EntityManager em;
    /**
     * load all roles 
     */
    @PostConstruct
    protected void postConstruct() {
        allRoles = em.createNamedQuery(Role.LIST_AVAILABLE, Role.class).getResultList();
    }
    /**
     * Get the USER Role
     * @return USER Role
     */
    public Role getUserRole()  {
        for ( Role role: allRoles ) {
            if ( role.getRole().equals("USER")) return role;
        }
        throw new RuntimeException("Role USER not found"); 
    }
    /**
     * Get the ADMIN Role
     * @return ADMIN Role
     */
    public Role getAdminRole()  {
        for ( Role role: allRoles ) {
            if ( role.getRole().equals("ADMIN")) return role;
        }
        throw new RuntimeException("Role ADMIN not found"); 
    }
}

