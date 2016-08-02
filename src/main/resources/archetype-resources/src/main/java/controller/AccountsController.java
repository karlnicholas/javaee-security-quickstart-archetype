#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import model.User;
import service.UserSessionBean;
import util.Resources;

/**
 * <p>Handles all of the logic for user accounts and security.</p> 
 * <p>Login, logout, and register user methods.</p>
 * <p>Methods for JSF pages, get current user, test if the user is an admin or not.</p>
 * <p>Methods for handling user details and password updates.</p>
 * <p>Administrator methods for promoting and deleting users.</p>
 * <p>JSF utility to see if a component has been set to an error state.</p>  
 * 
 * @author Karl Nicholas
 *
 */
@ManagedBean
public class AccountsController {
    @Inject private FacesContext facesContext;
    @Inject private UserSessionBean userService;
    private static final String NAV_ACCOUNTS = "/views/accounts/accounts.xhtml";
    private static final String NAV_ACCOUNTS_REDIRECT = "/views/accounts/accounts.xhtml?faces-redirect=true";

    private String email;
    private String password;

    private User newUser;
    private User currentUser;

    private String passwordConfirmation;
    
    @PostConstruct
    public void postConstruct() {
        ExternalContext externalContext = facesContext.getExternalContext();
        currentUser = (User)externalContext.getSessionMap().get("user");
        // in case went to a specific URL
        if ( currentUser == null ) {
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            java.security.Principal principal = request.getUserPrincipal();
            if ( principal != null ) {
                try {
                	currentUser = userService.findByEmail(principal.getName());
                } catch (Exception ignored) {
                    // logout whoever and set user to null.
                    try {
                        ((HttpServletRequest) externalContext.getRequest()).logout();
                    } catch (ServletException alsoIgnored) {}
                    externalContext.invalidateSession();
                    currentUser = null;
                }
            }
        }
        newUser = new User();
    }

	/**
	 * True if a user is logged in.
	 * 
	 * @return True if Logged In
	 */
	public boolean isLoggedIn() {
	    return currentUser != null;
	}
	
	/**
	 * True if a user has the ADMIN role.
	 * 
	 * @return True if ADMIN
	 */
	public boolean isAdmin() {
	    if ( currentUser == null ) return false;
	    return currentUser.isAdmin();
	}
	
	/**
	 * Currently logged user or null.
	 *  
	 * @return User
	 */
	public User getCurrentUser() {
	    return currentUser;
	}
	

    /**
     * Login a user using the email and password fields.
     * 
     * @return String navigation to /views/accounts/accounts.xhtml
     */
    public String login() {
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        if ( request.getUserPrincipal() == null ) {
            try {
            	// Ignorecase is handled by the database, see create.sql.
                request.login(email, password);
                currentUser = userService.findByEmail(email);
                externalContext.getSessionMap().put("user", currentUser);                
            } catch (ServletException ignored) {
                // Handle unknown username/password in request.login().
                facesContext.addMessage(null, new FacesMessage("Login Failed!", ""));
                return null;
            }
        } 
        return NAV_ACCOUNTS_REDIRECT;
    }

    /**
     * Email Field
     * @return Email field.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Email Field
     * @param email field.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Password Field
     * @return Password field.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Password Field
     * @param password field.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Logout current user
     * 
     * @return Naviation to /views/accounts/accounts.xhtml
     */
    public String logout() {
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try {
            request.logout();
            externalContext.invalidateSession();
        } catch (ServletException e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Logout Failed!", Resources.getRootErrorMessage(e) ));
            return null;
        }
        // navigate
        return NAV_ACCOUNTS_REDIRECT;
    }

    /**
     * Register a new user
     * 
     * @return String navigation to /views/accounts/accounts.xhtml
     */
    public String register() {
        try {
            // save the password before encoding
            String password = newUser.getPassword();
            currentUser = userService.encodeAndSave(newUser);
            if ( currentUser == null ) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration failed!", "User Already Exists" ));
                return null;
            } else {
                // login user            
                ExternalContext externalContext = facesContext.getExternalContext();
                HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            	// Ignorecase is handled by the database, see create.sql.
                request.login(newUser.getEmail(), password);
                externalContext.getSessionMap().put("user", currentUser);
            }
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed!", Resources.getRootErrorMessage(e) ));
            return null;
        }
        // navigate
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration Successful!", "" ));
        return NAV_ACCOUNTS;
    }

    /**
     * New User Field
     * @return newUser field.
     */
    public User getNewUser() {
        return newUser;
    }
    /**
     * New User Field
     * @param newUser field.
     */
    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    /**
     * Check user.password against passwordConfirmation, encode password, and merge user.
     * @return String navigation to /views/accounts/accounts.xhtml
     */
    public String updatePassword() {
        try {
            // check password confirmation.
            if ( !currentUser.getPassword().equals(passwordConfirmation) ) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Passwords Must Match", ""));
                return null;
            }
            // update user
            userService.merge(userService.updatePassword(currentUser));
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update failed", Resources.getRootErrorMessage(e)));
            return null;
        }
        // message and navigation
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password Updated", ""));
        return NAV_ACCOUNTS;
    }

    /**
     * Password Confirmation Field
     * @return passwordConfirmation field.
     */
    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    /**
     * Password Confirmation Field
     * @param passwordConfirmation field.
     */
    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    /**
     * Update/merge user fields
     * @return Navigation to /views/accounts/accounts.xhtml
     */
    public String update() {
        try {
            // update user
            userService.merge(currentUser);
        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update unsuccessful", Resources.getRootErrorMessage(e)));
            return null;
        }
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User Info Updated", ""));
        return NAV_ACCOUNTS;
    }

    /**
     * Return a list of users
     * @return List of users
     */
    public List<User> getUsers() {
        return userService.findAll();
    }

    /**
     * Remove a user based on id.
     * @param id of User to be removed.
     */
    public void removeUser(Long id) {
        if ( currentUser.getId() == id ) throw new RuntimeException("Cannot change current user!");
        userService.delete(id);
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User removed", "") );
    }

    /**
     * Promote user to administrator based on id
     * @param id of User to be promoted.
     */
    public void promoteUser(Long id) {
        if ( currentUser.getId() == id ) return;
        userService.promoteUser(id);
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User promoted to administrator", "") );
    }

    /**
     * Demote a user, based on id, by removing ADMIN role.
     * @param id of User to be demoted. 
     */
    public void demoteUser(Long id) {
        if ( currentUser.getId() == id ) return;
        userService.demoteUser(id);
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User demoted to user only", "") );
    }

	/**
	 * Method to determine whether a component has a validation error condition.
	 * 
	 * @author http://stackoverflow.com/questions/24329504/how-to-check-if-jsf-component-is-valid-in-xhtml
	 * @param clientId of component, e.g. id="test"
	 * @return true if no validation problem.
	 */
	public boolean isValid(String clientId) {
        UIComponent comp = FacesContext.getCurrentInstance().getViewRoot().findComponent(clientId);
        if(comp instanceof UIInput) {
            return ((UIInput)comp).isValid();
        }
        throw new IllegalAccessError();
    }
	
}
