#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller.user;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import ${package}.util.Resources;

@Model
public class Logout {

    @Inject FacesContext context;

    /**
     * Logout current user
     * 
     * @return Naviation to /views/account.xhtml
     */
    public String logout() {
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try {
            request.logout();
            externalContext.invalidateSession();
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Logout Failed!", Resources.getRootErrorMessage(e) ));
            return null;
        }
        // navigate
        return "/views/account.xhtml?faces-redirect=true";
    }

}
