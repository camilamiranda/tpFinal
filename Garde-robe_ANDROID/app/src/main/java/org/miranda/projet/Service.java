package org.miranda.projet;

import org.miranda.EmailPassword;
import org.miranda.Item;
import org.miranda.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Service {

    String endPoint = "http://10.0.2.2:8443/";

    @GET("/rest/hello")
    Call<String> hello();

    @POST("/rest/users/login")
    Call<User> login(@Body EmailPassword emailPassword);

    @POST("/rest/users/register")
    Call<User> createUser(@Body EmailPassword emailPassword);

    @POST("/rest/logout")
    Call<User> logout();


    @GET("/rest/users/{userID}/items")
    Call<List<Item>> getItemList(@Path("userID") int userID);

    @GET("/rest/users/{userID}/items/{itemID}")
    Call<Item> getItemDetails(@Path("userID") int userID, @Path("itemID") int itemID);

    @POST("/rest/users/{userID}/items/new")
    Call<Item> createItem(@Path("userID") int userID, @Body Item item);

}
