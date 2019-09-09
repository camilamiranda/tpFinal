package org.miranda.projet;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.miranda.projet.events.EventAddItem;
import org.miranda.projet.events.EventChangeList;
import org.miranda.projet.events.EventChangeState;
import org.miranda.projet.events.EventDetailItem;
import org.miranda.Item;
import org.miranda.State;
import org.miranda.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {

    List<Item> listItemsUser = new ArrayList<>();
    ActionBarDrawerToggle toggle;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

//        MyBus.bus.register(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(i);
            }
        });

        final ListView lv = findViewById(R.id.lv);

        adapter = new Adapter(this);
        adapter.addAll(listItemsUser);
        lv.setAdapter(adapter);

        String s = getIntent().getStringExtra("NOM");
        setTitle("Garde-Robe de " + CurrentUser.user.username);

        ///// DRAWER /////
        NavigationView navView = findViewById(R.id.navigation);
        final DrawerLayout drawer_layout = findViewById(R.id.drawer_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu_list)
                {
                    //Toast.makeText(getApplicationContext(), "Click 1", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), ListActivity.class);
                    startActivity(i);
                }
                else if(item.getItemId() == R.id.menu_add)
                {
                    //Toast.makeText(getApplicationContext(), "Click 2", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), AddActivity.class);
                    startActivity(i);
                }
                else if(item.getItemId() == R.id.menu_logOut)
                {
                    //Toast.makeText(getApplicationContext(), "Click 3", Toast.LENGTH_SHORT).show();
                    Service service = RetrofitUtils.get();
                    service.logout().enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Intent i = new Intent(getApplicationContext(), LogActivity.class);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), "Bravo Déconnexion", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                }
                drawer_layout.closeDrawers();
                return false;
            }
        });
        toggle = new ActionBarDrawerToggle(
                this,
                drawer_layout,
                R.string.open_drawer,
                R.string.close_drawer);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();


        //Mock//
        Service service = RetrofitUtils.get();
        service.getItemList(Integer.parseInt(CurrentUser.user.id)).enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                List<Item> lstItems = response.body();
                for (Item i:lstItems) {
                    Log.i("RETROFIT", i.name);
                    listItemsUser.add(i);
                }
                adapter.addAll(listItemsUser);
                lv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {

            }
        });


    }

    //Drawer//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        toggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        MyBus.bus.unregister(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        MyBus.bus.register(this);
        super.onResume();
    }



//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        MyBus.bus.unregister(this);
//    }

    @Subscribe
    public void itemDetail(final EventDetailItem e){
        //Log.i("TEST", "event item detail");
        Service service = RetrofitUtils.get();
        service.getItemDetails(Integer.parseInt(CurrentUser.user.id), Integer.parseInt(e.item.id)).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                i.putExtra("ItemId", e.item.id);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {

            }
        });
    }

    @Subscribe
    public void changeList(EventChangeList e){
        Log.i("TEST", "event change list");
        listItemsUser.add(e.item);
        adapter.clear();
        adapter.addAll(listItemsUser);
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void changeState(EventChangeState e){
        Log.i("TEST", "event change state");
        if (e.item.state == State.propre){
            e.item.state = State.sale;
        }
        else if (e.item.state == State.sale){
            e.item.state = State.propre;
        }
        else if (e.item.state == State.perdu){
            e.item.state = State.sale;
        }
        adapter.clear();
        adapter.addAll(listItemsUser);
        adapter.notifyDataSetChanged();
    }
}
