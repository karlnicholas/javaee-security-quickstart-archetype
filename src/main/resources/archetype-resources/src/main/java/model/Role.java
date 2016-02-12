#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Role Entity. Holds list of unique Role Strings.
 *  
 * @author Karl Nicholas
 *
 */
@SuppressWarnings("serial")
@Table(name = "role")
@Entity
@NamedQueries({
    @NamedQuery(name = Role.LIST_AVAILABLE, query = "select r from Role r"), 
})
public class Role implements Serializable {
    public static final String LIST_AVAILABLE = "Role.listAvailable";

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable=false)
    private String role;

    /**
     * Get Role Name
     * @return Role Name
     */
    public String getRole() {
        return role;
    }
    /**
     * Set Role Name
     * @param role Role
     */
    public void setRole(String role) {
        this.role = role;
    }
    /**
     * Get Role Database Id
     * @return Database Id
     */
    public Long getId() {
        return id;
    }

}
