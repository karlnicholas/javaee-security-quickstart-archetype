#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller.user;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import ${package}.facade.UserFacade;
import ${package}.model.User;
import ${package}.util.Resources;

@Model
public class Register {

    @Inject FacesContext context;
    @Inject UserFacade userFacade;
    
    private User newUser;

    @PostConstruct
    public void initNewUser() {
        newUser = new User();
    }
    
    /**
     * Register a new user
     * 
     * @return String navigation to /views/account.xhtml
     */
    public String register() {
        try {
            // save the password before encoding
            String password = newUser.getPassword();
            User user = userFacade.encodeAndSave(newUser);
            if ( user == null ) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration failed!", "User Already Exists" ));
                return null;
            } else {
                // login user            
                ExternalContext externalContext = context.getExternalContext();
                HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
                request.login(newUser.getEmail(), password);
                externalContext.getSessionMap().put("user", user);                
            }
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed!", Resources.getRootErrorMessage(e) ));
            return null;
        }
        // navigate
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration Successful!", "" ));
        return "/views/account.xhtml";
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
}
