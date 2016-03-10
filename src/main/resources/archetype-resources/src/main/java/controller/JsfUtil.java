#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package controller;

import javax.enterprise.inject.Model;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

/**
 * Class for utilities for JSF pages.
 * 
 * @author Karl Nicholas
 *
 */
@Model
public class JsfUtil {
	/**
	 * Method to determine whether a component has a validation error condition.
	 * 
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
