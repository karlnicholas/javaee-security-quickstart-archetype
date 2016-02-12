#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package controller.user;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import model.User;
import service.UserSessionBean;

/**
 * Support bean for views. Holds the currently logged in user, if any. 
 * Has isLoggedIn() method.
 * 
 * @author Karl Nicholas.
 *
 */
@Model
public class Principal {
    
    @Inject private FacesContext context;
    @Inject private UserSessionBean userBean;
    private User user;
        
    @PostConstruct
    public void postConstruct() {
        ExternalContext externalContext = context.getExternalContext();
        user = (User)externalContext.getSessionMap().get("user");
        // in case went to a specific URL
        if ( user == null ) {
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            java.security.Principal principal = request.getUserPrincipal();
            if ( principal != null ) {
                try {
                    user = userBean.findByEmail(principal.getName());
                } catch (Exception ignored) {
                    // logout whoever and set user to null.
                    try {
                        ((HttpServletRequest) externalContext.getRequest()).logout();
                    } catch (ServletException alsoIgnored) {}
                    externalContext.invalidateSession();
                    user = null;
                }
            }
        }
    }

    /**
     * True if a user is logged in.
     * 
     * @return True if Logged In
     */
    public boolean isLoggedIn() {
        return user != null;
    }

    /**
     * True if a user has the ADMIN role.
     * 
     * @return True if ADMIN
     */
    public boolean isAdmin() {
        if ( user == null ) return false;
        return user.isAdmin();
    }

    /**
     * Currently logged user or null.
     *  
     * @return User
     */
    public User getUser() {
        return user;
    }

}
