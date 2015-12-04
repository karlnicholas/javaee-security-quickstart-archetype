#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.facade;

import java.util.List;

import javax.persistence.EntityManager;

import ${package}.model.Role;

/**
 * This class is a singleton that loads and holds all Role definitions from 
 * the database. Call RoleFacade.getInstance(EntityManager em) and then
 * getUserRole() or getAdminRole(). Very specific to this particular 
 * application. Should really only be used by UserFacade.
 * 
 * @author Karl Nicholas
 *
 */
public final class RoleFacade {
    private static EntityManager em;
    private List<Role> allRoles;
    //private constructor to avoid client applications to use constructor
    private RoleFacade(){
        allRoles = em.createNamedQuery(Role.LIST_AVAILABLE, Role.class).getResultList();
    }
    private static class SingletonHelper {
        private static final RoleFacade INSTANCE = new RoleFacade();
    }
    public static RoleFacade getInstance(EntityManager em){
        RoleFacade.em = em;
        return SingletonHelper.INSTANCE;
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

