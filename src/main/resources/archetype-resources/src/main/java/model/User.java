#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * User Entity. Holds everything needed for registering users on the system.
 * 
 * @author Karl Nicholas
 *
 */
@NamedQueries({
    @NamedQuery(name = User.FIND_BY_EMAIL, query = "select u from User u where u.email = :email"), 
    @NamedQuery(name = User.COUNT_EMAIL, query = "select Count(u.email) from User u where u.email = :email"), 
    @NamedQuery(name = User.FIND_ALL, query = "select u from User u"), 
    @NamedQuery(name = User.USER_COUNT, query = "select count(u) from User u"), 
})
@SuppressWarnings("serial")
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"),name = "user")
@Entity
public class User implements Serializable {
    public static final String FIND_BY_EMAIL = "User.findByEmail";
    public static final String COUNT_EMAIL = "User.countEmail";
    public static final String FIND_ALL = "User.findAll";
    public static final String USER_COUNT = "User.userCount";

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotNull(message="{email.required}")
    @Pattern(regexp = "[A-Za-z0-9!${symbol_pound}${symbol_dollar}%&'*+/=?^_`{|}~-]+(?:${symbol_escape}${symbol_escape}."
            + "[A-Za-z0-9!${symbol_pound}${symbol_dollar}%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?${symbol_escape}${symbol_escape}.)+[A-Za-z0-9]"
            + "(?:[A-Za-z0-9-]*[A-Za-z0-9])?",
            message = "{invalid.email}")
    private String email;

    @XmlTransient
    @NotNull(message="{password.required}")
    private String password;

    private String firstName;
    private String lastName;
    
    @ManyToMany(fetch=FetchType.EAGER)
    private List<Role> roles;

    /**
     * Get Users E-Mail
     * @return Users E-Mail
     */
    public String getEmail() {
        return email;
    }
    /**
     * Set User's E-Mail
     * @param email email.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Get User's Password
     * @return User's Password
     */
    public String getPassword() {
        return password;
    }
    /**
     * Set User's Password
     * @param password field.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Get Users's First Name
     * 
     * @return First Name
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * Set User's First Name
     * 
     * @param firstName User First Name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * Get Users's Last Name
     * 
     * @return String Last Name
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * Set User's Last Name
     * 
     * @param lastName User's Last Name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * Get User's Database Id
     * @return User's Database Id
     */
    public Long getId() {
        return id;
    }
    /**
     * Get Roles associated with User
     * @return List of Roles
     */
    public List<Role> getRoles() {
        return roles;
    }
    /**
     * Set Roles associated with User
     * @param roles to be set.
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    /**
     * Check to see if the User is has Admin role
     * 
     * @return true if admin
     */
    @XmlTransient
    public boolean isAdmin() {
        for ( Role role: roles ) {
            if ( role.getRole().equals("ADMIN")) return true;
        }
        return false;
    }

}
