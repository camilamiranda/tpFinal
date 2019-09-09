package org.miranda.projet;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.miranda.projet.events.EventAddItem;
import org.miranda.projet.events.EventChangeList;
import org.miranda.Item;
import org.miranda.State;
import org.miranda.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {

    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

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

        Button btnAdd = findViewById(R.id.btnAdd);
        EditText inputType = findViewById(R.id.inputType);
        EditText inputColor = findViewById(R.id.inputColor);
        EditText inputDetails = findViewById(R.id.inputDetails);
        EditText inputMaterial = findViewById(R.id.inputMaterial);
        EditText inputSeason = findViewById(R.id.inputSeason);
        EditText inputGivenBy = findViewById(R.id.inputGivenBy);
        EditText inputImage = findViewById(R.id.inputImage);
        final EditText inputName = findViewById(R.id.inputName);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = new Item(inputName.getText().toString());
                MyBus.bus.post(new EventAddItem(item));
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

    @Subscribe
    public void Ajout(EventAddItem e){
        Service service = RetrofitUtils.get();

        final Item itemitem = e.item;

        service.createItem(Integer.parseInt(CurrentUser.user.id), itemitem).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                if (response.isSuccessful()) {
                    response.body().name = itemitem.name;
                    Intent i = new Intent(getApplicationContext(), ListActivity.class);
                    MyBus.bus.post(new EventChangeList(itemitem));
                    Log.i("TEST", "event change list start");
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "Bravo! Ajout de l'item " + response.body().name.toString(), Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Item du même nom existe déjà", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {

            }
        });
    }
}
