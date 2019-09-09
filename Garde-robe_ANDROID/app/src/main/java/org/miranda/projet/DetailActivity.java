package org.miranda.projet;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
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
import android.widget.TextView;
import android.widget.Toast;

import org.miranda.Item;
import org.miranda.State;
import org.miranda.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    ActionBarDrawerToggle toggle;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

        final Button btnState1 = findViewById(R.id.btnState1);
        final Button btnState2 = findViewById(R.id.btnState2);
        final Button btnState3 = findViewById(R.id.btnState3);
        final TextView tv1 = findViewById(R.id.textType);
        final TextView tv2 = findViewById(R.id.textColor);
        final TextView tv3 = findViewById(R.id.textDetails);
        final TextView tv4 = findViewById(R.id.textMaterial);
        final TextView tv5 = findViewById(R.id.textSeason);
        final TextView tv6 = findViewById(R.id.textGivenBy);

        Service service = RetrofitUtils.get();
        service.getItemDetails(Integer.parseInt(CurrentUser.user.id), getIntent().getIntExtra("ItemId", 0)).enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Log.i("TEST", "event item detail 2");
                item = response.body();
                String s = item.state.toString();
                if (s == State.propre.toString()){
                    btnState1.setPressed(true);
                }
                else if(s == State.sale.toString()){
                    btnState2.setPressed(true);
                }
                else{
                    btnState3.setPressed(true);
                }
                if (item.type != null) {
                    tv1.append(item.type);
                    tv1.setPaintFlags(tv1.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                }
                if (item.color != null) {
                    tv2.append(item.color);
                    tv2.setPaintFlags(tv2.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                }
                if (item.details != null) {
                    tv3.append(item.details);
                    tv3.setPaintFlags(tv3.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                }
                if (item.material != null) {
                    tv4.append(item.material);
                    tv4.setPaintFlags(tv4.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                }
                if (item.season != null) {
                    tv5.append(item.season);
                    tv5.setPaintFlags(tv5.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                }
                if (item.givenBy != null) {
                    tv6.append(item.givenBy);
                    tv6.setPaintFlags(tv6.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                }

                setTitle(item.name);

                btnState1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnState1.setPressed(true);
                    }
                });
                btnState2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnState2.setPressed(true);
                    }
                });
                btnState3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnState3.setPressed(true);
                    }
                });
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {

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
}
