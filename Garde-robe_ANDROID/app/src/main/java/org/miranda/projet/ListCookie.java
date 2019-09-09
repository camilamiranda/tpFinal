package org.miranda.projet;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;

public class ListCookie {

    public static List<Cookie> listCookie;

    public static void addCookie(Cookie cookie){
        if (listCookie == null)
            listCookie = new ArrayList<Cookie>();
        listCookie.add(cookie);
    }

    public static List<Cookie> getListCookie(){
        if (listCookie == null)
            listCookie = new ArrayList<Cookie>();
        return listCookie;
    }
}
