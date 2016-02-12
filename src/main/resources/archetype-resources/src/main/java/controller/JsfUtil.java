#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package controller;

import javax.enterprise.inject.Model;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

@Model
public class JsfUtil {
	public boolean isValid(String clientId) {
        UIComponent comp = FacesContext.getCurrentInstance().getViewRoot().findComponent(clientId);
        if(comp instanceof UIInput) {
            return ((UIInput)comp).isValid();
        }
        throw new IllegalAccessError();
    }
}
