package org.miranda.projet;

import org.miranda.User;

public class CurrentUser {

    public static User user;
    public static void setCurrentUser(User u){
        user = u;
    }

}
