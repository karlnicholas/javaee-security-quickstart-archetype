#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package controller;

import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import service.UserSessionBean;

@ManagedBean
public class IndexController {
    
    @Inject private UserSessionBean userBean;
    
    private String userCountMessage;
    
    /**
     * Set UserCountMessage field with the number of registered users
     */
    public void updateUserCount() {
        userCountMessage = String.format("There are %d users", userBean.userCount() );
    }

    /**
     * UserCountMessage field
     * @return userCountMessage field.
     */
    public String getUserCountMessage() {
        return userCountMessage;
    }

    /**
     * UserCountMessage Field
     * @param userCountMessage field.
     */
    public void setUserCountMessage(String userCountMessage) {
        this.userCountMessage = userCountMessage;
    }

}
