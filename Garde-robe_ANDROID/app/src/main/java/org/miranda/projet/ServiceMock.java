//package org.miranda.projet;
//
//import org.miranda.EmailPassword;
//import org.miranda.Item;
//import org.miranda.State;
//import org.miranda.User;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.mock.BehaviorDelegate;
//
//public class ServiceMock implements Service {
//
//    BehaviorDelegate<Service> delegate;
//
//    public ServiceMock(BehaviorDelegate<Service> delegate) {
//
//        this.delegate = delegate;
//    }
//
//    @Override
//    public Call<List<Item>> getItemList(int userID) {
//        List<Item> lstT = new ArrayList<>();
//        lstT.add(new Item("Chandail noir", State.propre));
//        lstT.add(new Item("Pantalon rouge", State.propre));
//        lstT.add(new Item("Pantalon noir", State.sale));
//        lstT.add(new Item("Chandail blanc", State.perdu));
//        return delegate.returningResponse(lstT).getItemList(userID);
//    }
//
//    @Override
//    public Call<Item> getItemDetails(int userID, int itemID) {
//        return null;
//    }
//
//    @Override
//    public Call<Item> createItem(int userID, Item item) {
//        return null;
//    }
//
////    @Override
////    public Call<Item> getItemDetails(int itemID) {
////        Item itemT = new Item("Chandail noir", State.propre);
////        itemT.type = "Chandail";
////        itemT.color = "Noir";
////        itemT.details = "Sans manches";
////        itemT.material = "Côton";
////        itemT.season = "Toutes";
////        itemT.givenBy = "S/O";
////        return delegate.returningResponse(itemT).getItemDetails(itemID);
////  }
//
////    @Override
////    public Call<Item> createItem(Item item) {
////        return delegate.returningResponse(new Item("Chandail rouge", State.propre)).createItem(item);
////    }
//
//    @Override
//    public Call<String> hello() {
//        return null;
//    }
//
//    @Override
//    public Call<User> login(EmailPassword emailPassword) {
//        User useruser = new User("popi@popo", "123456");
//        return delegate.returningResponse(useruser).login(emailPassword);
//    }
//
//    @Override
//    public Call<User> createUser(EmailPassword emailPassword) {
//        return delegate.returningResponse(new User("Pipo", "1234")).createUser(emailPassword);
//    }
//
//    @Override
//    public Call<User> logout() {
//        return delegate.returningResponse(null).logout();
//    }
//}
