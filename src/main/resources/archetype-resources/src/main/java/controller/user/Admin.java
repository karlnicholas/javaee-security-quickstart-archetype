#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller.user;

import java.util.List;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import ${package}.bean.UserSessionBean;
import ${package}.model.User;

/**
 * Controller to update User information.
 * 
 * @author Karl Nicholas
 *
 */
@Model
public class Admin extends Principal {
    
    @Inject private UserSessionBean userBean;
    @Inject private FacesContext context;

    /**
     * Return a list of users
     * @return List of users
     */
    public List<User> getUsers() {
        return userBean.findAll();
    }

    /**
     * Remove a user based on id.
     * @param id of User to be removed.
     */
    public void removeUser(Long id) {
        if ( getUser().getId() == id ) throw new RuntimeException("Cannot change current user!");
        userBean.delete(id);
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User removed", "") );
    }

    /**
     * Promote user to administrator based on id
     * @param id of User to be promoted.
     */
    public void promoteUser(Long id) {
        if ( getUser().getId() == id ) return;
        userBean.promoteUser(id);
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User promoted to administrator", "") );
    }

    /**
     * Demote a user, based on id, by removing ADMIN role.
     * @param id of User to be demoted. 
     */
    public void demoteUser(Long id) {
        if ( getUser().getId() == id ) return;
        userBean.demoteUser(id);
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User demoted to user only", "") );
    }

}
