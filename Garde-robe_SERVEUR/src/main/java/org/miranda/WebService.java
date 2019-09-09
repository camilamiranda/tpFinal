package org.miranda;

import com.google.gson.Gson;

import javax.ws.rs.*;
import java.util.List;

// vérifier que le webservice correspond avec le service et le mock

@Path("/")
public class WebService {

    private Gson gson;

    public WebService() {
        gson = new Gson();
    }

    @GET @Path("/hello")
    public String hello(){
        System.out.println("GET hello");
        return "hello";
    }

    @POST @Path("/users/register")
    public User createUser(EmailPassword emailPassword){
        System.out.println("POST createUser :: " + emailPassword.username);

        if (ListUsers.listUsers != null){
            for (User u:ListUsers.listUsers){
                if (u.username.equals(emailPassword.username))
                    throw new IllegalArgumentException("ERROR_USER_EXISTS");
            }
        }
        User user = new User(emailPassword.username, emailPassword.password);
        ListUsers.addUser(user);
        return user;
    }

    @POST @Path("/users/login")
    public User login(EmailPassword emailPassword){
        System.out.println("POST login :: " + emailPassword.username);

        if(ListUsers.listUsers != null){
            for (User u:ListUsers.listUsers) {
                if (u.username.equals(emailPassword.username) && u.password.equals(emailPassword.password))
                    return u;
                else if (u.username.equals(emailPassword.username) && !u.password.equals(emailPassword.password))
                    throw new IllegalArgumentException("ERROR_WRONG_PASSWORD");
                // envoyer un message si le mot de pass est incorrect ou si l'utilisateur n'existe pas
            }
            throw new IllegalArgumentException("ERROR_INVALID_USERNAME");
        }
        return null;
    }

    @POST @Path("/logout")
    public User logout(){
        System.out.println("POST logout");
        return null;
        // vérifier qu'un utilisateur est connecté
    }

    @GET @Path("/users/{userID}/items")
    public List<Item> getItemList(@PathParam("userID") int userID){
        System.out.println("GET userItemList :: " + userID);

        if (ListUsers.listUsers != null){
            for (User u:ListUsers.listUsers){
				if (u.id == userID && u.listItems != null)
					return u.listItems;
			}
        }

        // qu'est-ce qui arrive si la liste est vide

        return null;
    }

    @GET @Path("/users/{userID}/items/{itemID}")
    public Item getItemDetails(@PathParam("userID") int userID, @PathParam("itemID") int itemID){
		System.out.println("GET itemDetails :: " + userID + " :: " + itemID);

		if (ListUsers.listUsers != null){
			for (User u:ListUsers.listUsers){
				if (u.id == userID) {
					for (Item i:u.listItems){
						if (i.id == itemID)
							return i;
					}
				}
			}
		}

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return null;
	}

    @POST @Path("/users/{userID}/items/new")
    public Item createItem(@PathParam("userID") int userID, Item item) {
        System.out.println("POST createItem :: " + item.id);

        if (ListUsers.listUsers != null) {
            for (User u:ListUsers.listUsers) {
                if (u.id == userID) {
                    for (Item i:u.listItems) {
                        if (i.name == item.name)
                            throw new IllegalArgumentException("ERROR_INVALID_ITEM");
                    }
                    u.listItems.add(item);
                    return item;
                    // vérifier que l'item n'est pas déjà dans la liste, que le nom n'existe pas déjà
                }
            }
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
