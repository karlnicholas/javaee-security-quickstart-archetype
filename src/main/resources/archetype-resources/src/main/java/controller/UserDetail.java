#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import ${package}.facade.UserFacade;
import ${package}.util.Resources;

/**
 * Controller to update User information.
 * 
 * @author Karl Nicholas
 *
 */
@Model
public class UserDetail extends Principal {
    
    @Inject UserFacade userFacade;

    /**
     * Update/merge user fields
     * @return Navigation to /views/account.xhtml
     */
    public String update() {
        try {
            // update user
            userFacade.merge(getUser());
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Update unsuccessful", Resources.getRootErrorMessage(e)));
            return null;
        }
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "User Info Updated", ""));
        return "/views/account.xhtml";
    }

}
