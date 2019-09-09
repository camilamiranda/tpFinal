package org.miranda;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {

    public int id;
    public String username;
    public String password;
    public List<Item> listItems;

    public User(String un, String pw) {
        username = un;
        password = pw;
        listItems = new ArrayList<Item>();
    }
}
