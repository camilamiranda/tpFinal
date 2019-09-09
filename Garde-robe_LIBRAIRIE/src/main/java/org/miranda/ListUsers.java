package org.miranda;

import java.util.ArrayList;
import java.util.List;

public class ListUsers {

    public static List<User> listUsers = new ArrayList<User>();

    public static void addUser(User user){
        if (listUsers == null)
            listUsers = new ArrayList<User>();
        listUsers.add(user);
        user.id = listUsers.indexOf(user);
    }
}
